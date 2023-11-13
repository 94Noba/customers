package sn.optimizer.amigosFullStackCourse.customer.utilities;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import sn.optimizer.amigosFullStackCourse.customer.Customer;
import sn.optimizer.amigosFullStackCourse.customer.security.permission.Role;
import sn.optimizer.amigosFullStackCourse.customer.utilities.updaterImpls.CustomerUpdaterFactory;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerEmailUpdaterTest {

    private static CustomerUpdater underTest;

    @BeforeAll
    static void  setUp() {
        underTest= CustomerUpdaterFactory.of("email");
    }

    @Test
    void canUpdateCustomerIfEmailIsValid() {
        Customer customer=new Customer("Sidi BA", "sidiba@optimizer.com",
                "password", Role.CUSTOMER, 25, true);
        String email="sidi@optimizer.com";

        int rs=underTest.updateCustomer(customer, email);

        assertThat(rs==1).isTrue();
        assertThat(customer.getEmail().equals(email)).isTrue();
    }

    @Test
    void canUpdateCustomerFailIfEmailIsNotValid(){
        Customer customer=new Customer("Sidi BA", "sidi@optimizer.com",
                "password", Role.VIP, 25, true);
        String email="something-wrong";

        int rs=underTest.updateCustomer(customer, email);

        assertThat(rs==-1).isTrue();
        assertThat(customer.getEmail().equals(email)).isFalse();
    }
}