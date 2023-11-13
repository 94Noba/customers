package sn.optimizer.amigosFullStackCourse.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;



@RestControllerAdvice
public class ApplicationExceptionHandler {
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ApplicationException.class)
    public ApplicationExceptionPayload handle(ApplicationException e, HttpServletRequest request){
        return new ApplicationExceptionPayload(e.getMessage(), e.getErrorCode(),
                e.getErrorCode().getCode(), LocalDateTime.now(),
                request.getRequestURI());

    }
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(CustomerRegistrationException.class)
    public CustomerRegistrationExceptionPayload handle(CustomerRegistrationException e, HttpServletRequest request){
        return new CustomerRegistrationExceptionPayload(e.getMessage(),e.getErrorCode(),
                e.getErrorCode().getCode(),
                e.getValidationResults(), LocalDateTime.now(),
                request.getRequestURI());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public ApplicationExceptionPayload handle(AccessDeniedException e, HttpServletRequest request){
        String message=e.getMessage()+":You dont have the required authorizations";
        return new ApplicationExceptionPayload(message, ErrorCode.ACCESS_DENIED,
                ErrorCode.ACCESS_DENIED.getCode(), LocalDateTime.now(), request.getRequestURI());
    }

    @ExceptionHandler(InsufficientAuthenticationException.class)
    public ApplicationExceptionPayload handle(InsufficientAuthenticationException e, HttpServletRequest request,
                         HttpServletResponse response){
        if(response.getStatus()==200){
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return new ApplicationExceptionPayload(e.getMessage(), ErrorCode.AUTHENTICATION_FAILED,
                    ErrorCode.UPDATE_OPERATION_FAILED.getCode(), LocalDateTime.now(), request.getRequestURI());
        }
        return new ApplicationExceptionPayload("Path not found", ErrorCode.PATH_NOT_FOUND,
                    ErrorCode.PATH_NOT_FOUND.getCode(), LocalDateTime.now(), "");
    }
}