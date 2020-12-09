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
        if(sort != null){
            if (sort[0].contains(",")) {
                for (String sortOrder : sort) {
                    String[] _sort = sortOrder.split(",");
                    Sort.Direction direction = Sort.Direction.ASC;
                    if (_sort.length > 1)
                        direction = getSortOrder(_sort[1]);
                    orders.add(new Sort.Order(direction, _sort[0]));
                }
            } else {
                Sort.Direction direction = Sort.Direction.ASC;
                if (sort.length > 1)
                    direction = getSortOrder(sort[1]);
                orders.add(new Sort.Order(direction, sort[0]));
            }
        }
        return Sort.by(orders);
    }
}
