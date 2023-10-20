package sn.optimizer.amigosFullStackCourse.customer;

import org.springframework.web.bind.annotation.*;
import sn.optimizer.amigosFullStackCourse.customer.data.CustomerDeletionRequest;
import sn.optimizer.amigosFullStackCourse.customer.data.CustomerRegistrationRequest;
import sn.optimizer.amigosFullStackCourse.customer.data.CustomerResponse;
import sn.optimizer.amigosFullStackCourse.customer.data.CustomerUpdateRequest;
import sn.optimizer.amigosFullStackCourse.customer.service.CustomerService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService){
        this.customerService=customerService;
    }

    @GetMapping("/home")
    public String home(){
        return "Hello Im the home page";
    }

    @GetMapping
    public List<CustomerResponse> getCustomers(){
        return customerService.getAllCustomers();
    }

    @GetMapping("{email}")
    public CustomerResponse getCustomer(@PathVariable(name="email") String email){
        return customerService.getCustomerResponseByEmail(email);
    }

    @PostMapping("/register")
    public void registerCustomer(@RequestBody CustomerRegistrationRequest customerRegistrationRequest){
        customerService.registerCustomer(customerRegistrationRequest);
    }

    @DeleteMapping("/remove")
    public void deleteCustomer(@RequestBody CustomerDeletionRequest customerDeletionRequest){
        customerService.deleteCustomer(customerDeletionRequest.email());
    }

    @PatchMapping("/update")
    public void updateCustomer(@RequestBody CustomerUpdateRequest customerUpdateRequest){
        customerService.updateCustomer(customerUpdateRequest);
    }
}
