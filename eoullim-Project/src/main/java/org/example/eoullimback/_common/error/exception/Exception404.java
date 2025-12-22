package org.example.eoullimback._common.error.exception;

import org.example.eoullimback._common.enums.errors.ErrorCode;

public class Exception404 extends RuntimeException {
    public Exception404(ErrorCode msg) {
        super(String.valueOf(msg));
    }

}
