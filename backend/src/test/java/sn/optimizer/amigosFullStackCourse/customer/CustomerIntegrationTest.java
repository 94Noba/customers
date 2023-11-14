package sn.optimizer.amigosFullStackCourse.customer;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import sn.optimizer.amigosFullStackCourse.customer.data.CustomerDeletionRequest;
import sn.optimizer.amigosFullStackCourse.customer.data.CustomerRegistrationRequest;
import sn.optimizer.amigosFullStackCourse.customer.data.CustomerResponse;
import sn.optimizer.amigosFullStackCourse.customer.data.CustomerUpdateRequest;
import sn.optimizer.amigosFullStackCourse.customer.security.jwt.JwtService;
import sn.optimizer.amigosFullStackCourse.customer.utilities.roleSuppliers.RoleSupplierFactory;
import sn.optimizer.amigosFullStackCourse.exception.ApplicationExceptionPayload;
import sn.optimizer.amigosFullStackCourse.exception.ErrorCode;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerIntegrationTest {

    @Autowired
    private WebTestClient apiClient;

    @Autowired
    private JwtService jwtService;

    private CustomerRegistrationRequest registrationRequest;



    public void init(String role){
        Faker faker=new Faker();
        Name name=faker.name();
        Random id=new Random();
        String fullName= name.fullName();
        String email=name.lastName()+id.nextInt(0, 100)+"@optimizer.com";
        String password=name.firstName()+"123";
        int age=id.nextInt(15, 30);

        registrationRequest=new CustomerRegistrationRequest(fullName, email, password,
                role, age);
    }

    private String registrationAndAuth(String role){

        init(role);
        apiClient.post()
                .uri("/api/v1/register")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(registrationRequest), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        return apiClient.post()
                .uri(uriBuilder ->uriBuilder
                        .path("/api/v1/auth")
                        .queryParam("username", registrationRequest.email())
                        .queryParam("password", registrationRequest.password())
                        .build())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Void.class)
                .returnResult()
                .getResponseHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);
    }

    @Test
    public void canRegisterCustomer(){

        String token=registrationAndAuth("customer");

        CustomerResponse expected= apiClient.get()
                .uri("/api/v1/customer/{email}", registrationRequest.email())
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(CustomerResponse.class)
                .returnResult()
                .getResponseBody();

        assertThat(expected)
                .satisfies(ex->{
                    assertThat(ex.email().equals(registrationRequest.email())).isTrue();
                    assertThat(ex.name().equals(registrationRequest.name())).isTrue();
                    assertThat(ex.age()==registrationRequest.age()).isTrue();
                    assertThat(ex.role().equals(
                            RoleSupplierFactory.of(registrationRequest.role()).get())).isTrue();
                });
    }

    @Test
    public void canRemoveCustomer(){
        String token=registrationAndAuth("vip");
        CustomerDeletionRequest deletionRequest=new CustomerDeletionRequest(registrationRequest.email());

        apiClient.method(HttpMethod.DELETE)
                .uri("/api/v1/remove")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(deletionRequest), CustomerDeletionRequest.class)
                .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token))
                .exchange()
                .expectStatus()
                .isOk();

        ApplicationExceptionPayload expected=apiClient.get()
                .uri("/api/v1/customer/{email}", registrationRequest.email())
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token))
                .exchange()
                .expectStatus()
                .isUnauthorized()
                .expectBody(ApplicationExceptionPayload.class)
                .returnResult()
                .getResponseBody();

        assertThat(expected)
                .satisfies(ex->{
                    assertThat(ex.message().equals("Authorization token invalid")).isTrue();
                    assertThat(ex.exception().equals(ErrorCode.AUTHORIZATION_TOKEN_INVALID)).isTrue();
                    assertThat(ex.code()==ErrorCode.AUTHORIZATION_TOKEN_INVALID.getCode()).isTrue();
                });
    }

    @Test
    void canUpdateCustomerEmail(){
        String token=registrationAndAuth("vip");
        Random random=new Random(500);
        int id= random.nextInt(7, 1000);
        String newEmail="testest"+id+"@optimizer.com";
        CustomerUpdateRequest updateRequest=new CustomerUpdateRequest(registrationRequest.email(), newEmail,
                "email");

        apiClient.method(HttpMethod.PATCH)
                .uri("/api/v1/update")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token))
                .body(Mono.just(updateRequest), CustomerUpdateRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        ApplicationExceptionPayload expected=apiClient.get()
                .uri("/api/v1/customer/{email}", registrationRequest.email())
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token))
                .exchange()
                .expectStatus()
                .isUnauthorized()
                .expectBody(ApplicationExceptionPayload.class)
                .returnResult()
                .getResponseBody();

        assertThat(expected)
                .satisfies(ex->{
                    assertThat(ex.message().equals("Authorization token invalid")).isTrue();
                    assertThat(ex.exception().equals(ErrorCode.AUTHORIZATION_TOKEN_INVALID)).isTrue();
                });

        String newToken=apiClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/auth")
                        .queryParam("username", newEmail)
                        .queryParam("password", registrationRequest.password())
                        .build())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Void.class)
                .returnResult()
                .getResponseHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);

        CustomerResponse response=apiClient.get()
                .uri("/api/v1/customer/{email}", newEmail)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(newToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(CustomerResponse.class)
                .returnResult()
                .getResponseBody();

        assertThat(response.email().equals(newEmail)).isTrue();

    }

    @Test
    void canUpdateCustomerPassword(){
        String token=registrationAndAuth("vip");

        String newPassword="password123";
        CustomerUpdateRequest updateRequest=new CustomerUpdateRequest(registrationRequest.email(), newPassword, "password");

        apiClient.method(HttpMethod.PATCH)
                .uri("/api/v1/update")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token))
                .body(Mono.just(updateRequest), CustomerUpdateRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        ApplicationExceptionPayload expected= apiClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/auth")
                        .queryParam("username", registrationRequest.email())
                        .queryParam("password", registrationRequest.password())
                        .build())
                .exchange()
                .expectStatus()
                .isUnauthorized()
                .expectBody(ApplicationExceptionPayload.class)
                .returnResult()
                .getResponseBody();

        assertThat(expected)
                .satisfies(ex->{
                    assertThat(ex.message().equals("Incorrect password")).isTrue();
                    assertThat(ex.exception().equals(ErrorCode.AUTHENTICATION_FAILED)).isTrue();
                });

        apiClient.post()
                .uri(uriBuilder->uriBuilder
                        .path("/api/v1/auth")
                        .queryParam("username", registrationRequest.email())
                        .queryParam("password", newPassword)
                        .build())
                .exchange()
                .expectStatus()
                .isOk();
    }


    @Test
    void canAccessVipSpaceIfHasExpectedAuthority(){
        String token=registrationAndAuth("vip");

        String expected=apiClient.get()
                .uri("/api/v1/vip")
                .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token))
                .accept(MediaType.TEXT_PLAIN)
                .exchange()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();

        assertThat(expected.equals("Hello ["+registrationRequest.email()+"] welcome to the VIP space")).isTrue();
    }

    @Test
    void canAccessPublicSpaceIfHasExpectedAuthority(){
        String token=registrationAndAuth("customer");

        String expected=apiClient.get()
                .uri("/api/v1/public")
                .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token))
                .accept(MediaType.TEXT_PLAIN)
                .exchange()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();

        assertThat(expected.equals("Hello ["+registrationRequest.email()+"] welcome to the public space")).isTrue();
    }

    @Test
    void cantAccessVipSpaceIfHaveNotExpectedAuthority(){
        String token=registrationAndAuth("customer");

        apiClient.get()
                .uri("/api/v1/vip")
                .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token))
                .exchange()
                .expectStatus()
                .isForbidden();
    }
}
