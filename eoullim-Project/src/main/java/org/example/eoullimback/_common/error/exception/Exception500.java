package org.example.eoullimback._common.error.exception;

import org.example.eoullimback._common.enums.errors.ErrorCode;

public class Exception500 extends RuntimeException {
    public Exception500(ErrorCode msg) {
        super(String.valueOf(msg));
    }

}
