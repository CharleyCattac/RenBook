package com.itransition.lobach.renbook.builder;

import com.itransition.lobach.renbook.entity.UserInfo;

import java.util.Date;

public class UserInfoBuilder {

    private UserInfoBuilder() {
        super();
    }

    public static UserInfo buildUserInfo(String avatarUrl,
                                         Date birthDate,
                                         String description,
                                         String language,
                                         String theme) {
        return UserInfo.builder()
                .avatarUrl(avatarUrl)
                .birthDate(birthDate)
                .description(description)
                .preferredLanguage(language)
                .preferredTheme(theme)
                .build();
    }
}
