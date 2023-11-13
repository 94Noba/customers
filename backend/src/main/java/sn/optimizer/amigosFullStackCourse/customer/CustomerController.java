package sn.optimizer.amigosFullStackCourse.customer;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import sn.optimizer.amigosFullStackCourse.customer.data.CustomerDeletionRequest;
import sn.optimizer.amigosFullStackCourse.customer.data.CustomerRegistrationRequest;
import sn.optimizer.amigosFullStackCourse.customer.data.CustomerResponse;
import sn.optimizer.amigosFullStackCourse.customer.data.CustomerUpdateRequest;
import sn.optimizer.amigosFullStackCourse.customer.security.authentication.CustomerAuthentication;
import sn.optimizer.amigosFullStackCourse.customer.security.jwt.JwtService;
import sn.optimizer.amigosFullStackCourse.customer.service.CustomerService;


@RestController
@RequestMapping("/api/v1")
public class CustomerController {

    private final CustomerService customerService;
    private final JwtService jwtService;

    public CustomerController(CustomerService customerService, JwtService jwtService){
        this.customerService=customerService;
        this.jwtService=jwtService;
    }

    @GetMapping
    public String home(){
        return "Hello Im the home page";
    }

    @GetMapping("/vip")
    @PreAuthorize("hasRole('ROLE_VIP')")
    public String privateSpace(Authentication authentication){
        return "Hello ["+authentication.getName()+"] welcome to the VIP space" ;
    }

    @GetMapping("/public")
    @PreAuthorize("hasAnyRole('ROLE_VIP', 'ROLE_CUSTOMER')")
    public String publicSpace(Authentication authentication){
        return "Hello ["+authentication.getName()+"] welcome to the public space";
    }


    @GetMapping("/customer/{email}")
    @PostAuthorize("hasAuthority('permission:public') && #email.equals(authentication.name)")
    public CustomerResponse getCustomer(@PathVariable(name="email") String email){
        return customerService.getCustomerResponseByEmail(email);
    }

    @PostMapping("/register")
    public void registerCustomer(@RequestBody CustomerRegistrationRequest customerRegistrationRequest){
        customerService.registerCustomer(customerRegistrationRequest);
    }


    @DeleteMapping("/remove")
    @PreAuthorize("hasAuthority('permission:delete') && #customerDeletionRequest.email().equals(authentication.name)")
    public void deleteCustomer(@RequestBody CustomerDeletionRequest customerDeletionRequest){
        customerService.deleteCustomer(customerDeletionRequest.email());
    }

    @PatchMapping("/update")
    @PreAuthorize("hasAuthority('permission:public') && authentication.name.equals(#customerUpdateRequest.email())")
    public void updateCustomer(@RequestBody CustomerUpdateRequest customerUpdateRequest){
        customerService.updateCustomer(customerUpdateRequest);
    }

    @PostMapping("/auth")
    public ResponseEntity<?> authentication(Authentication authentication){
        String token="";
        if(authentication instanceof CustomerAuthentication){
            token= jwtService.generateJwtToken(authentication.getName());
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, token)
                .build();
    }
}
