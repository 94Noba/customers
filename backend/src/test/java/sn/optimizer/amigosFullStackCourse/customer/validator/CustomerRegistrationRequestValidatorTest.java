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
import static sn.optimizer.amigosFullStackCourse.customer.validator.CustomerRegistrationRequestValidator.*;

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
        isEmailValid(validationResults)
                .accept(request);

        assertThat(validationResults.isEmpty()).isTrue();
    }

    @Test
    void canUnValidateEmail(){
        when(request.email()).thenReturn("sidi.optimizer.com");

        isEmailValid(validationResults)
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

        isNameValid(validationResults)
                .accept(request);

        assertThat(validationResults.isEmpty()).isTrue();
    }

    @Test
    void canInvalidateName(){
        when(request.name()).thenReturn("");

        isNameValid(validationResults)
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

        isAgeValid(validationResults)
                .accept(request);

        assertThat(validationResults.isEmpty()).isTrue();
    }

    @Test
    void canInvalidateAge(){
        when(request.age()).thenReturn(null);

        isAgeValid(validationResults)
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
    void canValidatePassword(){
        when(request.password()).thenReturn("password12345");

        isPasswordValid(validationResults)
                .accept(request);

        assertThat(validationResults.isEmpty()).isTrue();
    }

    @Test
    void canInvalidatePassword(){
        when(request.password()).thenReturn("pa");

        isPasswordValid(validationResults)
                .accept(request);

        assertThat(validationResults)
                .usingRecursiveFieldByFieldElementComparator()
                .contains(new ValidationResult("Password", "The password is not valid"));
    }

    @Test
    void canProcessEntireCustomerRegisterRequestValidationIfAllFieldsAreValid() {
        when(request.name()).thenReturn("Sidi Ba");
        when(request.email()).thenReturn("sidi@optimizer.com");
        when(request.password()).thenReturn("password12345");
        when(request.age()).thenReturn(28);

        isEmailValid(validationResults)
                .and(isPasswordValid(validationResults))
                .and(isNameValid(validationResults))
                .and(isAgeValid(validationResults))
                .accept(request);

        assertThat(validationResults.isEmpty()).isTrue();
    }

    @Test
    void canProcessEntireCustomerRegisterRequestValidationIfAllFieldsAreInValid(){
        when(request.name()).thenReturn(null);
        when(request.email()).thenReturn(null);
        when(request.password()).thenReturn(null);
        when(request.age()).thenReturn(null);

        isEmailValid(validationResults)
                .and(isPasswordValid(validationResults))
                .and(isNameValid(validationResults))
                .and(isAgeValid(validationResults))
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
                    assertThat(vr.stream()
                            .anyMatch(r->r.getFieldName().equals("Password")&&
                                    r.getResult().equals("The password is not valid")))
                            .isTrue();
                });
    }
}