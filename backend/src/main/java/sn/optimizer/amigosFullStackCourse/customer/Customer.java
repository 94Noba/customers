package sn.optimizer.amigosFullStackCourse.customer;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.optimizer.amigosFullStackCourse.customer.data.CustomerRegistrationRequest;
import sn.optimizer.amigosFullStackCourse.customer.repository.CustomerJpaRepository;
import sn.optimizer.amigosFullStackCourse.customer.service.CustomerService;
import sn.optimizer.amigosFullStackCourse.customer.utilities.CustomerUpdater;
import sn.optimizer.amigosFullStackCourse.customer.utilities.CustomerUpdaterFactory;
import sn.optimizer.amigosFullStackCourse.customer.validator.CustomerRegistrationRequestValidator;
import sn.optimizer.amigosFullStackCourse.customer.validator.ValidationResult;
import sn.optimizer.amigosFullStackCourse.exception.ApplicationException;
import sn.optimizer.amigosFullStackCourse.exception.CustomerRegistrationException;
import sn.optimizer.amigosFullStackCourse.exception.ErrorCode;

import java.util.ArrayList;
import java.util.List;
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
    @Column(nullable = false)
    private Integer age;

    public Customer(Long id, String name, String email, Integer age) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
    }

    public Customer(String name, String email, Integer age) {
        this.name = name;
        this.email = email;
        this.age = age;
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
