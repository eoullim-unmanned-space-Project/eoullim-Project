package org.example.eoullimback._common.error.exception;

import org.example.eoullimback._common.enums.errors.ErrorCode;

public class Exception409 extends RuntimeException {
    public Exception409(ErrorCode message) {
        super(String.valueOf(message));
    }
}
