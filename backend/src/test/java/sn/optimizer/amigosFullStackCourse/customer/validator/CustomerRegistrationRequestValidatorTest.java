package sn.optimizer.amigosFullStackCourse.customer.validator;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import sn.optimizer.amigosFullStackCourse.customer.data.CustomerRegistrationRequest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class CustomerRegistrationRequestValidatorTest {

    private List<ValidationResult> validationResults;
    private static CustomerRegistrationRequest request;


    @BeforeAll
    static void setUp(){
        request=Mockito.mock(CustomerRegistrationRequest.class);
    }

    @BeforeEach()
    void init(){
        validationResults=new ArrayList<>();
    }


    @Test
    void canValidateEmail() {
        when(request.email()).thenReturn("sidi@optimizer.com");
        CustomerRegistrationRequestValidator.isEmailValid(validationResults)
                .accept(request);

        assertThat(validationResults.isEmpty()).isTrue();
    }

    @Test
    void canUnValidateEmail(){
        when(request.email()).thenReturn("sidi.optimizer.com");

        CustomerRegistrationRequestValidator.isEmailValid(validationResults)
                .accept(request);

        assertThat(validationResults.isEmpty()).isFalse();
        assertThat(validationResults)
                .satisfies(vr->{
                    assertThat(vr.stream()
                            .anyMatch(r->r.getFieldName().equals("Email")
                                    && r.getResult().equals("The email is not valid")))
                            .isTrue();
                });
    }

    @Test
    void canValidateName() {
        when(request.name()).thenReturn("Sidi Ba");

        CustomerRegistrationRequestValidator.isNameValid(validationResults)
                .accept(request);

        assertThat(validationResults.isEmpty()).isTrue();
    }

    @Test
    void canInvalidateName(){
        when(request.name()).thenReturn("");

        CustomerRegistrationRequestValidator.isNameValid(validationResults)
                .accept(request);

        assertThat(validationResults.isEmpty()).isFalse();
        assertThat(validationResults)
                .satisfies(vr->{
                    assertThat(vr.stream()
                            .anyMatch(r->r.getFieldName().equals("Name")
                                    &&r.getResult().equals("The name is not valid")))
                            .isTrue();
                });
    }

    @Test
    void canValidateAge() {
        when(request.age()).thenReturn(28);

        CustomerRegistrationRequestValidator.isAgeValid(validationResults)
                .accept(request);

        assertThat(validationResults.isEmpty()).isTrue();
    }

    @Test
    void canInvalidateAge(){
        when(request.age()).thenReturn(null);

        CustomerRegistrationRequestValidator.isAgeValid(validationResults)
                .accept(request);

        assertThat(validationResults.isEmpty()).isFalse();
        assertThat(validationResults)
                .satisfies(vr->{
                    assertThat(vr.stream()
                            .anyMatch(r->r.getFieldName().equals("Age")
                                    &&r.getResult().equals("The age is not valid")))
                            .isTrue();
                });
    }

    @Test
    void canProcessEntireCustomerRegisterRequestValidationIfAllFieldsAreValid() {
        when(request.name()).thenReturn("Sidi Ba");
        when(request.email()).thenReturn("sidi@optimizer.com");
        when(request.age()).thenReturn(28);

        CustomerRegistrationRequestValidator
                .isEmailValid(validationResults)
                .and(CustomerRegistrationRequestValidator.isNameValid(validationResults))
                .and(CustomerRegistrationRequestValidator.isAgeValid(validationResults))
                .accept(request);

        assertThat(validationResults.isEmpty()).isTrue();
    }

    @Test
    void canProcessEntireCustomerRegisterRequestValidationIfAllFieldsAreInValid(){
        when(request.name()).thenReturn(null);
        when(request.email()).thenReturn(null);
        when(request.age()).thenReturn(null);

        CustomerRegistrationRequestValidator
                .isEmailValid(validationResults)
                .and(CustomerRegistrationRequestValidator.isNameValid(validationResults))
                .and(CustomerRegistrationRequestValidator.isAgeValid(validationResults))
                .accept(request);

        assertThat(validationResults.isEmpty()).isFalse();
        assertThat(validationResults)
                .satisfies(vr->{
                    assertThat(vr.stream()
                            .anyMatch(r->r.getFieldName().equals("Email")&&
                                    r.getResult().equals("The email is not valid")))
                            .isTrue();
                    assertThat(vr.stream()
                            .anyMatch(r->r.getFieldName().equals("Name")&&
                                    r.getResult().equals("The name is not valid")))
                            .isTrue();
                    assertThat(vr.stream()
                            .anyMatch(r->r.getFieldName().equals("Age")&&
                                    r.getResult().equals("The age is not valid")))
                            .isTrue();
                });
    }
}