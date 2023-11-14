package sn.optimizer.amigosFullStackCourse.customer.security.authentication;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;
import sn.optimizer.amigosFullStackCourse.customer.Customer;

@Component
public class CustomerAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;

    CustomerAuthenticationProvider(UserDetailsService userDetailsService){
        this.userDetailsService=userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return this.processAuthentication(authentication.getName(),
                authentication.getCredentials());
    }

    private CustomerAuthentication processAuthentication(String username, Object credentials){
        Customer customer=(Customer) userDetailsService.loadUserByUsername(username);
        if(!customer.isActive())
            throw new DisabledException("Account disabled");

        if(!BCrypt.checkpw((String)credentials, customer.getPassword()))
            throw new BadCredentialsException("Incorrect password");

        return CustomerAuthentication.authenticatedCustomer(username,
                customer.getRole().getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(CustomerAuthentication.class);
    }
}
