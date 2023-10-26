package sn.optimizer.amigosFullStackCourse.customer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import sn.optimizer.amigosFullStackCourse.customer.data.CustomerRegistrationRequest;
import sn.optimizer.amigosFullStackCourse.customer.data.CustomerUpdateRequest;
import sn.optimizer.amigosFullStackCourse.customer.repository.CustomerJpaRepository;
import sn.optimizer.amigosFullStackCourse.customer.validator.ValidationResult;
import sn.optimizer.amigosFullStackCourse.exception.ApplicationException;
import sn.optimizer.amigosFullStackCourse.exception.CustomerRegistrationException;
import sn.optimizer.amigosFullStackCourse.exception.ErrorCode;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerJpaRepository customerJpaRepository;

    @InjectMocks
    private CustomerServiceImpl underTest;


    @Test
    void getAllCustomers() {
        underTest.getAllCustomers();
        verify(customerJpaRepository).findAll();
    }

    @Test
    void getCustomerById() {
        Long id=10L;
        Customer customer=Customer.builder()
                    .id(id)
                    .email("email")
                    .password("password12345")
                    .name("name")
                    .age(20)
                    .build();

        when(customerJpaRepository.findById(id)).thenReturn(Optional.of(customer));

        Customer expected=underTest.getCustomerById(id);
        verify(customerJpaRepository).findById(id);
        assertThat(expected)
                .satisfies(c->{
                    assertThat(c.getAge().equals(customer.getAge())).isTrue();
                    assertThat(c.getName().equals(customer.getName())).isTrue();
                    assertThat(c.getEmail().equals(customer.getEmail())).isTrue();
                    assertThat(c.getPassword().equals(customer.getPassword())).isTrue();
                });
    }

    @Test
    void getCustomerByIdWillThrowApplicationExceptionIfCustomerNotExist(){
        Long id=10L;

        when(customerJpaRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(()->underTest.getCustomerById(id))
               .isExactlyInstanceOf(ApplicationException.class)
               .hasMessage("Customer ["+10L+"] not found")
               .satisfies(e->{
                   assertThat((ApplicationException)e)
                           .satisfies(ae->{
                               assertThat(ae.getErrorCode().equals(ErrorCode.CUSTOMER_NOT_FOUND)).isTrue();
                           });
               });
        verify(customerJpaRepository).findById(id);
    }

    @Test
    void isEmailTaken() {
        when(customerJpaRepository.existsByEmail("email")).thenReturn(true);

        assertThat(underTest.isEmailTaken("email")).isTrue();
        verify(customerJpaRepository).existsByEmail("email");
    }

    @Test
    void canIsEmailTakenReturnFalseIfEmailIsNotTaken(){
        when(customerJpaRepository.existsByEmail("email")).thenReturn(false);

        assertThat(underTest.isEmailTaken("email")).isFalse();
        verify(customerJpaRepository).existsByEmail("email");
    }

    @Test
    void canRegisterCustomer() {
        CustomerRegistrationRequest request=new CustomerRegistrationRequest("Sidi Ba",
                "sidi@optimizer.com", "password12345", 28);

        ArgumentCaptor<Customer>customerArgumentCaptor=ArgumentCaptor.forClass(Customer.class);
        underTest.registerCustomer(request);

        verify(customerJpaRepository).save(customerArgumentCaptor.capture());
        Customer expected=customerArgumentCaptor.getValue();

        assertThat(expected)
                .satisfies(c->{
                    assertThat(c.getAge().equals(request.age())).isTrue();
                    assertThat(c.getName().equals(request.name())).isTrue();
                    assertThat(c.getEmail().equals(request.email())).isTrue();
                    assertThat(c.getPassword().equals(request.password())).isTrue();
                });
    }

    @Test
    void canRegisterCustomerFailIfCustomerAlreadyExist(){
        String email="sidi@optimizer.com";
        CustomerRegistrationRequest request=new CustomerRegistrationRequest("Sidi Ba",
                email, "password12345", 28);

        when(customerJpaRepository.existsByEmail(email))
                .thenReturn(true);

        assertThatThrownBy(()->underTest.registerCustomer(request))
                .isExactlyInstanceOf(ApplicationException.class)
                .hasMessage("Email ["+email+"] already taken")
                .satisfies(e->{
                    assertThat((ApplicationException)e)
                            .satisfies(ae->{
                                assertThat(ae.getErrorCode().equals(ErrorCode.EMAIL_ALREADY_TAKEN)).isTrue();
                            });
                });
        verify(customerJpaRepository, never()).save(CustomerRegistrationRequest
                .customerRegistrationRequestToCustomer(request));
    }

    @Test
    void registerCustomerWillFailIfNameIsNotValid(){
        CustomerRegistrationRequest request=new CustomerRegistrationRequest("",
                "sidi@optimizer.com", "password12345", 28);

        assertThatThrownBy(()->underTest.registerCustomer(request))
                .isExactlyInstanceOf(CustomerRegistrationException.class);


        verify(customerJpaRepository, never()).save(CustomerRegistrationRequest
                .customerRegistrationRequestToCustomer(request));

    }

    @Test
    void registerCustomerWillFailIfEmailIsNotValid(){
        CustomerRegistrationRequest request=new CustomerRegistrationRequest("Sidi Ba",
                "sidioptimizer.com", "password1234", 28);

        assertThatThrownBy(()->underTest.registerCustomer(request))
                .isExactlyInstanceOf(CustomerRegistrationException.class);

        verify(customerJpaRepository, never()).save(CustomerRegistrationRequest
                .customerRegistrationRequestToCustomer(request));

    }

    @Test
    void registerCustomerWillFailIfPasswordIsNotValid(){
        CustomerRegistrationRequest request=new CustomerRegistrationRequest("Sidi Ba",
                "sidi@optimizer.com", "pa", 28);

        assertThatThrownBy(()->underTest.registerCustomer(request))
                .isExactlyInstanceOf(CustomerRegistrationException.class);

        verify(customerJpaRepository, never()).save(any(Customer.class));
    }

    @Test
    void registerCustomerWillFailIfAgeIsNotValid(){
        CustomerRegistrationRequest request=new CustomerRegistrationRequest("Sidi Ba",
                "sidi@optimizer.com", "password12345", null);

        assertThatThrownBy(()->underTest.registerCustomer(request))
                .isExactlyInstanceOf(CustomerRegistrationException.class);

        verify(customerJpaRepository, never()).save(CustomerRegistrationRequest
                .customerRegistrationRequestToCustomer(request));
    }

    @Test
    void canThrowCustomerRegistrationExceptionIfCustomerRegistrationRequestNameIsInvalid(){
        CustomerRegistrationRequest request=new CustomerRegistrationRequest("",
                "email", "password12345", 29);

        assertThatThrownBy(()->underTest.registerCustomer(request))
                .isExactlyInstanceOf(CustomerRegistrationException.class)
                .hasMessage("Registration validation failed")
                .satisfies(e->{
                    assertThat((CustomerRegistrationException)e).satisfies(ce->{
                        assertThat(ce.getErrorCode().equals(ErrorCode.REGISTRATION_REQUEST_NOT_VALID)).isTrue();
                        assertThat(ce.getValidationResults())
                                .usingRecursiveFieldByFieldElementComparator()
                                .contains(new ValidationResult("Name", "The name is not valid"));
                    });
                });
    }

    @Test
    void canThrowCustomerRegistrationExceptionIfCustomerRegistrationRequestEmailIsInvalid(){
        CustomerRegistrationRequest request=new CustomerRegistrationRequest("Sidi Ba",
                "email.sidi.com", "password12345", 29);

        assertThatThrownBy(()->underTest.registerCustomer(request))
                .isExactlyInstanceOf(CustomerRegistrationException.class)
                .hasMessage("Registration validation failed")
                .satisfies(e->{
                    assertThat((CustomerRegistrationException)e).satisfies(re->{
                        assertThat(re.getErrorCode().equals(ErrorCode.REGISTRATION_REQUEST_NOT_VALID)).isTrue();
                        assertThat(re.getValidationResults())
                                .usingRecursiveFieldByFieldElementComparator()
                                .contains(new ValidationResult("Email", "The email is not valid"));
                    });
                });
    }

    @Test
    void canThrowCustomerRegistrationExceptionIfCustomerRegistrationRequestAgeIsInvalid(){
        CustomerRegistrationRequest request=new CustomerRegistrationRequest("Sidi Ba",
                "sidi@optimizer.com", "password12345", null);

        assertThatThrownBy(()->underTest.registerCustomer(request))
                .isExactlyInstanceOf(CustomerRegistrationException.class)
                .hasMessage("Registration validation failed")
                .satisfies(e->{
                    assertThat((CustomerRegistrationException)e)
                            .satisfies(re->{
                                assertThat(re.getErrorCode().equals(ErrorCode.REGISTRATION_REQUEST_NOT_VALID)).isTrue();
                                assertThat(re.getValidationResults()
                                        .stream()
                                        .allMatch(vr->vr.getFieldName().equals("Age")&&
                                                vr.getResult().equals("The age is not valid"))).isTrue();
                            });
                });
    }

    @Test
    void canThrowCustomerRegistrationExceptionIfCustomerRegistrationRequestPasswordIsNotValid(){
        CustomerRegistrationRequest request=new CustomerRegistrationRequest("Sidi Ba",
                "sidi@optimizer.com", "", 28);

        assertThatThrownBy(()->underTest.registerCustomer(request))
                .isExactlyInstanceOf(CustomerRegistrationException.class)
                .hasMessage("Registration validation failed")
                .satisfies(e->{
                    assertThat((CustomerRegistrationException)e)
                            .satisfies(re->{
                                assertThat(re.getErrorCode().equals(ErrorCode.REGISTRATION_REQUEST_NOT_VALID)).isTrue();
                                assertThat(re.getValidationResults()
                                        .stream()
                                        .allMatch(vr->vr.getFieldName().equals("Password")&&
                                                vr.getResult().equals("The password is not valid"))).isTrue();
                            });
                });
    }

    @Test
    void canDeleteCustomer(){
        String email="sidi@optimizer.com";
        Customer customer=new Customer("Sidi Ba", email, "password", 28, true);

        when(customerJpaRepository.findByEmail(email)).thenReturn(Optional.of(customer));

        underTest.deleteCustomer(email);
        verify(customerJpaRepository).delete(customer);
    }

    @Test
    void canDeleteCustomerThrowApplicationExceptionIfCustomerNotExist(){
        String email="sidi@optimizer.com";

        when(customerJpaRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThatThrownBy(()->underTest.deleteCustomer(email))
                .isExactlyInstanceOf(ApplicationException.class)
                .hasMessage("Customer ["+email+"] not found")
                .satisfies(e->{
                    assertThat((ApplicationException)e)
                            .satisfies(ae->{
                                assertThat(ae.getErrorCode().equals(ErrorCode.CUSTOMER_NOT_FOUND)).isTrue();
                            });
                });
        verify(customerJpaRepository, never()).delete(any());
    }

    @Test
    void canGetCustomerByEmail() {
        String email="sidi@optimizer.com";
        Customer customer=new Customer("Sidi Ba", email, "password", 28, true);

        when(customerJpaRepository.findByEmail(email))
                .thenReturn(Optional.of(customer));

        Customer expected=underTest.getCustomerByEmail(email);

        verify(customerJpaRepository).findByEmail(email);
        assertThat(expected).usingRecursiveComparison()
                .comparingOnlyFields("name", "email", "password", "age", "active")
                .isEqualTo(customer);
    }

    @Test
    void canGetCustomerByEmailThrowApplicationExceptionIfEmailNotExist(){
        String email="sidi@optimizer.com";

        when(customerJpaRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThatThrownBy(()->underTest.getCustomerByEmail(email))
                .isExactlyInstanceOf(ApplicationException.class)
                .hasMessage("Customer ["+email+"] not found")
                .satisfies(e->{
                    assertThat((ApplicationException)e)
                            .satisfies(ae->{
                                assertThat(ae.getErrorCode().equals(ErrorCode.CUSTOMER_NOT_FOUND)).isTrue();
                            });
                });
    }

    @Test
    void canThrowApplicationExceptionNotSupportedOperationIfUpdaterIsNull(){
        CustomerUpdateRequest request=Mockito.mock(CustomerUpdateRequest.class);

        when(request.type()).thenReturn("something-wrong");

        assertThatThrownBy(()->underTest.updateCustomer(request))
                .isExactlyInstanceOf(ApplicationException.class)
                .hasMessage("Operation not supported")
                .satisfies(e->{
                    assertThat((ApplicationException)e)
                            .satisfies(ae->{
                                assertThat(ae.getErrorCode().equals(ErrorCode.NOT_SUPPORTED_OPERATION)).isTrue();
                            });
                });
    }

    @Test
    void canUpdateEmailCustomer(){
        String email="sidi@gmail.com";
        String updateEmail="sidi@optimizer.com";
        Customer customer=new Customer("Sidi Ba", email, "password", 28, true );

        CustomerUpdateRequest updateRequest=Mockito.mock(CustomerUpdateRequest.class);

        when(updateRequest.email()).thenReturn(email);
        when(updateRequest.type()).thenReturn("email");
        when(updateRequest.patch()).thenReturn(updateEmail);
        when(customerJpaRepository.findByEmail(email)).thenReturn(Optional.of(customer));

        underTest.updateCustomer(updateRequest);

        assertThat(customer.getEmail().equals(updateEmail)).isTrue();
    }

    @Test
    void canUpdateEmailCustomerFailIfEmailIsNotValid(){
        CustomerUpdateRequest request=Mockito.mock(CustomerUpdateRequest.class);
        String email="sidi@Optimizer.com";
        Customer customer=new Customer("Sidi Ba", email, "password", 28, true);

        when(customerJpaRepository.findByEmail(email)).thenReturn(Optional.of(customer));
        when(request.patch()).thenReturn("something-wrong");
        when(request.type()).thenReturn("email");
        when(request.email()).thenReturn(email);

        assertThatThrownBy(()->underTest.updateCustomer(request)).isExactlyInstanceOf(ApplicationException.class)
                .hasMessage("Update operation failed because [email] is not valid")
                .satisfies(e->{
                    assertThat((ApplicationException)e)
                            .satisfies(ae->{
                                assertThat(ae.getErrorCode().equals(ErrorCode.UPDATE_OPERATION_FAILED))
                                        .isTrue();
                            });
                });
        assertThat(!customer.getEmail().equals("something-wrong")).isTrue();
    }

    @Test
    void canUpdateCustomerAge(){
        String email="sidi@gmail.com";
        int updateAge=29;
        Customer customer=new Customer("Sidi Ba", email, "password", 20, true);

        CustomerUpdateRequest updateRequest=Mockito.mock(CustomerUpdateRequest.class);

        when(updateRequest.email()).thenReturn(email);
        when(updateRequest.type()).thenReturn("age");
        when(updateRequest.patch()).thenReturn(updateAge);
        when(customerJpaRepository.findByEmail(email)).thenReturn(Optional.of(customer));

        underTest.updateCustomer(updateRequest);

        assertThat(customer.getAge().equals(updateAge)).isTrue();
    }

    @Test
    void canUpdateCustomerAgeFailIfAgeIsNotValid(){
        CustomerUpdateRequest request=Mockito.mock(CustomerUpdateRequest.class);
        String email="sidi@Optimizer.com";
        int age=4;
        Customer customer=new Customer("Sidi Ba", email, "password", 28, true);

        when(customerJpaRepository.findByEmail(email)).thenReturn(Optional.of(customer));
        when(request.patch()).thenReturn(age);
        when(request.type()).thenReturn("age");
        when(request.email()).thenReturn(email);

        assertThatThrownBy(()->underTest.updateCustomer(request)).isExactlyInstanceOf(ApplicationException.class)
                .hasMessage("Update operation failed because [age] is not valid")
                .satisfies(e->{
                    assertThat((ApplicationException)e)
                            .satisfies(ae->{
                                assertThat(ae.getErrorCode().equals(ErrorCode.UPDATE_OPERATION_FAILED))
                                        .isTrue();
                            });
                });
        assertThat(customer.getAge()!=age).isTrue();
    }
}