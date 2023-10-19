package sn.optimizer.amigosFullStackCourse.customer.utilities;

import sn.optimizer.amigosFullStackCourse.customer.Customer;

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
