package com.knackitsolutions.crm.imaginepenguins.dbservice.constant;

import java.util.stream.Stream;

public enum PrivilegeCode {

    ATTENDANCE("ATD"),
    CALENDAR("CLD"),
    VIEW_SELF_ATTENDANCE("VSATD"),
    MARK_EMPLOYEE_ATTENDANCE("MEATD"),
    MARK_STUDENT_ATTENDANCE("MSATD"),
    VIEW_STUDENTS_ATTENDANCE_HISTORY("VSATDH");

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
}
