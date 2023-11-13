package sn.optimizer.amigosFullStackCourse.customer.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import sn.optimizer.amigosFullStackCourse.customer.security.authentication.CustomerAuthenticationFilter;
import sn.optimizer.amigosFullStackCourse.customer.security.authentication.CustomerAuthenticationProvider;
import sn.optimizer.amigosFullStackCourse.customer.security.jwt.JwtFilter;

@Configuration
public class CustomerConfigurer extends AbstractHttpConfigurer<CustomerConfigurer, HttpSecurity> {

    private final CustomerAuthenticationProvider customerAuthenticationProvider;
    private final JwtFilter jwtFilter;

    CustomerConfigurer(CustomerAuthenticationProvider authenticationProvider,
                       JwtFilter jwtFilter){

        this.customerAuthenticationProvider=authenticationProvider;
        this.jwtFilter=jwtFilter;
    }

    @Override
    public void init(HttpSecurity http){

        http.authenticationProvider(customerAuthenticationProvider);
    }

    @Override
    public void configure(HttpSecurity httpSecurity){
        AuthenticationManager authenticationManager=httpSecurity.getSharedObject(AuthenticationManager.class);
        CustomerAuthenticationFilter customerAuthenticationFilter=new CustomerAuthenticationFilter(authenticationManager);
        httpSecurity.addFilterBefore(customerAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        httpSecurity.addFilterBefore(jwtFilter, CustomerAuthenticationFilter.class);
    }
}
