package sn.optimizer.amigosFullStackCourse.customer;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.optimizer.amigosFullStackCourse.customer.data.CustomerRegistrationRequest;
import sn.optimizer.amigosFullStackCourse.customer.data.CustomerResponse;
import sn.optimizer.amigosFullStackCourse.customer.data.CustomerUpdateRequest;
import sn.optimizer.amigosFullStackCourse.customer.repository.CustomerJpaRepository;
import sn.optimizer.amigosFullStackCourse.customer.service.CustomerService;
import sn.optimizer.amigosFullStackCourse.customer.utilities.CustomerUpdater;
import sn.optimizer.amigosFullStackCourse.customer.utilities.RoleSupplier;
import sn.optimizer.amigosFullStackCourse.customer.utilities.roleSuppliers.RoleSupplierFactory;
import sn.optimizer.amigosFullStackCourse.customer.utilities.updaterImpls.CustomerPasswordUpdater;
import sn.optimizer.amigosFullStackCourse.customer.utilities.updaterImpls.CustomerUpdaterFactory;
import sn.optimizer.amigosFullStackCourse.customer.validator.ValidationResult;
import sn.optimizer.amigosFullStackCourse.exception.ApplicationException;
import sn.optimizer.amigosFullStackCourse.exception.CustomerRegistrationException;
import sn.optimizer.amigosFullStackCourse.exception.ErrorCode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static sn.optimizer.amigosFullStackCourse.customer.validator.CustomerRegistrationRequestValidator.*;

@Service
public  class CustomerServiceImpl implements CustomerService {

    private final CustomerJpaRepository customerJpaRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomerServiceImpl(CustomerJpaRepository customerJpaRepository, PasswordEncoder passwordEncoder){
        this.customerJpaRepository=customerJpaRepository;
        this.passwordEncoder=passwordEncoder;
    }
    @Override
    public List<CustomerResponse> getAllCustomers() {

        return customerJpaRepository.findAll()
                .stream()
                .map(CustomerResponse::customerToCustomerResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Customer getCustomerById(Long customerId) {
        return customerJpaRepository.findById(customerId)
                .orElseThrow(()->new ApplicationException("Customer ["+customerId+"] not found", ErrorCode.CUSTOMER_NOT_FOUND));
    }

    @Override
    public boolean isEmailTaken(String email) {
        return customerJpaRepository.existsByEmail(email);
    }

    @Override
    @Transactional
    public void registerCustomer(CustomerRegistrationRequest customerRegistrationRequest) {
        this.validateCustomerRegistrationRequest(customerRegistrationRequest);
        RoleSupplier roleSupplier=RoleSupplierFactory.of(customerRegistrationRequest.role());
        if(roleSupplier==null)
            throw new ApplicationException("operation not supported", ErrorCode.NOT_SUPPORTED_OPERATION);
        String email=customerRegistrationRequest.email();
        if(this.isEmailTaken(email))
            throw new ApplicationException("Email ["+email+"] already taken", ErrorCode.EMAIL_ALREADY_TAKEN);

        Customer customer=CustomerRegistrationRequest
                .customerRegistrationRequestToCustomer(customerRegistrationRequest);
        customer.setPassword(passwordEncoder.encode(customerRegistrationRequest.password()));
        customer.setRole(roleSupplier.get());
        customerJpaRepository.save(customer);
    }

    private void validateCustomerRegistrationRequest(CustomerRegistrationRequest customerRegistrationRequest){
        List<ValidationResult> results= new ArrayList<>();
        isEmailValid(results)
                .and(isNameValid(results))
                .and(isPasswordValid(results))
                .and(isAgeValid(results))
                .accept(customerRegistrationRequest);
        if (!results.isEmpty())
            throw new CustomerRegistrationException("Registration validation failed", results,
                    ErrorCode.REGISTRATION_REQUEST_NOT_VALID);
    }

    @Override
    @Transactional
    public void deleteCustomer(String email) {
        customerJpaRepository.delete(this.getCustomerByEmail(email));
    }

    @Override
    public Customer getCustomerByEmail(String email) {
        return customerJpaRepository.findByEmail(email)
                .orElseThrow(()->new ApplicationException("Customer ["+email+"] not found",
                        ErrorCode.CUSTOMER_NOT_FOUND));

    }

    @Override
    public CustomerResponse getCustomerResponseByEmail(String email) {
        Customer customer=customerJpaRepository.findByEmail(email)
                .orElseThrow(()->new ApplicationException("Customer ["+email+"] not found",
                        ErrorCode.CUSTOMER_NOT_FOUND));
        return CustomerResponse.customerToCustomerResponse(customer);
    }

    @Override
    @Transactional
    public void updateCustomer(CustomerUpdateRequest updateRequest) {
        CustomerUpdater updater= CustomerUpdaterFactory.of(updateRequest.type());
        Object patch=updateRequest.patch();
        if(updater==null)
            throw new ApplicationException("Operation not supported", ErrorCode.NOT_SUPPORTED_OPERATION);

        Customer customer=this.getCustomerByEmail(updateRequest.email());
        if(updater instanceof CustomerPasswordUpdater)
            patch=passwordEncoder.encode((String)patch);

        int result=updater.updateCustomer(customer, patch);
        if(result==-1)
            throw new ApplicationException("Update operation failed because ["
                    +updateRequest.type()+"] is not valid", ErrorCode.UPDATE_OPERATION_FAILED);

    }
}