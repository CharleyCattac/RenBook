package com.itransition.lobach.renbook.constants;

public class MessageConstants {

    private MessageConstants() {
        super();
    }

    public static final String SIGNUP_DEFAULT = "signup.error";
    public static final String SIGNUP_USER_EXISTS = "signup.error_exists";
    public static final String SIGNUP_DATE = "signup.error_date";

    public static final String LOGIN_USERNAME_NOT_EXISTS = "login.error_username";
    public static final String LOGIN_INVALID_PASSWORD = "login.error_password";

    public static final String FANDOM_TYPE_UNDEFINED = "work.fandom_type.undefined";

    public static final String INVALID_ACCESS = "You do not have the authority for this action!";
}
