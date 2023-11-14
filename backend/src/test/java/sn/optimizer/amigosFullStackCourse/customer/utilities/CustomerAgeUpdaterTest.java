package sn.optimizer.amigosFullStackCourse.customer.utilities;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import sn.optimizer.amigosFullStackCourse.customer.Customer;
import sn.optimizer.amigosFullStackCourse.customer.security.permission.Role;
import sn.optimizer.amigosFullStackCourse.customer.utilities.updaterImpls.CustomerUpdaterFactory;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerAgeUpdaterTest {

    private static CustomerUpdater underTest;

    @BeforeAll
    static void setUp(){
       underTest= CustomerUpdaterFactory.of("age");
    }

    @Test
    void canUpdateCustomerAge() {

        Customer customer=new Customer("Sidi BA", "sidi@optimizer.com",
                "password", Role.CUSTOMER, 25, true);

        int age=29;
        int rs=underTest.updateCustomer(customer, age);

        assertThat(rs==1).isTrue();
        assertThat(customer.getAge().equals(age)).isTrue();
    }

    @Test
    void canUpdateCustomerAgeFailIfAgeIsNotValid(){
        Customer customer=new Customer("Sidi BA", "sidi@optimizer.com",
                "password", Role.CUSTOMER, 25, true);

        int age=4;
        int rs=underTest.updateCustomer(customer, age);

        assertThat(rs==-1).isTrue();
        assertThat(customer.getAge().equals(age)).isFalse();
    }
}