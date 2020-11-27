package com.knackitsolutions.crm.imaginepenguins.dbservice.common.sort;

public interface Sortable {
    default String getFirstName() {
        return "";
    }

    default String getLastName() {
        return "";
    }

    default String getRollNumber(){
        return "";
    }

    default String getEmployeeId(){
        return "";
    }

    default String getSection(){
        return "";
    }

    default String getAttendanceStatus() {
        return "";
    }

    default String getUserType() {
        return "";
    }

    default Boolean getActiveStatus() {
        return false;
    }

    default String getPhone() {
        return "";
    }

    default String getEmail() {
        return "";
    }
}
