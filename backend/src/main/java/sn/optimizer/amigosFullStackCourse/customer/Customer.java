package sn.optimizer.amigosFullStackCourse.customer;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "customers", uniqueConstraints = {
        @UniqueConstraint(name = "UQ_CUSTOMERS_EMAIL", columnNames = {"email"})
})
@Getter
@Setter
@Builder
public class Customer {

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

    @Column(nullable = false)
    private Integer age;

    @Column(columnDefinition = "bool not null default true")
    private boolean active;

    public Customer(Long id, String name, String email, String password, Integer age, boolean active) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password=password;
        this.age = age;
        this.active=active;
    }

    public Customer(String name, String email, String password, Integer age, boolean active) {
        this.name = name;
        this.email = email;
        this.password=password;
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
                Objects.equals(email, customer.email) && Objects.equals(age, customer.age);
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
                ", age=" + age +
                '}';
    }
}
