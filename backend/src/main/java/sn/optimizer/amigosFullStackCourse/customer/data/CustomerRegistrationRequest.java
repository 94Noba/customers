package sn.optimizer.amigosFullStackCourse.customer.data;

import sn.optimizer.amigosFullStackCourse.customer.Customer;

public record CustomerRegistrationRequest(String name, String email,
                                          String password,String role, Integer age) {

    public static Customer customerRegistrationRequestToCustomer(CustomerRegistrationRequest customerRegistrationRequest){
        return Customer.builder()
                .email(customerRegistrationRequest.email)
                .name(customerRegistrationRequest.name)
                .age(customerRegistrationRequest.age)
                .active(true)
                .build();
    }
}
