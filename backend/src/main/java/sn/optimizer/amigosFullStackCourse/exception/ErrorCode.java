package sn.optimizer.amigosFullStackCourse.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    CUSTOMER_NOT_FOUND(1404),
    EMAIL_ALREADY_TAKEN(1409),
    NOT_SUPPORTED_OPERATION(1409),
    REGISTRATION_REQUEST_NOT_VALID(1400),
    UPDATE_OPERATION_FAILED(1400);

    private final int code;

    ErrorCode(int code){
        this.code=code;
    }

}
