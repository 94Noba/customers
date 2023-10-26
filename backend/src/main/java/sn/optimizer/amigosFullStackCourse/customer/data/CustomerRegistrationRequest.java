package sn.optimizer.amigosFullStackCourse.customer.data;

import sn.optimizer.amigosFullStackCourse.customer.Customer;

public record CustomerRegistrationRequest(String name, String email, String password, Integer age) {

    public static Customer customerRegistrationRequestToCustomer(CustomerRegistrationRequest customerRegistrationRequest){
        return Customer.builder()
                .email(customerRegistrationRequest.email)
                .password(customerRegistrationRequest.password)
                .name(customerRegistrationRequest.name)
                .age(customerRegistrationRequest.age)
                .active(true)
                .build();
    }
}
