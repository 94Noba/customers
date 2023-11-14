package sn.optimizer.amigosFullStackCourse.customer.utilities.updaterImpls;

import sn.optimizer.amigosFullStackCourse.customer.utilities.CustomerUpdater;
import sn.optimizer.amigosFullStackCourse.customer.utilities.updaterImpls.CustomerAgeUpdater;
import sn.optimizer.amigosFullStackCourse.customer.utilities.updaterImpls.CustomerEmailUpdater;
import sn.optimizer.amigosFullStackCourse.customer.utilities.updaterImpls.CustomerPasswordUpdater;

public class CustomerUpdaterFactory {

    public static CustomerUpdater of(String updater){
        return switch (updater) {
            case "email" -> new CustomerEmailUpdater();
            case "age" -> new CustomerAgeUpdater();
            case "password" -> new CustomerPasswordUpdater();
            default -> null;
        };
    }
}
