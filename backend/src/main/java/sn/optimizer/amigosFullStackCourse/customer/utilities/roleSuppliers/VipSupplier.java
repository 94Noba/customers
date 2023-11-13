package sn.optimizer.amigosFullStackCourse.customer.utilities.roleSuppliers;

import sn.optimizer.amigosFullStackCourse.customer.security.permission.Role;
import sn.optimizer.amigosFullStackCourse.customer.utilities.RoleSupplier;

public class VipSupplier implements RoleSupplier {
    @Override
    public Role get() {
        return Role.VIP;
    }
}
