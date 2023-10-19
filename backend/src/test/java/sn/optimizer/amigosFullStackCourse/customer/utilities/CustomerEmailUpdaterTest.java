package sn.optimizer.amigosFullStackCourse.customer.utilities;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sn.optimizer.amigosFullStackCourse.customer.Customer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CustomerEmailUpdaterTest {

    private static CustomerUpdater underTest;

    @BeforeAll
    static void  setUp() {
        underTest=CustomerUpdaterFactory.of("email");
    }

    @Test
    void canUpdateCustomerIfEmailIsValid() {
        Customer customer=new Customer("Sidi Ba", "sidi@gmail.com", 28);
        String email="sidi@optimizer.com";

        int rs=underTest.updateCustomer(customer, email);

        assertThat(rs==1).isTrue();
        assertThat(customer.getEmail().equals(email)).isTrue();
    }

    @Test
    void canUpdateCustomerFailIfEmailIsNotValid(){
        Customer customer=new Customer("Sidi Ba", "sidi@gmail.com", 28);
        String email="something-wrong";

        int rs=underTest.updateCustomer(customer, email);

        assertThat(rs==-1).isTrue();
        assertThat(customer.getEmail().equals(email)).isFalse();
    }
}