package sn.optimizer.amigosFullStackCourse.customer.utilities.updaterImpls;

import sn.optimizer.amigosFullStackCourse.customer.Customer;
import sn.optimizer.amigosFullStackCourse.customer.utilities.CustomerUpdater;

public class CustomerPasswordUpdater implements CustomerUpdater {
    @Override
    public int updateCustomer(Customer customer, Object patch) {
        if(patch!=null){
            String password=(String) patch;
            if(!password.isBlank() && password.length()>=5){
                customer.setPassword(password);
                return 1;
            }
        }
        return -1;
    }
}
