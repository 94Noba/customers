package sn.optimizer.amigosFullStackCourse.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;


@RestControllerAdvice
public class ApplicationExceptionHandler {
    @ExceptionHandler(ApplicationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApplicationExceptionPayload handle(ApplicationException e, HttpServletRequest request){
        return new ApplicationExceptionPayload(e.getMessage(), e.getErrorCode(),
                e.getErrorCode().getCode(), LocalDateTime.now(),
                request.getRequestURI());
    }

    @ExceptionHandler(CustomerRegistrationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public CustomerRegistrationExceptionPayload handle(CustomerRegistrationException e, HttpServletRequest request){
        return new CustomerRegistrationExceptionPayload(e.getMessage(),e.getErrorCode(), e.getErrorCode().getCode(),
                e.getValidationResults(), LocalDateTime.now(),
                request.getRequestURI());
    }
}