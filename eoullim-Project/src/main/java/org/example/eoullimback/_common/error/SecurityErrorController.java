package org.example.eoullimback._common.error;

import org.example.eoullimback._common.enums.errors.ErrorCode;
import org.example.eoullimback._common.error.exception.Exception400;
import org.example.eoullimback._common.error.exception.Exception401;
import org.example.eoullimback._common.error.exception.Exception403;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SecurityErrorController {

    @GetMapping("/error-direct/403")
    public void error403() {
        throw new Exception403(ErrorCode.ACCESS_DENIED);
    }

    @GetMapping("/error-direct/401")
    public void error401() {
        throw new Exception401(ErrorCode.LOGIN_UNAUTHORIZED);
    }

    @GetMapping("/error-direct/account-locked")
    public void errorAccountLocked() {
        throw new Exception401(ErrorCode.USER_STATUS_SUSPENDED);
    }

    @GetMapping("/error-direct/account-disabled")
    public void errorAccountDisabled() {
        throw new Exception401(ErrorCode.USER_STATUS_WITHDRAWN);
    }


}
