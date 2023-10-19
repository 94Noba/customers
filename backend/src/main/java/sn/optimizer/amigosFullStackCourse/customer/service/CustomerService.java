package sn.optimizer.amigosFullStackCourse.customer.service;

import sn.optimizer.amigosFullStackCourse.customer.Customer;
import sn.optimizer.amigosFullStackCourse.customer.data.CustomerRegistrationRequest;
import sn.optimizer.amigosFullStackCourse.customer.data.CustomerResponse;
import sn.optimizer.amigosFullStackCourse.customer.data.CustomerUpdateRequest;

import java.util.List;

public interface CustomerService {

    List<CustomerResponse> getAllCustomers();

    Customer getCustomerById(Long customerId);

    boolean isEmailTaken(String email);

    void registerCustomer(CustomerRegistrationRequest customerRegistrationRequest);

    void deleteCustomer(String email);

    Customer getCustomerByEmail(String email);

    CustomerResponse getCustomerResponseByEmail(String email);

    void updateCustomer(CustomerUpdateRequest updateRequest);

}
