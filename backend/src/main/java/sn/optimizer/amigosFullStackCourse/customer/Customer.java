package sn.optimizer.amigosFullStackCourse.customer;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import sn.optimizer.amigosFullStackCourse.customer.security.permission.Role;

import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "customers", uniqueConstraints = {
        @UniqueConstraint(name = "UQ_CUSTOMERS_EMAIL", columnNames = {"email"})
})
@Getter
@Setter
@Builder
public class Customer  implements UserDetails {

    @Id
    @SequenceGenerator(name = "customers_id_sequence",
            sequenceName = "customers_id_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "customers_id_sequence")
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(columnDefinition = "varchar(255) not null default 'password'")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(15) not null default 'CUSTOMER'")
    private Role role;

    @Column(nullable = false)
    private Integer age;

    @Column(columnDefinition = "bool not null default true")
    private boolean active;

    public Customer(Long id, String name,
                    String email, String password,
                    Role role, Integer age, boolean active) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password=password;
        this.role=role;
        this.age = age;
        this.active=active;
    }

    public Customer(String name, String email,
                    String password, Role role,
                    Integer age, boolean active) {
        this.name = name;
        this.email = email;
        this.password=password;
        this.role=role;
        this.age = age;
        this.active=active;
    }

    public Customer() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(id, customer.id) && Objects.equals(name, customer.name) &&
                Objects.equals(email, customer.email) && Objects.equals(age, customer.age)&&
                Objects.equals(password, customer.password)&&Objects.equals(role, customer.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, age);
    }

    @Override
    public String toString() {
        return "customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", role='" +role+ '\''+
                ", age=" + age +
                '}';
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.active;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.active;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.active;
    }

    @Override
    public boolean isEnabled() {
        return this.active;
    }
}
