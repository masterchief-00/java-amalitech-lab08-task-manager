package com.kwizera.utils;

public class InputValidationUtil {
    // validating names
    public static boolean invalidNames(String names) {
        return (!names.matches("[A-Za-z ]*") || names.length() < 2);
    }

    // validating email
    public static boolean invalidEmail(String email) {
        return (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$\n"));
    }

}
