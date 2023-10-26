package sn.optimizer.amigosFullStackCourse.customer.utilities;

import sn.optimizer.amigosFullStackCourse.customer.Customer;

public class CustomerPasswordUpdater implements CustomerUpdater{
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
