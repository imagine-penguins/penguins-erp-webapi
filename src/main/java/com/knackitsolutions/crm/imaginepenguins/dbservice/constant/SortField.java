package com.knackitsolutions.crm.imaginepenguins.dbservice.constant;

import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.UserListDTO;

import java.util.Comparator;
import java.util.stream.Stream;

public enum SortField {
    NAME("name"){
        @Override
        public Comparator<UserListDTO.UserDTO> comparator() {
            return Comparator
                    .comparing(UserListDTO.UserDTO::getFirstName)
                    .thenComparing(UserListDTO.UserDTO::getLastName);
        }
    },
    TYPE("type"){
        @Override
        public Comparator<UserListDTO.UserDTO> comparator() {
            return Comparator.comparing(UserListDTO.UserDTO::getUserType);
        }
    },
    STATUS("status"){
        @Override
        public Comparator<UserListDTO.UserDTO> comparator() {
            return Comparator.comparing(UserListDTO.UserDTO::getActive);
        }
    },
    PHONE("phone"){
        @Override
        public Comparator<UserListDTO.UserDTO> comparator() {
            return Comparator
                    .comparing(userDTO -> userDTO.getContact().getPhone());
        }
    },
    EMAIL("email"){
        @Override
        public Comparator<UserListDTO.UserDTO> comparator() {
            return Comparator.comparing(userDTO -> userDTO.getContact().getEmail());
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

    public abstract Comparator<UserListDTO.UserDTO> comparator();

}
