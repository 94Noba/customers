package sn.optimizer.amigosFullStackCourse.customer.utilities.roleSuppliers;

import sn.optimizer.amigosFullStackCourse.customer.security.permission.Role;
import sn.optimizer.amigosFullStackCourse.customer.utilities.RoleSupplier;

public class CustomerSupplier implements RoleSupplier {
    @Override
    public Role get() {
        return Role.CUSTOMER;
    }
}
