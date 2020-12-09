package com.knackitsolutions.crm.imaginepenguins.dbservice.constant;

import com.knackitsolutions.crm.imaginepenguins.dbservice.controller.*;
import com.knackitsolutions.crm.imaginepenguins.dbservice.controller.attendance.AttendanceHistoryController;
import com.knackitsolutions.crm.imaginepenguins.dbservice.controller.attendance.LeaveRequestController;
import com.knackitsolutions.crm.imaginepenguins.dbservice.controller.attendance.AttendanceController;
import org.springframework.hateoas.Link;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public enum PrivilegeCode {

    ATTENDANCE("attendance"){
        @Override
        public List<Link> getLinks() {
            return null;
        }
    },
    CALENDAR("calendar"){
        @Override
        public List<Link> getLinks() {
            return null;
        }
    },
    VIEW_ATTENDANCE_HISTORY("view-attendance-history"){
        @Override
        public List<Link> getLinks() {
            return Arrays.asList();
        }
    },
    VIEW_SELF_ATTENDANCE_HISTORY("view-self-attendance-history"){
        @Override
        public List<Link> getLinks() {
            return Arrays.asList(linkTo(methodOn(AttendanceController.class).selfAttendance( null, null))
                    .withRel(this.getPrivilegeCode()));
        }
    },
    MARK_EMPLOYEE_ATTENDANCE("mark-employee-attendance"){
        @Override
        public List<Link> getLinks(){
            return Arrays.asList(linkTo(methodOn(AttendanceController.class).userAttendance(null))
                            .withRel(MARK_ATTENDANCE.getPrivilegeCode()));
        }
    },
    MARK_SUBORDINATES_EMPLOYEE_ATTENDANCE("mark-subordinates-employee-attendance") {
        @Override
        public List<Link> getLinks() {
            return Arrays.asList(linkTo(methodOn(AttendanceController.class).userAttendance(null))
                    .withRel(MARK_ATTENDANCE.getPrivilegeCode()));
        }
    },
    MARK_ATTENDANCE("mark-attendance"){
        @Override
        public List<Link> getLinks() {
            return null;
        }
    },
    MARK_STUDENT_ATTENDANCE("mark-student-attendance"){
        @Override
        public List<Link> getLinks() {
            return Arrays.asList(linkTo(methodOn(AttendanceController.class)
                            .userAttendance(null)).withRel(MARK_ATTENDANCE.getPrivilegeCode()));
        }

    },
    MARK_CLASS_STUDENT_ATTENDANCE("mark-class-student-attendance"){
        @Override
        public List<Link> getLinks() {
            return Arrays.asList(linkTo(methodOn(AttendanceController.class)
                    .userAttendance(null)).withRel(MARK_ATTENDANCE.getPrivilegeCode()));
        }

    },
    EDIT_ATTENDANCE("edit-attendance"){
        @Override
        public List<Link> getLinks() {
            return Arrays.asList();
        }
    },
    EDIT_CLASS_STUDENTS_ATTENDANCE_HISTORY("edit-class-student-attendance-history"){
        @Override
        public List<Link> getLinks() {
            return Arrays.asList(
                    linkTo(methodOn(AttendanceHistoryController.class)
                            .userAttendanceHistory(null, null, null, null, 0, 1))
                            .withRel(VIEW_ATTENDANCE_HISTORY.getPrivilegeCode())
            );
        }
    },
    EDIT_STUDENTS_ATTENDANCE_HISTORY("edit-student-attendance-history") {
        @Override
        public List<Link> getLinks() {
            return Arrays.asList(
                    linkTo(methodOn(AttendanceHistoryController.class)
                        .userAttendanceHistory(null, null, null, null, 0, 10))
                        .withRel("view-attendance-history"),
                    linkTo(methodOn(AttendanceController.class)
                        .updateAttendance(null, null, null))
                        .withRel(EDIT_ATTENDANCE.getPrivilegeCode()));
        }
    },
    EDIT_EMPLOYEE_ATTENDANCE_HISTORY("edit-employee-attendance-history") {
        @Override
        public List<Link> getLinks() {
            return Arrays.asList(linkTo(methodOn(AttendanceHistoryController.class)
                    .userAttendanceHistory(null, null, null, null, 0, 10))
                    .withRel(VIEW_ATTENDANCE_HISTORY.getPrivilegeCode()));
        }
    },
    APPLY_LEAVE_REQUEST("apply-leave-request"){
        @Override
        public List<Link> getLinks() {
            return Arrays.asList(
                    linkTo(methodOn(LeaveRequestController.class).saveLeaveRequest(null))
                            .withRel(this.getPrivilegeCode()),
                    linkTo(methodOn(LeaveRequestController.class).all(null, null, 0, 10, null, null))
                            .withRel("view-leave-requests"),
                    linkTo(methodOn(LeaveRequestController.class).updateLeaveRequest(null, null))
                            .withRel("update-leave-request")
            );
        }
    },
    VIEW_LEAVE_REQUEST("view-leave-request"){
        @Override
        public List<Link> getLinks() {
            return null;
        }
    },
    VIEW_RECEIVED_LEAVE_REQUEST("view-received-leave-request"){
        @Override
        public List<Link> getLinks() {
            return null;
        }
    },
    VIEW_APPLIED_LEAVE_REQUEST("view-applied-leave-request"){
        @Override
        public List<Link> getLinks() {
            return null;
        }
    },
    UPDATE_LEAVE_REQUEST("update-leave-request"){
        @Override
        public List<Link> getLinks() {
            return Arrays.asList(
                    linkTo(methodOn(LeaveRequestController.class).updateLeaveRequestStatus(null, null, null))
                            .withRel("update-leave-request-status"),
                    linkTo(methodOn(LeaveRequestController.class).leaveRequestHistory(null, null, 0, 10, null, null))
                            .withRel("view-leave-request-history")
            );
        }
    },
    UPDATE_LEAVE_REQUEST_STATUS("update-leave-request-status"){
        @Override
        public List<Link> getLinks() {
            return null;
        }
    },
    MANAGE_USERS("manage-users"){
        @Override
        public List<Link> getLinks() {
            return Arrays.asList(
                    linkTo(methodOn(UserControllerImpl.class)
                            .all(0, 1, null, null))
                            .withRel("users"),
                    linkTo(methodOn(UserControllerImpl.class).hierarchy(null)).withRel("hierarchy")
            );
        }
    }
    /*,
    HIERARCHY_VIEW("hierarchy"){
        @Override
        public List<Link> getLinks() {
            return Arrays.asList(linkTo(methodOn(UserControllerImpl.class).hierarchy(null)).withRel("hierarchy"));
        }
    }*/
    ;

    private String privilegeCode;

    PrivilegeCode(String privilegeCode) {
        this.privilegeCode = privilegeCode;
    }

    public String getPrivilegeCode() {
        return privilegeCode;
    }

    public void setPrivilegeCode(String privilegeCode) {
        this.privilegeCode = privilegeCode;
    }

    public static PrivilegeCode of(String value) {
        return Stream
                .of(PrivilegeCode.values())
                .filter(code -> code.getPrivilegeCode().equals(value))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public abstract List<Link> getLinks();
}
