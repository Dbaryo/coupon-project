package com.david.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// checking input validation (taken from your project)
public class InputValidationUtils {

    private final static String PASSWORD_REGEX = "[a-zA-Z0-9]{4,12}";
    private final static String EMAIL_REGEX = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
    private final static String NAME_REGEX = "[a-zA-Z ,.'-]{2,}";
    //changed date regex to suite yyyy-mm-dd
    private final static String DATE_REGEX = "^((19|20)\\d\\d)-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])$";


    // --------------------------------Password validation-----------------------------------

    public static boolean isPasswordValid (String password) {

        return password.matches(PASSWORD_REGEX);
    }

    //----------------------------------------------------------------------------------------

    // --------------------------------Email validation---------------------------------------

    public static boolean isEmailValid (String email) {

        // This line converts the regex code into the desired pattern
        Pattern emailPattern = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);

        //This line checks if the inserted email follows the previously created pattern
        Matcher matcher = emailPattern.matcher(email);

        return matcher.find();
    }

    // --------------------------------Name validation---------------------------------------

    public static boolean isNameValid (String name) {

        return name.matches(NAME_REGEX);
    }

    // --------------------------------Date validation---------------------------------------

    public static boolean isDateValid (String date) {

        return date.matches(DATE_REGEX);
    }

    //----------------------------------------------------------------------------------------
}