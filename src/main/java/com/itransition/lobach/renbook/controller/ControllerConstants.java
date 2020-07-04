package com.itransition.lobach.renbook.controller;

public class ControllerConstants {

    private ControllerConstants() {
        super();
    }

    public static final String URL_INDEX = "index";
    public static final String URL_TABLE_READONLY = "table_readonly";
    public static final String URL_TABLE_EDIT = "table_edit";
    public static final String URL_TABLE_EDIT_REDIRECT = "redirect:/table_edit";
    public static final String URL_LOGIN = "login";
    public static final String URL_LOGIN_REDIRECT = "redirect:/login";
    public static final String URL_SIGNUP = "signup";

    public static final String ATTRIBUTE_LIST = "userList";
    public static final String ATTRIBUTE_MESSAGE = "message";
    public static final String ATTRIBUTE_ERROR = "error";

    public static final String ATTRIBUTE_MSG_ANON = "You have no right to modify here, anonym!";
    public static final String ATTRIBUTE_MSG_SIGNUP_SUCCESSFUL = "Success! Now you can log in.";
    public static final String ATTRIBUTE_MSG_LOGIN_SUCCESSFUL = "Success! Now you're allowed to see editable table.";
    public static final String ATTRIBUTE_MSG_LOGIN_REQUIRED = "You need to be logged in to do this!";
    public static final String ATTRIBUTE_MSG_BLOCKED = "You've been blocked! Wait until someone unblocks you.";
    public static final String ATTRIBUTE_MSG_LOGOUT = "You have been logged out.";

    public static final String ATTRIBUTE_ERR_DEFAULT = "Failed to perform requested action!";
    public static final String ATTRIBUTE_ERR_LOGIN = "Failed to login.";
    public static final String ATTRIBUTE_ERR_LOGIN_INVALID_DATA = "Invalid username or/and password.";
    public static final String ATTRIBUTE_ERR_SIGN_UP = "Failed to sign up.";
    public static final String ATTRIBUTE_ERR_SIGN_UP_USER_EXISTS = "User with such login already exists.";

}
