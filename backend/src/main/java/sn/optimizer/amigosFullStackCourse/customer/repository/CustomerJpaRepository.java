package sn.optimizer.amigosFullStackCourse.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.optimizer.amigosFullStackCourse.customer.Customer;

import java.util.Optional;

public interface CustomerJpaRepository extends JpaRepository<Customer, Long> {

    boolean existsByEmail(String email);

    Optional<Customer> findByEmail(String email);
}
