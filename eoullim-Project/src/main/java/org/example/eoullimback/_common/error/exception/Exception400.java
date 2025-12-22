package org.example.eoullimback._common.error.exception;

import org.example.eoullimback._common.enums.errors.ErrorCode;

public class Exception400 extends RuntimeException {
    public Exception400(ErrorCode msg) {
        super(String.valueOf(msg));
    }

}
