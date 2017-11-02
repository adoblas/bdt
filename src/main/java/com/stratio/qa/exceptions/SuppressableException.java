package com.stratio.qa.exceptions;

public class SuppressableException extends Exception {

    private boolean suppressStacktrace = false;

    public SuppressableException(String message, boolean suppressStacktrace) {
        super(message, null, suppressStacktrace, !suppressStacktrace);
        this.suppressStacktrace = suppressStacktrace;
    }

    @Override
    public String toString() {
        if (suppressStacktrace) {
            return getMessage();
        } else {
            return super.toString();
        }
    }
}
