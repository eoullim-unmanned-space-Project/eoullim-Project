package org.example.eoullimback._common.error;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.eoullimback._common.error.exception.*;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;
import java.io.PrintWriter;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception400.class)
    public void ex400(Exception400 e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        handleLog(e, request);
        script(response, e.getMessage(), "back");
    }

    @ExceptionHandler(Exception401.class)
    public void ex401(Exception401 e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        handleLog(e, request);
        script(response, e.getMessage(), "/auth/login");
    }

    @ExceptionHandler(Exception403.class)
    public void ex403(Exception403 e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        handleLog(e, request);
        script(response, e.getMessage(), "back");
    }

    @ExceptionHandler(Exception404.class)
    public void ex404(Exception404 e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        handleLog(e, request);
        script(response, e.getMessage(), "back");
    }

    @ExceptionHandler(Exception409.class)
    public void ex409(Exception409 e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        handleLog(e, request);
        script(response, e.getMessage(), "back");
    }

    @ExceptionHandler(Exception500.class)
    public String ex500(Exception500 e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        handleLog(e, request);
       script(response,e.getMessage(),"back");
        return "err/500";
    }

    private void handleLog(Exception e, HttpServletRequest request) {
        log.warn("요청 URL: {}", request.getRequestURL());
        log.warn("에러 메시지: {}", e.getMessage());
        log.warn("예외 클래스: {}", e.getClass().getSimpleName());

        request.setAttribute("msg", e.getMessage());
    }

    private void script(HttpServletResponse response, String message, String path) throws IOException {
        response.setContentType("text/html; charset=utf-8");
        PrintWriter out = response.getWriter();
        out.println("<script>");
        out.println("alert('"+ message + "')");
        if ("back".equals(path)) {
            out.println("history.back();");
        } else {
            out.println("location.href='" + path + "'");
        }
        out.println("</script>");
        out.flush();
        out.close();
    }
}
