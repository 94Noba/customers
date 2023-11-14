package sn.optimizer.amigosFullStackCourse.customer.security.permission;

public enum Permission {

    PUBLIC("permission:public"),
    PRIVATE("permission:private"),
    DELETE("permission:delete");

    private final String permission;

    Permission(String permission){
        this.permission=permission;
    }

    public String getPermission(){
        return this.permission;
    }
}
