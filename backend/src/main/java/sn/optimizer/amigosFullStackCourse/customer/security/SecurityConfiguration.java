package sn.optimizer.amigosFullStackCourse.customer.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfiguration {

    private static final String REGISTRATION_PATH="/api/v1/register";
    private static final String AUTH_PATH="/api/v1/auth";
    private static final String HOME_PATH="/api/v1";
    private final CustomerConfigurer customerConfigurer;
    private final AuthEntryPoint authEntryPoint;

    SecurityConfiguration(CustomerConfigurer customerConfigurer,
                          AuthEntryPoint authEntryPoint){
        this.customerConfigurer=customerConfigurer;
        this.authEntryPoint=authEntryPoint;

    }

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf-> csrf.disable())
                .authorizeHttpRequests(request->request
                        .requestMatchers(HttpMethod.POST, REGISTRATION_PATH, AUTH_PATH).permitAll()
                        .requestMatchers(HttpMethod.GET, HOME_PATH).permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(session->session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(e->e
                        .authenticationEntryPoint(authEntryPoint))
                .apply(customerConfigurer);

        return http.build();
    }
}
