package com.itransition.lobach.renbook.constants;

public class PathConstants {

    private PathConstants() {
        super();
    }

    public static final String INDEX = "index";
    public static final String INDEX_REDIRECT = "redirect:/";

    public static final String LOGIN_URL = "auth/login";
    public static final String LOGIN_REDIRECT_URL = "redirect:/auth/login";
    public static final String SIGNUP_URL = "auth/signup";

    public static final String DUMMY = "#";
    public static final String ERROR_URL = "error";
    public static final String NOT_READY_URL = "not_ready";

    public static final String WORKS_URL = "works";
    public static final String WORK_VIEW_URL = "work/work_view";
    public static final String CHAPTER_VIEW_URL = "work/chapter_view";
    public static final String WORK_FANDOMS_URL = "work/fandoms";
    public static final String WORK_UPDATE_URL = "work/list";

    public static final String AUTHORS_URL = "authors";
    public static final String RULES_URL = "rules";

    public static final String HOME_WORKS = "home/my_works";
    public static final String HOME_WORKS_REDIRECT  = "redirect:/home/my_works";

    public static final String HOME_ADD_WORK = "home/add_work";
    public static final String HOME_EDIT_WORK = "home/edit_work";

    public static final String HOME_ADD_CHAPTER = "home/add_chapter";
    public static final String HOME_EDIT_CHAPTER = "home/edit_chapter";
}
