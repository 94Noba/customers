package sn.optimizer.amigosFullStackCourse.customer.security.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFilter;
import sn.optimizer.amigosFullStackCourse.exception.ApplicationExceptionPayload;
import sn.optimizer.amigosFullStackCourse.exception.ErrorCode;

import java.io.IOException;
import java.time.LocalDateTime;

public class CustomerAuthenticationFilter extends AuthenticationFilter {

    private static final String AUTH_PATH="/api/v1/auth";
    public CustomerAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager, CustomerAuthenticationFilter::converter);
        super.setRequestMatcher(CustomerAuthenticationFilter::matches);
        super.setFailureHandler(CustomerAuthenticationFilter::onAuthenticationFailure);
        super.setSuccessHandler(CustomerAuthenticationFilter::onAuthenticationSuccess);
    }

    private static CustomerAuthentication converter(HttpServletRequest request){
        String username=request.getParameter("username");
        String password=request.getParameter("password");
        if(username==null || password==null
                ||username.isEmpty() || password.isEmpty())
            throw new BadCredentialsException("invalid credentials");

        return CustomerAuthentication.customerAuthenticationCandidate(username, password);
    }

    private static  boolean matches(HttpServletRequest request){
        return request.getRequestURI().equals(AUTH_PATH);
    }

    private static void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                               AuthenticationException exception) throws IOException {
        ApplicationExceptionPayload payload=new ApplicationExceptionPayload(exception.getMessage(), ErrorCode.AUTHENTICATION_FAILED,
                ErrorCode.AUTHENTICATION_FAILED.getCode(), LocalDateTime.now().toString(), request.getRequestURI());
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.getOutputStream()
                .println(new ObjectMapper().writeValueAsString(payload));
    }

    private static  void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                                 Authentication authentication) throws IOException, ServletException{

    }
}