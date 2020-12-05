package com.knackitsolutions.crm.imaginepenguins.dbservice.service;

import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

public class SortingService {

    private static Sort.Direction getSortOrder(String direction) {
        if (direction.equalsIgnoreCase("desc")) {
            return Sort.Direction.DESC;
        } else if (direction.equalsIgnoreCase("asc")) {
            return Sort.Direction.ASC;
        }
        throw new IllegalArgumentException("Sort Direction Invalid. Valid options are: desc, asc");
    }

    public static Sort sort(String[] sort) {
        List<Sort.Order> orders = new ArrayList<>();
        if (sort[0].contains(",")) {
            for (String sortOrder : sort) {
                String[] _sort = sortOrder.split(",");
                orders.add(new Sort.Order(getSortOrder(_sort[1]), _sort[0]));
            }
        } else {
            orders.add(new Sort.Order(getSortOrder(sort[1]), sort[0]));
        }
        return Sort.by(orders);
    }
}
