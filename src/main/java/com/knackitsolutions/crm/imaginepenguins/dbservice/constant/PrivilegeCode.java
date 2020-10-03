package com.knackitsolutions.crm.imaginepenguins.dbservice.constant;

import com.google.common.collect.ImmutableList;
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
    VIEW_SELF_ATTENDANCE("VSATD"){
        @Override
        public List<Link> getLinks() {
            return Arrays.asList(linkTo(methodOn(UserControllerImpl.class).viewAttendance(null, null, null))
                    .withRel("view-self-attendance"));
        }

    },
    MARK_EMPLOYEE_ATTENDANCE("MEATD"){
        @Override
        public List<Link> getLinks() {
            return Arrays.asList(linkTo(methodOn(EmployeeController.class).one(null)).withRel("employee"));
        }
    },
    MARK_STUDENT_ATTENDANCE("MSATD"){
        @Override
        public List<Link> getLinks() {
            return Arrays.asList(linkTo(methodOn(TeacherController.class).classes(null)).withRel("load-classes")
                    ,linkTo(methodOn(AttendanceController.class).studentAttendance(null)).withRel("save-attendance"));
        }

    },
    VIEW_STUDENTS_ATTENDANCE_HISTORY("VSATDH"){
        @Override
        public List<Link> getLinks() {
            return Arrays.asList(linkTo(methodOn(AttendanceController.class)
                    .attendanceHistory(null, null, null, null))
                    .withRel("view-attendance-history"));
        }

    },
    EDIT_STUDENTS_ATTENDANCE_HISTORY("ESAH") {
        @Override
        public List<Link> getLinks() {
            return Arrays.asList(linkTo(methodOn(AttendanceController.class)
                    .updateAttendance(null, null, null))
                    .withRel("edit-attendance-history"));
        }
    },
    EDIT_EMPLOYEE_ATTENDANCE_HISTORY("EEAH") {
        @Override
        public List<Link> getLinks() {
            return Arrays.asList(linkTo(methodOn(AttendanceController.class)
                    .updateAttendance(null, null, null))
                    .withRel("edit-attendance-history"));
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
