package sn.optimizer.amigosFullStackCourse.customer.validator;

import sn.optimizer.amigosFullStackCourse.customer.data.CustomerRegistrationRequest;

import java.util.List;
import java.util.function.Consumer;


public interface CustomerRegistrationRequestValidator extends Consumer<CustomerRegistrationRequest> {

    static CustomerRegistrationRequestValidator isEmailValid(List<ValidationResult> results){
        return customerRegistrationRequest->{
            if(customerRegistrationRequest.email()==null
                    || customerRegistrationRequest.email().isBlank()
                    ||!EmailValidator.validate(customerRegistrationRequest.email()))
                results.add(new ValidationResult("Email", "The email is not valid"));
        };
    }

    static CustomerRegistrationRequestValidator isNameValid(List<ValidationResult> results) {
        return customerRegistrationRequest -> {
            if (customerRegistrationRequest.name() == null
                || customerRegistrationRequest.name().isBlank())
                results.add(new ValidationResult("Name", "The name is not valid"));
        };

    }

    static CustomerRegistrationRequestValidator isAgeValid(List<ValidationResult> results){
        return customerRegistrationRequest -> {
            if(customerRegistrationRequest.age()==null
                    || customerRegistrationRequest.age()<10)
                results.add(new ValidationResult("Age", "The age is not valid"));
        };
    }

    default CustomerRegistrationRequestValidator and(CustomerRegistrationRequestValidator next){
        return  customerRegistrationRequest -> {
            this.accept(customerRegistrationRequest);
            next.accept(customerRegistrationRequest);
        };
    }
}