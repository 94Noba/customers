package sn.optimizer.amigosFullStackCourse.customer.utilities;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import sn.optimizer.amigosFullStackCourse.customer.Customer;

import static org.assertj.core.api.Assertions.assertThat;

public class CustomerPasswordUpdaterTest {

    private static CustomerUpdater underTest;

    @BeforeAll
    static void setUp(){
        underTest=CustomerUpdaterFactory.of("password");
    }

    @Test
    void canUpdateCustomerAge() {

        Customer customer=new Customer("Sidi BA", "sidi@optimizer.com",
                "password", 25, true);

        String password="password12345";
        int rs=underTest.updateCustomer(customer, password);

        assertThat(rs==1).isTrue();
        assertThat(customer.getPassword().equals(password)).isTrue();
    }

    @Test
    void canUpdateCustomerAgeFailIfAgeIsNotValid(){
        Customer customer=new Customer("Sidi BA", "sidi@optimizer.com",
                "password", 25, true);

        String password="pa";
        int rs=underTest.updateCustomer(customer, password);

        assertThat(rs==-1).isTrue();
        assertThat(customer.getPassword().equals(password)).isFalse();
    }
}
