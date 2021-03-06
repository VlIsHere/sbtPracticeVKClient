package com.sbt.exceptions;

public class IdFormatException extends Exception {
    /**
     * Returns the detail message string of this throwable.
     *
     * @return the detail message string of this {@code Throwable} instance
     * (which may be {@code null}).
     */
    @Override
    public String getMessage() {
        return "Wrong ID!"+super.getMessage() ;
    }
}
