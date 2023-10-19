package sn.optimizer.amigosFullStackCourse.customer.validator;

import lombok.Getter;

@Getter
public class ValidationResult {

    private final String fieldName;
    private final String result;

    public ValidationResult(String fieldName, String result){
        this.fieldName=fieldName;
        this.result=result;
    }

}
