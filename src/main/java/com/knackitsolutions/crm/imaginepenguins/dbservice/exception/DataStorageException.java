package com.knackitsolutions.crm.imaginepenguins.dbservice.exception;

public class DataStorageException extends RuntimeException {
    public DataStorageException(String message) {
        super(message);
    }

    public DataStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
