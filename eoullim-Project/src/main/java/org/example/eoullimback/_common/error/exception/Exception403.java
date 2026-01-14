package org.example.eoullimback._common.error.exception;

import org.example.eoullimback._common.enums.errors.ErrorCode;

public class Exception403 extends RuntimeException {
    private final String reason;

    public Exception403(ErrorCode msg) {
        super(String.valueOf(msg.toString()));
        this.reason = null;
    }

    public Exception403(ErrorCode msg, String reason) {
        super(msg.toString());
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

}
