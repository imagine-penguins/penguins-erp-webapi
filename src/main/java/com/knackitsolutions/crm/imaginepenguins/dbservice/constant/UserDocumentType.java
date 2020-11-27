package com.knackitsolutions.crm.imaginepenguins.dbservice.constant;

import java.io.Serializable;
import java.util.Arrays;

public enum UserDocumentType implements Serializable {
    PASSPORT_PICTURE("PASSPORT"),
    DISPLAY_PICTURE("DISPLAY");

    String docType;

    UserDocumentType(String docType) {
        this.docType = docType;
    }

    public String getDocType() {
        return docType;
    }

    public static UserDocumentType of(String docType) {
        return Arrays
                .stream(UserDocumentType.values())
                .filter(userDocumentType -> userDocumentType.getDocType().equalsIgnoreCase(docType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("User document of given type: " + docType + " is not present."));
    }

}
