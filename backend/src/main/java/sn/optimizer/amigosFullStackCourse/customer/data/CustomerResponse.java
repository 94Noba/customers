package sn.optimizer.amigosFullStackCourse.customer.data;

import sn.optimizer.amigosFullStackCourse.customer.Customer;

public record CustomerResponse(String name, String email, int age){

    public static CustomerResponse customerToCustomerResponse(Customer customer){
        return new CustomerResponse(customer.getName(),
                customer.getEmail(), customer.getAge());
    }
}
