package sn.optimizer.amigosFullStackCourse.customer.security.permission;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public enum Role {

    CUSTOMER(Set.of(Permission.PUBLIC)),
    VIP(Set.of(Permission.PUBLIC, Permission.PRIVATE, Permission.DELETE));

    private final Set<Permission>permissions;

    Role(Set<Permission> permissions){
        this.permissions=permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getAuthorities(){

        Set<SimpleGrantedAuthority>authorities=this.permissions
                .stream()
                .map(authority->new SimpleGrantedAuthority(authority.getPermission()))
                .collect(Collectors.toSet());

        authorities.add(new SimpleGrantedAuthority("ROLE_"+this));

        return authorities;
    }
}
