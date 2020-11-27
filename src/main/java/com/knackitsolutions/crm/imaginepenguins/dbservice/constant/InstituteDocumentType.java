package com.knackitsolutions.crm.imaginepenguins.dbservice.constant;

import java.io.Serializable;
import java.util.Arrays;

public enum InstituteDocumentType implements Serializable {
    LOGO("LOGO"),
    COVER_PICTURE("COVER");
    private String docType;

    InstituteDocumentType(String docType) {
        this.docType = docType;
    }

    public String getDocType() {
        return docType;
    }

    public static final InstituteDocumentType of(String docType) {
        return Arrays
                .stream(InstituteDocumentType.values())
                .filter(instituteDocumentType -> instituteDocumentType.getDocType().equalsIgnoreCase(docType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Institute document of given type: " + docType + " is not present."));
    }
}