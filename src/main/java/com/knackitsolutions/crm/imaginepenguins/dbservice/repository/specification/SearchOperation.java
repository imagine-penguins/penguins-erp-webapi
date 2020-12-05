package com.knackitsolutions.crm.imaginepenguins.dbservice.repository.specification;

import java.util.Arrays;

public enum SearchOperation {
    GREATER_THAN(">"),
    LESS_THAN("<"),
    GREATER_THAN_EQUAL(">:"),
    LESS_THAN_EQUAL("<:"),
    NOT_EQUAL("!:"),
    EQUAL(":"),
    MATCH("%"),
    MATCH_END("%>"),
    IN("");
    private String operation;

    SearchOperation(final String operation) {
        this.operation = operation;
    }

    public String getOperation() {
        return operation;
    }

    public static SearchOperation of(final String operation) {
        return Arrays
                .stream(SearchOperation.values())
                .filter(searchOperation -> searchOperation.getOperation().equals(operation))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Operation not found for argument: " + operation));
    }
}
