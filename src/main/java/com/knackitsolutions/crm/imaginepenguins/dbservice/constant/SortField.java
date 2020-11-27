package com.knackitsolutions.crm.imaginepenguins.dbservice.constant;

import com.knackitsolutions.crm.imaginepenguins.dbservice.common.sort.Sortable;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.UserListDTO;

import java.util.Comparator;
import java.util.stream.Stream;

public enum SortField {
    FIRST_NAME("first-name"){
        @Override
        public Comparator<Sortable> comparator() {
            return Comparator
                    .comparing(Sortable::getFirstName);
        }
    },
    LAST_NAME("last-name"){
        @Override
        public Comparator<Sortable> comparator() {
            return Comparator
                    .comparing(Sortable::getLastName);
        }
    },
    ROLL_NUMBER("roll-number"){
        @Override
        public Comparator<Sortable> comparator() {
            return Comparator.comparing(Sortable::getRollNumber);
        }
    },
    EMPLOYEE_ID("employee-id"){
        @Override
        public Comparator<Sortable> comparator() {
            return Comparator.comparing(Sortable::getEmployeeId);
        }
    },
    SECTION("class-section"){
        @Override
        public Comparator<Sortable> comparator() {
            return Comparator.comparing(Sortable::getSection);
        }
    },
    ATTENDANCE_STATUS("attendance-status"){
        @Override
        public Comparator<Sortable> comparator() {
            return Comparator.comparing(Sortable::getAttendanceStatus);
        }
    },
    USER_TYPE("user-type"){
        @Override
        public Comparator<Sortable> comparator() {
            return Comparator.comparing(Sortable::getUserType);
        }
    },
    ACTIVE_STATUS("active-status"){
        @Override
        public Comparator<Sortable> comparator() {
            return Comparator.comparing(Sortable::getActiveStatus);
        }
    },
    PHONE("phone"){
        @Override
        public Comparator<Sortable> comparator() {
            return Comparator
                    .comparing(Sortable::getPhone);
        }
    },
    EMAIL("email"){
        @Override
        public Comparator<Sortable> comparator() {
            return Comparator.comparing(Sortable::getEmail);
        }
    };

    private String fieldName;

    SortField(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public static SortField of(String fieldName) {
        return Stream
                .of(SortField.values())
                .filter(sortField -> fieldName.equals(sortField.getFieldName()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("\"" + fieldName + "\" sorting field not found"));
    }

    public abstract Comparator<Sortable> comparator();

}
