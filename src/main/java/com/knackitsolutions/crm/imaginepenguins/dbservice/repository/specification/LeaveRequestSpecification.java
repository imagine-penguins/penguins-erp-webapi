package com.knackitsolutions.crm.imaginepenguins.dbservice.repository.specification;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.LeaveRequestStatus;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.UserType;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.LeaveRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.JoinType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
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

    public static final Specification<LeaveRequest> leaveRequestByUserIds(Stream<Long> userIds) {
        return (root, query, criteriaBuilder) -> {
            CriteriaBuilder.In<Long> userTypeIn = criteriaBuilder.in(
                    root.join("user").get("id"));
            userIds.forEach(userTypeIn::value);
            return userTypeIn;
        };
    }

    public static final Specification<LeaveRequest> leaveRequestsByStartDate(final String key
            , final Date startDate, final SearchOperation searchOperation) {
        log.debug("where date is {} {}", searchOperation.getOperation(), startDate);
        return (root, query, criteriaBuilder) -> {
            Expression expression = root
                    .get(key);
            if (searchOperation == SearchOperation.GREATER_THAN) {
                return criteriaBuilder.greaterThan(expression, startDate);
            } else if (searchOperation == SearchOperation.GREATER_THAN_EQUAL) {
                return criteriaBuilder.greaterThanOrEqualTo(expression, startDate);
            } else if (searchOperation == SearchOperation.LESS_THAN) {
                return criteriaBuilder.lessThan(expression, startDate);
            } else if (searchOperation == SearchOperation.LESS_THAN_EQUAL) {
                return criteriaBuilder.lessThanOrEqualTo(expression, startDate);
            } else {
                return criteriaBuilder.equal(expression, startDate);
            }
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
                log.debug("Setting Request Status");
                List<LeaveRequestStatus> leaveRequestStatusStream = value.map(LeaveRequestStatus::of).collect(Collectors.toList());
                leaveRequestSpecification = leaveRequestSpecification.and(new GenericSpecification<>(new SearchCriteria(
                        "leaveRequestStatus", leaveRequestStatusStream, SearchOperation.IN
                )));
            } else if (key.equalsIgnoreCase("startDate")) {
                try {
                    log.debug("Setting startDate: {}", firstValue.getValue());
                    Date startDate = new SimpleDateFormat("dd_MM_yyyy").parse(firstValue.getValue().toString());
                    /*leaveRequestSpecification = leaveRequestSpecification.and(new GenericSpecification<>(new SearchCriteria(
                            "startDate", startDate, firstValue.getOperation()
                    )));*/
                    leaveRequestSpecification = leaveRequestSpecification
                            .and(leaveRequestsByStartDate("startDate", startDate, firstValue.getOperation()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else if (key.equalsIgnoreCase("endDate")) {
                try {
                    log.debug("Setting endDate: {}", firstValue.getValue());
                    Date endDate = new SimpleDateFormat("dd_MM_yyyy").parse(firstValue.getValue().toString());
                    leaveRequestSpecification = leaveRequestSpecification
                            .and(leaveRequestsByStartDate("endDate", endDate, firstValue.getOperation()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            } else if (key.equalsIgnoreCase("user")) {
                Stream<Long> userIds = value.map(Long::parseLong);
                leaveRequestSpecification = leaveRequestSpecification.and(LeaveRequestSpecification.leaveRequestByUserIds(userIds));
            }
        }
        return leaveRequestSpecification;
    }
}
