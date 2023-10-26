package sn.optimizer.amigosFullStackCourse.customer.utilities;

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
