package sn.optimizer.amigosFullStackCourse.exception;

import lombok.Getter;
import sn.optimizer.amigosFullStackCourse.customer.validator.ValidationResult;

import java.util.List;

@Getter
public class CustomerRegistrationException extends RuntimeException{

    private final List<ValidationResult> validationResults;
    private final ErrorCode errorCode;

    public CustomerRegistrationException(String message,
                                         List<ValidationResult> validationResults,
                                         ErrorCode errorCode){
        super(message);
        this.validationResults=validationResults;
        this.errorCode=errorCode;
    }
}
