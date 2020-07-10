package com.itransition.lobach.renbook.exception;

public class EmptyResultException extends Exception{

    public EmptyResultException() {
    }

    public EmptyResultException(String message) {
        super(message);
    }

    public EmptyResultException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmptyResultException(Throwable cause) {
        super(cause);
    }
}
