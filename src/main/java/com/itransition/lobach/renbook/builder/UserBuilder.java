package com.itransition.lobach.renbook.builder;

import com.itransition.lobach.renbook.entity.Role;
import com.itransition.lobach.renbook.entity.User;
import com.itransition.lobach.renbook.entity.UserInfo;

import java.util.Set;

public class UserBuilder {

    private UserBuilder() {
        super();
    }

    public static User buildUser(String email,
                                 String username,
                                 String password,
                                 Set<Role> roleSet,
                                 Long currentMillis,
                                 UserInfo userInfo) {
        return User.builder()
                .email(email)
                .username(username)
                .password(password)
                .roles(roleSet)
                .millisWhenCreated(currentMillis)
                .millisWhenLastLogin(currentMillis)
                .blocked(false)
                .userInfo(userInfo)
                .build();
    }
}
