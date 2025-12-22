package org.example.eoullimback._common.error.exception;

import org.example.eoullimback._common.enums.errors.ErrorCode;

public class Exception403 extends RuntimeException {
    public Exception403(ErrorCode msg) {
        super(String.valueOf(msg));
    }

}
