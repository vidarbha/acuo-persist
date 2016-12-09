package com.acuo.persist.core;

public class PersistenceException extends RuntimeException {

    private static final long serialVersionUID = 1005812541107158155L;

    public PersistenceException(String message) {
        super(message);
    }

    public PersistenceException(String message, Throwable cause) {
        super(message, cause);
    }

    public PersistenceException(Throwable cause) {
        super(cause);
    }
}