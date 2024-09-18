package com.spring.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.view.RedirectView;

@ControllerAdvice
public class GlobalException {
    @ExceptionHandler(Exception.class)
    public RedirectView exception(Exception e, HttpServletRequest request) {
        return new RedirectView("/");
    }
}
