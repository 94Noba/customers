package sn.optimizer.amigosFullStackCourse.customer.utilities.updaterImpls;

import sn.optimizer.amigosFullStackCourse.customer.Customer;
import sn.optimizer.amigosFullStackCourse.customer.utilities.CustomerUpdater;

public class CustomerAgeUpdater implements CustomerUpdater {
    @Override
    public int updateCustomer(Customer customer, Object patch){
        if(patch!=null && (int)patch>=10){
            customer.setAge((Integer) patch);
            return 1;
        }
        return -1;
    }
}
