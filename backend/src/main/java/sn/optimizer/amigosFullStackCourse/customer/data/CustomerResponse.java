package sn.optimizer.amigosFullStackCourse.customer.data;

import sn.optimizer.amigosFullStackCourse.customer.Customer;
import sn.optimizer.amigosFullStackCourse.customer.security.permission.Role;

public record CustomerResponse(String name, String email,
                               Role role, int age){

    public static CustomerResponse customerToCustomerResponse(Customer customer){
        return new CustomerResponse(customer.getName(),
                customer.getEmail(), customer.getRole(), customer.getAge());
    }
}
