package com.kwizera.utils;

public class InputValidationUtil {
    // validating names
    public static boolean invalidNames(String names) {
        return (!names.matches("[A-Za-z ]*") || names.length() < 2);
    }

    // validating email
    public static boolean invalidEmail(String email) {
        return (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"));
    }

    public static boolean invalidURLParam(String param) {
        return (!param.matches("[0-9]+"));
    }

    public static boolean invalidProjectTitle(String title) {
        return (!title.matches("^[a-zA-Z0-9 ._-]{3,100}$"));
    }

    public static boolean invalidProjectDescription(String description) {
        return (!description.matches("^[a-zA-Z0-9 .,!?:;'\"()\\-&%+/]{10,500}$"));
    }

    public static boolean invalidLocalDate(String date) {
        return (!date.matches("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$"));
    }

}
