package org.example.eoullimback._common.error;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.example.eoullimback._common.error.exception.*;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class MyExceptionHandler {

    @ExceptionHandler(Exception400.class)
    public String ex400(Exception400 e, HttpServletRequest request) {
        log.warn("== 400 에러 발생 ==");
        handleLog(e, request);

        return "err/400";
    }

    @ExceptionHandler(Exception401.class)
    public String ex401(Exception401 e, HttpServletRequest request) {
        log.warn("== 401 에러 발생 ==");
        handleLog(e, request);

        return "err/401";
    }

    @ExceptionHandler(Exception403.class)
    public String ex403(Exception403 e, HttpServletRequest request) {
        log.warn("== 403 에러 발생 ==");
        handleLog(e, request);

        return "err/403";
    }

    @ExceptionHandler(Exception404.class)
    public String ex404(Exception404 e, HttpServletRequest request) {
        log.warn("== 404 에러 발생 ==");
        handleLog(e, request);

        return "err/404";
    }

    @ExceptionHandler(Exception500.class)
    public String ex500(Exception500 e, HttpServletRequest request) {
        log.warn("== 500 에러 발생 ==");
        handleLog(e, request);

        return "err/500";
    }

    private void handleLog(Exception e, HttpServletRequest request) {
        log.warn("요청 URL: {}", request.getRequestURL());
        log.warn("에러 메시지: {}", e.getMessage());
        log.warn("예외 클래스: {}", e.getClass().getSimpleName());

        request.setAttribute("msg", e.getMessage());
    }
}
