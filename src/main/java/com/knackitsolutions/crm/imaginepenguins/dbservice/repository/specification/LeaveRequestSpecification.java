package com.knackitsolutions.crm.imaginepenguins.dbservice.repository.specification;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.LeaveRequestStatus;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.UserType;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.LeaveRequest;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.JoinType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LeaveRequestSpecification {
    public static final Specification<LeaveRequest> leaveRequestByUserId(Long userId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                root.join("user").get("id")
                , userId
        );
    }

    public static final Specification<LeaveRequest> leaveRequestByNotUserId(Long userId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.notEqual(
                root.join("user").get("id")
                , userId
        );
    }

    public static final Specification<LeaveRequest> leaveRequestByApprovesId(Long approvesId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                root.join("approves").get("id")
                , approvesId
        );
    }

    public static final Specification<LeaveRequest> leaveRequestByApprovedBy(Long approvedByUserId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                root.join("approvedBy").get("id")
                , approvedByUserId
        );
    }

    public static final Specification<LeaveRequest> leaveRequestByUserType(Stream<UserType> userTypes) {
        return (root, query, criteriaBuilder) -> {
            CriteriaBuilder.In<UserType> userTypeIn = criteriaBuilder.in(
                    root.join("user").get("userType"));
            userTypes.forEach(userTypeIn::value);
            return userTypeIn;
        };
    }

    public static final Specification<LeaveRequest> leaveRequestByDepartmentId(Stream<Integer> departments) {
        return (root, query, criteriaBuilder) -> {
            CriteriaBuilder.In<Integer> inDepartment = criteriaBuilder
                    .in(root
                            .join("user", JoinType.INNER)
                            .joinSet("userDepartments", JoinType.LEFT)
                            .join("instituteDepartment", JoinType.LEFT)
                            .get("id")
                    );
            departments.forEach(inDepartment::value);
            return inDepartment;
        };
    }

    public static final Specification<LeaveRequest> filterLeaveRequests(Map<String, List<SearchCriteria>> searchMap) {
        Specification<LeaveRequest> leaveRequestSpecification = Specification.where(null);
        for (Map.Entry<String, List<SearchCriteria>> stringListEntry : searchMap.entrySet()) {
            String key = stringListEntry.getKey();
            SearchCriteria firstValue = stringListEntry.getValue().get(0);
            Stream<String> value = stringListEntry.getValue().stream().map(searchCriteria -> searchCriteria.getValue().toString());
            if (key.equalsIgnoreCase("userType")) {
                Stream<UserType> userTypes = value.map(UserType::of);
                leaveRequestSpecification = leaveRequestSpecification.and(leaveRequestByUserType(userTypes));
            } else if (key.equalsIgnoreCase("department")) {
                Stream<Integer> departments = value.map(Integer::parseInt);
                leaveRequestSpecification = leaveRequestSpecification.and(leaveRequestByDepartmentId(departments));
            } else if (key.equalsIgnoreCase("requestStatus")) {
                List<LeaveRequestStatus> leaveRequestStatusStream = value.map(LeaveRequestStatus::of).collect(Collectors.toList());
                leaveRequestSpecification = leaveRequestSpecification.and(new GenericSpecification<>(new SearchCriteria(
                        "leaveRequestStatus", leaveRequestStatusStream, SearchOperation.IN
                )));
            } else if (key.equalsIgnoreCase("startDate")) {
                try {
                    Date startDate = new SimpleDateFormat("dd-MM-yyyy").parse(firstValue.getValue().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                leaveRequestSpecification = leaveRequestSpecification.and(new GenericSpecification<>(new SearchCriteria(
                        key, firstValue.getValue(), firstValue.getOperation()
                )));
            } else if (key.equalsIgnoreCase("endDate")) {
                try {
                    Date startDate = new SimpleDateFormat("dd-MM-yyyy").parse(firstValue.getValue().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                leaveRequestSpecification = leaveRequestSpecification.and(new GenericSpecification<>(new SearchCriteria(
                        key, firstValue.getValue(), firstValue.getOperation()
                )));
            }

        }
        return leaveRequestSpecification;
    }
}
