package sn.optimizer.amigosFullStackCourse.customer.utilities;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CustomerUpdaterFactoryTest {

    @Test
    void canCreateCustomerEmailUpdater() {
        CustomerUpdater emailUpdater=CustomerUpdaterFactory.of("email");

        assertThat(emailUpdater instanceof CustomerEmailUpdater).isTrue();
    }

    @Test
    void canCreateCustomerAgeUpdater(){
        CustomerUpdater ageUpdater=CustomerUpdaterFactory.of("age");

        assertThat(ageUpdater instanceof CustomerAgeUpdater).isTrue();
    }

    @Test
    void canReturnNullIfFactoryMethodParameterIsNotCorrect(){

        CustomerUpdater updater=CustomerUpdaterFactory.of("something");

        assertThat(updater==null).isTrue();
    }
}