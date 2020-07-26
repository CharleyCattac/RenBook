package com.itransition.lobach.renbook.constants;

public class OtherConstants {

    private OtherConstants() {
        super();
    }

    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String TAG_PATTERN = "^[a-zA-Zа-яА-Я0-9()& ]+"; //^[a-zA-Z0-9()& ]*$ //[a-zA-Z]+

    public static final String LANG_PARAM = "?lang=";

    public static final String FANDOM_CODE = "work.category.types.";

    public static final Integer WORKS_PER_PAGE = 10;

    public static final String SORT_PARAM_LAST_UPDATE = "lastUpdateMillis";

    public static final String WORK_VIEW_RESPONSE_BASE = "/works/view/";
}
