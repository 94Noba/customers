package sn.optimizer.amigosFullStackCourse.customer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import sn.optimizer.amigosFullStackCourse.MainDataAccessTest;
import sn.optimizer.amigosFullStackCourse.customer.repository.CustomerJpaRepository;
import sn.optimizer.amigosFullStackCourse.customer.security.permission.Role;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerJpaRepositoryTest extends MainDataAccessTest {

    @Autowired
    private CustomerJpaRepository underTest;

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
    }

    @Test
    void existsByEmail() {
        Customer customer= Customer.builder()
                .name("testname")
                .email("testemail")
                .password("password")
                .role(Role.CUSTOMER)
                .age(19)
                .active(true)
                .build();

        underTest.save(customer);

        assertThat(underTest.existsByEmail("testemail")).isTrue();
    }

    @Test
    void canExistsByEmailReturnFalseIfCustomerNotExist(){
        Customer customer= Customer.builder()
                .name("testname")
                .email("testemail")
                .password("password")
                .role(Role.VIP)
                .age(19)
                .active(true)
                .build();

        underTest.save(customer);

        assertThat(underTest.existsByEmail("email")).isFalse();
    }

    @Test
    void findByEmail() {
        Customer customer= Customer.builder()
                .name("testname")
                .email("testemail")
                .password("password")
                .role(Role.CUSTOMER)
                .age(19)
                .active(true)
                .build();

        underTest.save(customer);

        Optional<Customer> expected=underTest.findByEmail("testemail");

        assertThat(expected).isPresent()
                .hasValueSatisfying(c->{
                    assertThat(c.getEmail().equals(customer.getEmail())).isTrue();
                    assertThat(c.getName().equals(customer.getName())).isTrue();
                    assertThat(c.getAge().equals(customer.getAge())).isTrue();
                    assertThat(c.getPassword().equals("password")).isTrue();
                    assertThat(c.isActive()).isTrue();
                });
    }

    @Test
    void canFindByEmailReturnEmptyOptionalCustomerIfCustomerNotExist(){
        Customer customer= Customer.builder()
                .name("testname")
                .email("testemail")
                .password("password")
                .role(Role.CUSTOMER)
                .age(19)
                .active(true)
                .build();

        underTest.save(customer);

        assertThat(underTest.findByEmail("email")).isEmpty();
    }
}