package com.littlepay.util;

public final class Constants {

    private Constants() {
        throw new AssertionError("Constants class should not be instantiated.");
    }


    public enum TRIP_STATUS {
        INCOMPLETE,
        CANCELLED,
        COMPLETED
    }

    public enum TAP_TYPE {
        ON,
        OFF
    }
}
