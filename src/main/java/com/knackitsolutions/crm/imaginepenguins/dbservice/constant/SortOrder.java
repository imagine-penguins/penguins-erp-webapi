package com.knackitsolutions.crm.imaginepenguins.dbservice.constant;

import com.knackitsolutions.crm.imaginepenguins.dbservice.common.sort.Sortable;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.UserListDTO;

import java.util.Comparator;
import java.util.stream.Stream;

public enum SortOrder {
    ASCENDING("asc"){
        @Override
        public Comparator<Sortable> order(Comparator<Sortable> comparator) {
            return comparator;
        }
    },
    DESCENDING("desc"){
        @Override
        public Comparator<Sortable> order(Comparator<Sortable> comparator) {
            return comparator.reversed();
        }
    };

    private String order;

    SortOrder(String order) {
        this.order = order;
    }

    public String getOrder() {
        return order;
    }

    public static SortOrder of(String order) {
        return Stream
                .of(SortOrder.values())
                .filter(sortOrder -> order.equalsIgnoreCase(sortOrder.getOrder()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Cannot sort in the given order: \"" + order + "\""));
    }

    public abstract Comparator<Sortable> order(Comparator<Sortable> comparator);
}
