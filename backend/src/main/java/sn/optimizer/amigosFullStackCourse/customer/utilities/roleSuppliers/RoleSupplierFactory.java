package sn.optimizer.amigosFullStackCourse.customer.utilities.roleSuppliers;

import sn.optimizer.amigosFullStackCourse.customer.utilities.RoleSupplier;

public class RoleSupplierFactory {

    public static RoleSupplier of(String supplierType){
        return switch (supplierType){
            case "customer" -> new CustomerSupplier();
            case "vip"-> new VipSupplier();
            default -> null;
        };
    }
}
