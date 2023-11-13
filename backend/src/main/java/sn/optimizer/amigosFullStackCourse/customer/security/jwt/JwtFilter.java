package sn.optimizer.amigosFullStackCourse.customer.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import sn.optimizer.amigosFullStackCourse.customer.Customer;
import sn.optimizer.amigosFullStackCourse.customer.security.CustomerDetailsService;
import sn.optimizer.amigosFullStackCourse.customer.security.authentication.CustomerAuthentication;
import sn.optimizer.amigosFullStackCourse.exception.ApplicationException;
import sn.optimizer.amigosFullStackCourse.exception.ApplicationExceptionPayload;
import sn.optimizer.amigosFullStackCourse.exception.ErrorCode;

import java.io.IOException;
import java.time.LocalDateTime;


@Component
public class JwtFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_TYPE="Bearer ";
    private static final int CUT_FROM=7;
    private final JwtService jwtService;
    private final CustomerDetailsService customerDetailsService;


    JwtFilter(JwtService jwtService, CustomerDetailsService customerDetailsService){
        this.jwtService=jwtService;
        this.customerDetailsService=customerDetailsService;

    }


    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException, ApplicationException {

        String authorization=request.getHeader(HttpHeaders.AUTHORIZATION);
        if(authorization==null || !authorization.startsWith(AUTHORIZATION_TYPE)){
            filterChain.doFilter(request, response);
            return;
        }
        String token=authorization.substring(CUT_FROM);
       try{
           Jws<Claims> claims=jwtService.parseToken(token);
           String subject= jwtService.getSubject(claims);
           if(subject!=null && SecurityContextHolder.getContext().getAuthentication()==null){
               Customer customer=(Customer)customerDetailsService.loadUserByUsername(subject);
               if(!customer.isActive()){
                   onFailure(request, response, "Account locked",
                           ErrorCode.ACCOUNT_LOCKED);
                   return;
               }
               CustomerAuthentication authenticated=CustomerAuthentication
                       .authenticatedCustomer(subject, customer.getRole().getAuthorities());
               onSuccess(request, response, authenticated);
           }
           filterChain.doFilter(request, response);
       }catch (SignatureException e){
           onFailure(request, response, "Authorization token corrupted", ErrorCode.AUTHORIZATION_TOKEN_CORRUPTED);
       }catch (ExpiredJwtException e){
           onFailure(request, response, "Authorization token expired", ErrorCode.AUTHORIZATION_TOKEN_EXPIRED);
       }catch (MalformedJwtException | UnsupportedJwtException
               | IllegalArgumentException | UsernameNotFoundException e){
           onFailure(request, response, "Authorization token invalid", ErrorCode.AUTHORIZATION_TOKEN_INVALID);
        }
    }

    private static void onFailure(HttpServletRequest request, HttpServletResponse response,
                                  String message, ErrorCode errorCode) throws IOException {
        SecurityContextHolder.clearContext();
        ApplicationExceptionPayload payload=new ApplicationExceptionPayload(message, errorCode,
                errorCode.getCode(), LocalDateTime.now().toString(), request.getRequestURI());
        response.setContentType("application/json");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getOutputStream()
                .println(new ObjectMapper().writeValueAsString(payload));
    }

    private static void onSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication){
        SecurityContext context=SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }
}
