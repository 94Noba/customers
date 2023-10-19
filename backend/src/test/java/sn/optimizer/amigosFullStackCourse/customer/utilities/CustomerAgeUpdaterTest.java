package sn.optimizer.amigosFullStackCourse.customer.utilities;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import sn.optimizer.amigosFullStackCourse.customer.Customer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class CustomerAgeUpdaterTest {

    private static CustomerUpdater underTest;

    @BeforeAll
    static void setUp(){
       underTest=CustomerUpdaterFactory.of("age");
    }

    @Test
    void canUpdateCustomerAge() {

        Customer customer=new Customer("Sidi Ba",
                "sidi@optimizer.com", 25);

        int age=29;
        int rs=underTest.updateCustomer(customer, age);

        assertThat(rs==1).isTrue();
        assertThat(customer.getAge().equals(age)).isTrue();
    }

    @Test
    void canUpdateCustomerAgeFailIfAgeIsNotValid(){
        Customer customer=new Customer("Sidi Ba",
                "sidi@optimizer.com", 25);

        int age=4;
        int rs=underTest.updateCustomer(customer, age);

        assertThat(rs==-1).isTrue();
        assertThat(customer.getAge().equals(age)).isFalse();
    }
}