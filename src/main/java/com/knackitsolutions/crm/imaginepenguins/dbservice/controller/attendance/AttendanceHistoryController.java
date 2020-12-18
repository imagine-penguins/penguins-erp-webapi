package com.knackitsolutions.crm.imaginepenguins.dbservice.controller.attendance;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.AttendanceStatus;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.Period;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.PrivilegeCode;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.UserType;
import com.knackitsolutions.crm.imaginepenguins.dbservice.controller.EmployeeController;
import com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.attendance.AttendanceResponseMapper;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance.UserAttendanceResponseDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.*;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.EmployeeAttendance;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.StudentAttendance;
import com.knackitsolutions.crm.imaginepenguins.dbservice.exception.UserNotFoundException;
import com.knackitsolutions.crm.imaginepenguins.dbservice.facade.IAuthenticationFacade;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.specification.EmployeeSpecification;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.specification.SearchCriteria;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.specification.StudentSpecification;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.specification.UserSpecification;
import com.knackitsolutions.crm.imaginepenguins.dbservice.security.model.UserContext;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/attendance")
@RequiredArgsConstructor
@Validated
@Slf4j
public class AttendanceHistoryController {

    private final IAuthenticationFacade authenticationFacade;
    private final UserService userService;
    private final StudentService studentService;
    private final EmployeeService employeeService;
    private final AttendanceResponseMapper attendanceResponseMapper;

    private UserAttendanceResponseDTO addLinks(UserAttendanceResponseDTO user
            , String[] sort, Optional<Period> period, Optional<String> value
            , int page, int size, List<PrivilegeCode> privilegeCodes) {
        String[] search = new String[1];
        search[0] = "userId:" + user.getUserId();
        if (privilegeCodes.contains(PrivilegeCode.EDIT_STUDENTS_ATTENDANCE_HISTORY)
                || privilegeCodes.contains(PrivilegeCode.EDIT_EMPLOYEE_ATTENDANCE_HISTORY)) {
            user.add(linkTo(methodOn(AttendanceController.class)
                    .updateAttendance(user.getAttendanceId().get()
                            , user.getUserId(), null))
                    .withRel(PrivilegeCode.EDIT_STUDENTS_ATTENDANCE_HISTORY.getPrivilegeCode()));
        }

        user
                .add(WebMvcLinkBuilder.linkTo(methodOn(EmployeeController.class).one(user.getUserId())).withRel("profile"));
        user
                .add(linkTo(methodOn(AttendanceHistoryController.class)
                        .userAttendanceHistory(search, sort, period, value, page, size))
                        .withRel("view-user-attendance"));
        return user;
    }

    @GetMapping("/history")
    public PagedModel<UserAttendanceResponseDTO> userAttendanceHistory(
            @RequestParam(required = false) String[] search
            , @RequestParam(defaultValue = "id,desc") String[] sort
            , @RequestParam(name = "period") Optional<Period> period
            , @RequestParam(name = "value") Optional<String> value
            , @RequestParam(defaultValue = "0") @Min(0) int page
            , @RequestParam(defaultValue = "10") @Min(1) int size
    ) {

        UserContext userContext = (UserContext) authenticationFacade.getAuthentication().getPrincipal();
        Pageable pageable = PageRequest.of(page, size, SortingService.sort(sort));

        Optional<Date> startDate = period.flatMap(p -> FilterService.periodStartDateValue(p, value));
        Optional<Date> endDate = period.flatMap(p -> FilterService.periodEndDateValue(p, value));

        List<PrivilegeCode> privilegeCodes = userContext
                .getAuthorities()
                .stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .map(PrivilegeCode::of)
                .collect(Collectors.toList());

        Map<String, List<SearchCriteria>> searchMap = FilterService.createSearchMap(search);
        List<UserAttendanceResponseDTO> users = new ArrayList<>();
        Specification<User> userSpecification = null;
        try {
            userSpecification = UserSpecification.filterUsers(searchMap);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to parse date from search parameters.");
        }
        Specification<User> specByPrivileges = Specification.where(null);
        if (userContext.getAuthorities().contains(
                new SimpleGrantedAuthority(PrivilegeCode.EDIT_STUDENTS_ATTENDANCE_HISTORY.getPrivilegeCode())
        )) {
            specByPrivileges = specByPrivileges.or(UserSpecification.studentsByInstituteId(userContext.getInstituteId()));
        }
        if (userContext.getAuthorities().contains(
                new SimpleGrantedAuthority(PrivilegeCode.EDIT_CLASS_STUDENTS_ATTENDANCE_HISTORY.getPrivilegeCode())
        )) {
            User user = userService
                    .findById(userContext.getUserId())
                    .orElseThrow(() -> new UserNotFoundException(userContext.getUserId()));
            Set<InstituteClassSection> instituteClassSections = ((Teacher) user).getInstituteClassSections();
            List<Long> instituteClassSectionIds = instituteClassSections
                    .stream().map(i -> i.getId()).collect(Collectors.toList());
            specByPrivileges = specByPrivileges.or(UserSpecification.studentByClassIn(instituteClassSectionIds));
        }
        if (userContext.getAuthorities().contains(
                new SimpleGrantedAuthority(PrivilegeCode.EDIT_EMPLOYEE_ATTENDANCE_HISTORY.getPrivilegeCode())
        )) {
            specByPrivileges = specByPrivileges.or(
                    UserSpecification.employeesByInstituteId(userContext.getInstituteId())
            );
        }
        userSpecification = userSpecification.and(specByPrivileges);
        Page<User> all = userService.findAll(userSpecification, pageable);
        all.flatMap(user -> {
            if (user.getUserType() == UserType.STUDENT) {
                return ((Student) user).getStudentAttendances().stream();
            }
            else if(user.getUserType() == UserType.EMPLOYEE) {
                return ((Employee) user).getEmployeeAttendances().stream();
            }
            return Stream.empty();
        }).map(o -> {
            if (o instanceof EmployeeAttendance) {
                return attendanceResponseMapper.mapUserAttendanceToEmployee((EmployeeAttendance) o);
            }
            else if(o instanceof StudentAttendance) {
                return attendanceResponseMapper.mapUserAttendanceToStudent((StudentAttendance) o);
            }
            return Optional.empty();
        }).map(o -> this.addLinks((UserAttendanceResponseDTO) o, sort, period, value, page, size, privilegeCodes));
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(size, page, users.size(), all.getTotalPages());
        return PagedModel.of(users, pageMetadata, linkTo(methodOn(AttendanceHistoryController.class)
                .userAttendanceHistory(search, sort, period, value, page, size))
                .withRel("view-attendance-history"));
    }

    @GetMapping("/history/graph")
    public EntityModel<Map<String, Long>> graphData(
            @RequestParam(required = false) String[] search
            , @RequestParam(name = "period") Optional<Period> period
            , @RequestParam(name = "value") Optional<String> value) {
        UserContext userContext = (UserContext) authenticationFacade.getAuthentication().getPrincipal();
        Map<String, Long> graphData = new HashMap<>();
        Optional<Date> startDate = period
                .map(p -> FilterService.periodStartDateValue(p, value))
                .orElse(Optional.empty());
        Optional<Date> endDate = period
                .map(p -> FilterService.periodEndDateValue(p, value))
                .orElse(Optional.empty());
        Map<String, List<SearchCriteria>> searchMap = FilterService.createSearchMap(search);
        Specification<User> userSpecification = null;
        try {
            userSpecification = UserSpecification.filterUsers(searchMap);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to parse date from search parameters.");
        }
        Specification<User> specByPrivileges = Specification.where(null);
        if (userContext.getAuthorities().contains(
                new SimpleGrantedAuthority(PrivilegeCode.EDIT_STUDENTS_ATTENDANCE_HISTORY.getPrivilegeCode())
        )) {
            specByPrivileges = specByPrivileges.or(UserSpecification.studentsByInstituteId(userContext.getInstituteId()));
        }
        if (userContext.getAuthorities().contains(
                new SimpleGrantedAuthority(PrivilegeCode.EDIT_CLASS_STUDENTS_ATTENDANCE_HISTORY.getPrivilegeCode())
        )) {
            User user = userService
                    .findById(userContext.getUserId())
                    .orElseThrow(() -> new UserNotFoundException(userContext.getUserId()));
            Set<InstituteClassSection> instituteClassSections = ((Teacher) user).getInstituteClassSections();
            List<Long> instituteClassSectionIds = instituteClassSections
                    .stream().map(i -> i.getId()).collect(Collectors.toList());
            specByPrivileges = specByPrivileges.or(UserSpecification.studentByClassIn(instituteClassSectionIds));
        }
        if (userContext.getAuthorities().contains(
                new SimpleGrantedAuthority(PrivilegeCode.EDIT_EMPLOYEE_ATTENDANCE_HISTORY.getPrivilegeCode())
        )) {
            specByPrivileges = specByPrivileges.or(
                    UserSpecification.employeesByInstituteId(userContext.getInstituteId())
            );
        }
        userSpecification = userSpecification.and(specByPrivileges);

        Long presentCount = 0l;
        Long absentCount = 0l;
        Long leaveCount = 0l;
        presentCount = userService.count(
                userSpecification.and(UserSpecification.studentsByAttendanceStatus(AttendanceStatus.PRESENT))
        );
        absentCount = userService.count(
                userSpecification.and(UserSpecification.studentsByAttendanceStatus(AttendanceStatus.ABSENT))
        );
        leaveCount = userService.count(
                userSpecification.and(UserSpecification.studentsByAttendanceStatus(AttendanceStatus.LEAVE))
        );

        graphData.put("present", presentCount + presentCount);
        graphData.put("absent", absentCount + absentCount);
        graphData.put("leave", leaveCount + leaveCount);

        EntityModel<Map<String, Long>> entityModel = EntityModel.of(graphData);
        return entityModel;

    }
}
