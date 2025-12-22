package org.example.eoullimback._common.error.exception;

import org.example.eoullimback._common.enums.errors.ErrorCode;

public class Exception401 extends RuntimeException {
    public Exception401(ErrorCode msg) {
        super(String.valueOf(msg));
    }

}
