package sn.optimizer.amigosFullStackCourse.customer.utilities;

import sn.optimizer.amigosFullStackCourse.customer.Customer;
import sn.optimizer.amigosFullStackCourse.customer.validator.EmailValidator;

public class CustomerEmailUpdater implements CustomerUpdater{
    @Override
    public int updateCustomer(Customer customer, Object patch) {
        if (patch!=null && EmailValidator.validate((String)patch)){
            customer.setEmail(String.valueOf(patch));
            return 1;
        }
        return -1;
    }
}
