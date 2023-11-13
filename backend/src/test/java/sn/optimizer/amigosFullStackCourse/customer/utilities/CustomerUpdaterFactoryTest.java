package sn.optimizer.amigosFullStackCourse.customer.utilities;

import org.junit.jupiter.api.Test;
import sn.optimizer.amigosFullStackCourse.customer.utilities.updaterImpls.CustomerAgeUpdater;
import sn.optimizer.amigosFullStackCourse.customer.utilities.updaterImpls.CustomerEmailUpdater;
import sn.optimizer.amigosFullStackCourse.customer.utilities.updaterImpls.CustomerUpdaterFactory;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerUpdaterFactoryTest {

    @Test
    void canCreateCustomerEmailUpdater() {
        CustomerUpdater emailUpdater= CustomerUpdaterFactory.of("email");

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