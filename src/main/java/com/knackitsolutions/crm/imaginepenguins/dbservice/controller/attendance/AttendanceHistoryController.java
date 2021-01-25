package com.knackitsolutions.crm.imaginepenguins.dbservice.controller.attendance;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.AttendanceStatus;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.Period;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.PrivilegeCode;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.UserType;
import com.knackitsolutions.crm.imaginepenguins.dbservice.controller.EmployeeController;
import com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.attendance.AttendanceResponseMapper;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance.UserAttendanceResponseDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.*;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.Attendance;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.EmployeeAttendance;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.StudentAttendance;
import com.knackitsolutions.crm.imaginepenguins.dbservice.exception.UserNotFoundException;
import com.knackitsolutions.crm.imaginepenguins.dbservice.facade.IAuthenticationFacade;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.specification.*;
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
    private final AttendanceResponseMapper attendanceResponseMapper;
    private final AttendanceService attendanceService;

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
                        .userAttendanceHistory(search, sort, period, value))
                        .withRel("view-user-attendance"));
        return user;
    }

    //ToDO find the last attendance taken and get all the attedance history of that.
    @GetMapping("/history")
    public CollectionModel<UserAttendanceResponseDTO> userAttendanceHistory(
            @RequestParam(required = false) String[] search
            , @RequestParam(defaultValue = "id,desc") String[] sort
            , @RequestParam(name = "period") Optional<Period> period
            , @RequestParam(name = "value") Optional<String> value
    ) {
        log.debug("/history");
        UserContext userContext = (UserContext) authenticationFacade.getAuthentication().getPrincipal();
//        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE, SortingService.sort(sort));

        Optional<Date> startDate = period.flatMap(p -> FilterService.periodStartDateValue(p, value));
        Optional<Date> endDate = period.flatMap(p -> FilterService.periodEndDateValue(p, value));

        List<PrivilegeCode> privilegeCodes = userContext
                .getAuthorities()
                .stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .map(PrivilegeCode::of)
                .collect(Collectors.toList());

        Map<String, List<SearchCriteria>> searchMap = FilterService.createSearchMap(search);

        Specification<Attendance> attendanceSpecification = null;
        try {
            attendanceSpecification = AttendanceSpecification.filterStudentAttendance(searchMap);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to parse date from search parameters.");
        }
        Specification<Attendance> specByPrivileges = Specification.where(null);
        if (privilegeCodes.contains(PrivilegeCode.EDIT_STUDENTS_ATTENDANCE_HISTORY)) {
            log.debug("EDIT_STUDENTS_ATTENDANCE_HISTORY: ");
            specByPrivileges = specByPrivileges.or(AttendanceSpecification.attendanceByInstituteId(userContext.getInstituteId()));
        }
        if (privilegeCodes.contains(PrivilegeCode.EDIT_CLASS_STUDENTS_ATTENDANCE_HISTORY)) {
            log.debug("EDIT_CLASS_STUDENTS_ATTENDANCE_HISTORY: ");
            User user = userService
                    .findById(userContext.getUserId())
                    .orElseThrow(() -> new UserNotFoundException(userContext.getUserId()));
            Set<InstituteClassSection> instituteClassSections = ((Teacher) user).getInstituteClassSections();
            Stream<Long> instituteClassSectionIds = instituteClassSections
                    .stream().map(i -> i.getId());
            specByPrivileges = specByPrivileges.or(AttendanceSpecification.attendanceByClass(instituteClassSectionIds));
        }
        if (privilegeCodes.contains(PrivilegeCode.EDIT_EMPLOYEE_ATTENDANCE_HISTORY.getPrivilegeCode())) {
            log.debug("EDIT_EMPLOYEE_ATTENDANCE_HISTORY: ");
            specByPrivileges = specByPrivileges.or(
                    AttendanceSpecification.attendanceByInstituteId(userContext.getInstituteId())
            );
        }
        if (privilegeCodes.contains(PrivilegeCode.EDIT_SUBORDINATES_EMPLOYEE_ATTENDANCE_HISTORY.getPrivilegeCode())) {
            log.debug("EDIT_SUBORDINATE_EMPLOYEE_ATTENDANCE_HISTORY: ");
            specByPrivileges = specByPrivileges.or(
                    AttendanceSpecification.attendanceBySupervisorId(userContext.getUserId())
            );
        }
        attendanceSpecification = attendanceSpecification.and(specByPrivileges);

        if (startDate.isPresent() && endDate.isPresent()) {
            log.debug("Filtering on dates provided in request param");
            attendanceSpecification = attendanceSpecification.and(AttendanceSpecification.attendanceWithAttendanceDate(startDate.get(), SearchOperation.GREATER_THAN_EQUAL));
            attendanceSpecification = attendanceSpecification.and(AttendanceSpecification.attendanceWithAttendanceDate(endDate.get(), SearchOperation.LESS_THAN_EQUAL));
        }else{
            //get the last attendance date.
            log.debug("Getting last day of attendance");
            Date lastAttendanceDate = attendanceService.lastAttendanceDate();
            attendanceSpecification =  attendanceSpecification.and(AttendanceSpecification.attendanceWithAttendanceDate(lastAttendanceDate, SearchOperation.EQUAL));
        }

        log.debug("Calling database to fetch attendance history");
        List<Attendance> attendances = attendanceService.findAll(attendanceSpecification, SortingService.sort(sort));
//        Page<User> all = Service.findAll(attendanceSpecification, pageable);

        log.debug("Fetching completed.");

        List<UserAttendanceResponseDTO> users = new ArrayList<>();

        for (Attendance attendance : attendances) {
            attendance.getStudentAttendances().stream().map(studentAttendance -> attendanceResponseMapper.mapUserAttendanceToStudent(studentAttendance))
                    .map(o -> this.addLinks(o, sort, period, value, 0, Integer.MAX_VALUE, privilegeCodes))
                    .forEach(users::add);
            attendance
                    .getEmployeeAttendances()
                    .stream()
                    .map(attendanceResponseMapper::mapUserAttendanceToEmployee)
                    .map(o -> this.addLinks(o, sort, period, value, 0, Integer.MAX_VALUE, privilegeCodes))
                    .forEach(users::add);
        }

        return CollectionModel.of(users, linkTo(methodOn(AttendanceHistoryController.class)
                .userAttendanceHistory(search, sort, period, value))
                .withRel("view-attendance-history"));
    }

    @GetMapping("/history/graph")
    public EntityModel<Map<String, Long>> graphData(
            @RequestParam(required = false) String[] search
            , @RequestParam(name = "period") Optional<Period> period
            , @RequestParam(name = "value") Optional<String> value) {
        log.debug("/history/graph");
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
                new SimpleGrantedAuthority(PrivilegeCode.EDIT_SUBORDINATES_EMPLOYEE_ATTENDANCE_HISTORY.getPrivilegeCode())
        )) {
            specByPrivileges = specByPrivileges.or(
                    UserSpecification.employeeByManagerId(Arrays.asList(userContext.getUserId()))
            );
        }
        if (userContext.getAuthorities().contains(
                new SimpleGrantedAuthority(PrivilegeCode.EDIT_EMPLOYEE_ATTENDANCE_HISTORY.getPrivilegeCode())
        )) {
            specByPrivileges = specByPrivileges.or(
                    UserSpecification.employeesByInstituteId(userContext.getInstituteId())
            );
        }
        userSpecification = userSpecification.and(specByPrivileges);

        log.debug("Calling db for the data.");
        Long presentCount = userService.count(
                userSpecification.and(UserSpecification.studentsByAttendanceStatus(AttendanceStatus.PRESENT))
        );
        Long absentCount = userService.count(
                userSpecification.and(UserSpecification.studentsByAttendanceStatus(AttendanceStatus.ABSENT))
        );
        Long leaveCount = userService.count(
                userSpecification.and(UserSpecification.studentsByAttendanceStatus(AttendanceStatus.LEAVE))
        );
        log.debug("db call ended.");
        graphData.put("present", presentCount);
        graphData.put("absent", absentCount);
        graphData.put("leave", leaveCount);

        EntityModel<Map<String, Long>> entityModel = EntityModel.of(graphData);
        return entityModel;
    }
}
