package sn.optimizer.amigosFullStackCourse.customer.security.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import java.util.Collection;
import java.util.Set;

public class CustomerAuthentication implements Authentication {

    private final String username;
    private final Set<? extends GrantedAuthority> authorities;
    private WebAuthenticationDetails details;
    private Object credentials;


    private CustomerAuthentication(String username, Object credentials){
        this.username=username;
        this.credentials=credentials;
        this.authorities=null;
        this.details=null;
    }

    private CustomerAuthentication(String username, Set<? extends GrantedAuthority> authorities){
        this.username=username;
        this.authorities=authorities;
    }

    public static CustomerAuthentication customerAuthenticationCandidate(String username, Object credentials){
        return new CustomerAuthentication(username, credentials);
    }

    public static CustomerAuthentication authenticatedCustomer(String username, Set<? extends GrantedAuthority>authorities){
        return new CustomerAuthentication(username, authorities);
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    public Set<? extends GrantedAuthority> getAuthoritiesSet(){
        return this.authorities;
    }

    @Override
    public Object getCredentials() {
        return isAuthenticated() ? "[PROTECTED]" : this.credentials;
    }

    @Override
    public Object getDetails() {
        return this.details;
    }

    @Override
    public Object getPrincipal() {
        return this.username;
    }

    @Override
    public boolean isAuthenticated() {
        return this.authorities!=null;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        throw new IllegalArgumentException("Invalid operation");

    }

    @Override
    public String getName() {
        return this.username;
    }

    public void  setDetails(WebAuthenticationDetails details){
        this.details=details;
    }
}
