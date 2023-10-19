package sn.optimizer.amigosFullStackCourse.customer.utilities;

import sn.optimizer.amigosFullStackCourse.customer.Customer;

public interface CustomerUpdater {

    int updateCustomer(Customer customer, Object patch);
}
