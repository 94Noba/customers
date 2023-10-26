package sn.optimizer.amigosFullStackCourse.customer;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import sn.optimizer.amigosFullStackCourse.customer.data.CustomerDeletionRequest;
import sn.optimizer.amigosFullStackCourse.customer.data.CustomerRegistrationRequest;
import sn.optimizer.amigosFullStackCourse.customer.data.CustomerResponse;
import sn.optimizer.amigosFullStackCourse.customer.data.CustomerUpdateRequest;
import sn.optimizer.amigosFullStackCourse.exception.ApplicationExceptionPayload;
import sn.optimizer.amigosFullStackCourse.exception.ErrorCode;

import java.util.List;
import java.util.Random;


import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CustomerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void canRegisterCustomer(){
        Faker faker=new Faker();
        Random random=new Random();
        Name fakeName=faker.name();
        String name= fakeName.fullName().strip();
        String email=fakeName.lastName().strip()+random.nextInt(0, 500)+"@optimizer.com";
        int age=random.nextInt(15, 60);

        CustomerRegistrationRequest request=new CustomerRegistrationRequest(name, email, "password12345", age);
        CustomerResponse expected=new CustomerResponse(name, email, age) ;

        webTestClient.post()
                .uri("/api/v1/customers/register")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        List<CustomerResponse> response=webTestClient.get()
                .uri("/api/v1/customers")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(CustomerResponse.class)
                .returnResult()
                .getResponseBody();

        assertThat(response)
                .usingRecursiveFieldByFieldElementComparator()
                .contains(expected);

        CustomerResponse response1=webTestClient.get()
                .uri("/api/v1/customers/{email}", email)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(CustomerResponse.class)
                .returnResult()
                .getResponseBody();

        assertThat(response1)
                .usingRecursiveComparison()
                .comparingOnlyFields("name", "email", "age")
                .isEqualTo(expected);
    }

    @Test
    void canDeleteCustomer(){
        Faker faker=new Faker();
        Random random=new Random();
        Name fakeName=faker.name();
        String name= fakeName.fullName().strip();
        String email=fakeName.lastName().strip()+random.nextInt(0, 500)+"@optimizer.com";
        int age=random.nextInt(15, 60);

        CustomerRegistrationRequest request=new CustomerRegistrationRequest(name, email, "password12345", age);
        CustomerResponse expected=new CustomerResponse(name, email, age) ;

        webTestClient.post()
                .uri("/api/v1/customers/register")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        CustomerResponse response=webTestClient.get()
                .uri("/api/v1/customers/{email}", email)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectBody(CustomerResponse.class)
                .returnResult()
                .getResponseBody();

        assertThat(response)
                .usingRecursiveComparison()
                .comparingOnlyFields("name", "email", "age")
                .isEqualTo(expected);

        CustomerDeletionRequest deletionRequest=new CustomerDeletionRequest(email);

        webTestClient.method(HttpMethod.DELETE)
                .uri("/api/v1/customers/remove")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(deletionRequest), CustomerDeletionRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        ApplicationExceptionPayload expectedException=webTestClient.get()
                .uri("/api/v1/customers/{email}", email)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.CONFLICT)
                .expectBody(ApplicationExceptionPayload.class)
                .returnResult()
                .getResponseBody();

        assertThat(expectedException)
                .satisfies(ex->{
                    assertThat(ex.message().equals("Customer ["+email+"] not found")).isTrue();
                    assertThat(ex.code()==1404).isTrue();
                    assertThat(ex.exception().equals(ErrorCode.CUSTOMER_NOT_FOUND)).isTrue();
                });
    }

    @Test
    void canUpdateCustomerEmail(){
        Faker faker=new Faker();
        Random random=new Random();
        Name fakeName=faker.name();
        String name= fakeName.lastName().strip();
        String email=name+random.nextInt(0, 500)+"@optimizer.com";
        int age=random.nextInt(15, 60);

        CustomerRegistrationRequest request=new CustomerRegistrationRequest(name, email, "password12345",age);
        CustomerResponse expected=new CustomerResponse(name, email, age) ;

        webTestClient.post()
                .uri("/api/v1/customers/register")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        CustomerResponse response=webTestClient.get()
                .uri("/api/v1/customers/{email}", email)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectBody(CustomerResponse.class)
                .returnResult()
                .getResponseBody();

        assertThat(response)
                .usingRecursiveComparison()
                .comparingOnlyFields("name", "email", "age")
                .isEqualTo(expected);

        String newEmail=name+"newmail@optimizer.com";
        CustomerUpdateRequest updateRequest=new CustomerUpdateRequest(email,  newEmail, "email");

        webTestClient.method(HttpMethod.PATCH)
                .uri("/api/v1/customers/update")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateRequest), CustomerUpdateRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        CustomerResponse response1=webTestClient.get()
                .uri("/api/v1/customers/{email}", newEmail)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(CustomerResponse.class)
                .returnResult()
                .getResponseBody();

        CustomerResponse updated=new CustomerResponse(name, newEmail, age);
        assertThat(response1)
                .usingRecursiveComparison()
                .comparingOnlyFields("name", "email", "age")
                .isEqualTo(updated);

    }

    @Test
    void canUpdateCostumerAge(){
        Faker faker=new Faker();
        Random random=new Random();
        Name fakeName=faker.name();
        String name= fakeName.fullName().strip();
        String email=fakeName.lastName().strip()+random.nextInt(0, 500)+"@optimizer.com";
        int age=random.nextInt(15, 60);

        CustomerRegistrationRequest request=new CustomerRegistrationRequest(name, email, "password12345", age);
        CustomerResponse expected=new CustomerResponse(name, email, age) ;

        webTestClient.post()
                .uri("/api/v1/customers/register")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        CustomerResponse response=webTestClient.get()
                .uri("/api/v1/customers/{email}", email)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectBody(CustomerResponse.class)
                .returnResult()
                .getResponseBody();

        assertThat(response)
                .usingRecursiveComparison()
                .comparingOnlyFields("name", "email", "age")
                .isEqualTo(expected);

        int newAge=80;
        CustomerUpdateRequest updateRequest=new CustomerUpdateRequest(email,  newAge, "age");

        webTestClient.method(HttpMethod.PATCH)
                .uri("/api/v1/customers/update")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateRequest), CustomerUpdateRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        CustomerResponse response1=webTestClient.get()
                .uri("/api/v1/customers/{email}", email)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectBody(CustomerResponse.class)
                .returnResult()
                .getResponseBody();

        CustomerResponse updated=new CustomerResponse(name, email, newAge);
        assertThat(response1)
                .usingRecursiveComparison()
                .comparingOnlyFields("name", "email", "age")
                .isEqualTo(updated);
    }

    @Test
    void canUpdateCustomerPassword(){
        Faker faker=new Faker();
        Random random=new Random();
        Name fakeName=faker.name();
        String name= fakeName.fullName().strip();
        String email=fakeName.lastName().strip()+random.nextInt(0, 500)+"@optimizer.com";
        int age=random.nextInt(15, 60);

        CustomerRegistrationRequest request=new CustomerRegistrationRequest(name, email, "password12345", age);
        CustomerResponse expected=new CustomerResponse(name, email, age) ;

        webTestClient.post()
                .uri("/api/v1/customers/register")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        CustomerResponse response=webTestClient.get()
                .uri("/api/v1/customers/{email}", email)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectBody(CustomerResponse.class)
                .returnResult()
                .getResponseBody();

        assertThat(response)
                .usingRecursiveComparison()
                .comparingOnlyFields("name", "email", "age")
                .isEqualTo(expected);

        String newPassword="newpassword12345";
        CustomerUpdateRequest updateRequest=new CustomerUpdateRequest(email,  newPassword, "password");

        webTestClient.method(HttpMethod.PATCH)
                .uri("/api/v1/customers/update")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateRequest), CustomerUpdateRequest.class)
                .exchange()
                .expectStatus()
                .isOk();
    }
}