package com.knackitsolutions.crm.imaginepenguins.dbservice.constant;

import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.UserListDTO;

import java.util.Comparator;
import java.util.stream.Stream;

public enum SortOrder {
    ASCENDING("asc"){
        @Override
        public Comparator<UserListDTO.UserDTO> order(Comparator<UserListDTO.UserDTO> comparator) {
            return comparator;
        }
    },
    DESCENDING("desc"){
        @Override
        public Comparator<UserListDTO.UserDTO> order(Comparator<UserListDTO.UserDTO> comparator) {
            return comparator.reversed();
        }
    };

    private String oder;

    SortOrder(String oder) {
        this.oder = oder;
    }

    public String getOder() {
        return oder;
    }

    public static SortOrder of(String order) {
        return Stream
                .of(SortOrder.values())
                .filter(sortOrder -> order.equalsIgnoreCase(sortOrder.getOder()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Cannot sort in the given order: \"" + order + "\""));
    }

    public abstract Comparator<UserListDTO.UserDTO> order(Comparator<UserListDTO.UserDTO> comparator);
}
