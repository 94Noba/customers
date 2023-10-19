package sn.optimizer.amigosFullStackCourse.customer.validator;

import java.util.regex.Pattern;

public class EmailValidator {

    private static final String regex="^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    public static boolean validate(String email){
        return Pattern.compile(regex)
                .matcher(email)
                .matches();
    }
}
