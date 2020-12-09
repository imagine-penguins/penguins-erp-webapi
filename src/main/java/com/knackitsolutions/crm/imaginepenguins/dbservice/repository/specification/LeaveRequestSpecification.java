package com.knackitsolutions.crm.imaginepenguins.dbservice.repository.specification;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.LeaveRequestStatus;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.UserType;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.LeaveRequest;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.JoinType;
import java.util.List;

public class LeaveRequestSpecification {
    public static final Specification<LeaveRequest> leaveRequestByUserId(Long userId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
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

    public static final Specification<LeaveRequest> leaveRequestByUserType(List<UserType> userTypes) {
        return (root, query, criteriaBuilder) -> {
            CriteriaBuilder.In<UserType> userTypeIn = criteriaBuilder.in(
                    root.join("user").get("userType"));
            userTypes.forEach(userTypeIn::value);
            return userTypeIn;
        };
    }

    public static final Specification<LeaveRequest> leaveRequestByDepartmentId(List<Integer> departments) {
        return (root, query, criteriaBuilder) -> {
            CriteriaBuilder.In<Integer> inDepartment = criteriaBuilder
                    .in(root
                            .join("user", JoinType.INNER)
                            .join("userDepartments", JoinType.LEFT)
                            .join("instituteDepartment", JoinType.LEFT)
                            .get("id")
                    );
            departments.forEach(inDepartment::value);
            return inDepartment;
        };
    }


}
