package com.knackitsolutions.crm.imaginepenguins.dbservice.constant;

import com.knackitsolutions.crm.imaginepenguins.dbservice.controller.*;
import org.springframework.hateoas.Link;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public enum PrivilegeCode {

    ATTENDANCE("ATD"){
        @Override
        public List<Link> getLinks() {
            return null;
        }
    },
    CALENDAR("CLD"){
        @Override
        public List<Link> getLinks() {
            return null;
        }
    },
    VIEW_SELF_ATTENDANCE("view-self-attendance"){
        @Override
        public List<Link> getLinks() {
            return Arrays.asList(linkTo(methodOn(UserControllerImpl.class).viewAttendance( null, null))
                    .withRel("view-self-attendance"));
        }
    },
    MARK_EMPLOYEE_ATTENDANCE("mark-employee-attendance"){
        @Override
        public List<Link> getLinks(){
            return Arrays.asList(linkTo(methodOn(EmployeeController.class).loadEmployeesByDepartment(null))
                            .withRel("employees")
                    , linkTo(methodOn(UserControllerImpl.class).loadDepartments()).withRel("load-departments")
                );
        }
    },
    MARK_STUDENT_ATTENDANCE("mark-student-attendance"){
        @Override
        public List<Link> getLinks() {
            return Arrays.asList(linkTo(methodOn(TeacherController.class).classes()).withRel("load-classes")
                    ,linkTo(methodOn(AttendanceController.class)
                            .userAttendance(null, null, null, null)).withRel("save-attendance"));
        }

    },
    VIEW_STUDENTS_ATTENDANCE_HISTORY("view-student-attendance-history"){
        @Override
        public List<Link> getLinks() {
            return Arrays.asList();
        }

    },
    EDIT_STUDENTS_ATTENDANCE_HISTORY("edit-student-attendance-history") {
        @Override
        public List<Link> getLinks() {
            return Arrays.asList(
                    linkTo(methodOn(AttendanceController.class)
                        .userAttendanceHistory(null, null, null, null, null))
                        .withRel(PrivilegeCode.VIEW_STUDENTS_ATTENDANCE_HISTORY.getPrivilegeCode()),
                    linkTo(methodOn(AttendanceController.class)
                        .updateAttendance(null, null, null))
                        .withRel(this.getPrivilegeCode()));
        }
    },
    EDIT_EMPLOYEE_ATTENDANCE_HISTORY("edit-employee-attendance-history") {
        @Override
        public List<Link> getLinks() {
            return Arrays.asList(linkTo(methodOn(AttendanceController.class)
                    .userAttendanceHistory(null, null, null, null, null))
                    .withRel("attendance-history"));
        }
    },
    APPLY_LEAVE_REQUEST("apply-leave-request"){
        @Override
        public List<Link> getLinks() {
            return Arrays.asList(
                    linkTo(methodOn(UserControllerImpl.class).saveLeaveRequest(null))
                            .withRel(this.getPrivilegeCode()),
                    linkTo(methodOn(UserControllerImpl.class).leaveRequestHistory())
                            .withRel("view-leave-requests"),
                    linkTo(methodOn(UserControllerImpl.class).updateLeaveRequest(null, null))
                            .withRel("update-leave-request")
            );
        }
    },
    UPDATE_LEAVE_REQUEST("update-leave-request"){
        @Override
        public List<Link> getLinks() {
            return Arrays.asList(
                    linkTo(methodOn(AttendanceController.class).updateLeaveRequestStatus(null, null, null))
                            .withRel("update-leave-request-status"),
                    linkTo(methodOn(AttendanceController.class).leaveRequestHistory(null, null, null))
                            .withRel("view-leave-request-history")
            );
        }
    };

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
