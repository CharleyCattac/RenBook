package com.itransition.lobach.renbook.service;

import com.itransition.lobach.renbook.builder.UserInfoBuilder;
import com.itransition.lobach.renbook.enums.Language;
import com.itransition.lobach.renbook.enums.Theme;
import com.itransition.lobach.renbook.entity.UserInfo;
import com.itransition.lobach.renbook.repository.UserInfoRepository;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserInfoService {

    private static final String DEFAULT_LANGUAGE = Language.EN.name();
    private static final String DEFAULT_THEME = Theme.LIGHT.name();

    @Autowired
    private UserInfoRepository userInfoRepository;

    public UserInfo getById(Long id) {
        return userInfoRepository.getOne(id);
    }

    public UserInfo saveUserInfo(String avatarUrl,
                                 Date birthDate,
                                 String description) {
        return saveUserInfo(null,
                avatarUrl,
                birthDate,
                description,
                DEFAULT_LANGUAGE,
                DEFAULT_THEME);
    }

    public UserInfo saveUserInfo(UserInfo userInfo,
                                 String avatarUrl,
                                 Date birthDate,
                                 String description,
                                 String language,
                                 String theme) {
        if (avatarUrl == null
                || birthDate == null
                || language == null
                || theme == null) {
            return null;
        } else if (!birthDate.before(new Date())
                || !EnumUtils.isValidEnum(Language.class, language.toUpperCase())
                || !EnumUtils.isValidEnum(Theme.class, theme.toUpperCase())) {
            return null;
        } else {
            if (userInfo == null) {
                userInfo = UserInfoBuilder.buildUserInfo(
                        avatarUrl,
                        birthDate,
                        description,
                        language.toUpperCase(),
                        theme.toUpperCase()
                );
            } else {
                userInfo.setAvatarUrl(avatarUrl);
                userInfo.setBirthDate(birthDate);
                userInfo.setDescription(description);
                userInfo.setPreferredLanguage(language);
                userInfo.setPreferredTheme(theme);
            }
            return userInfoRepository.save(userInfo);
        }
    }

    public void deleteUserInfo(UserInfo userInfo) {
        if (userInfo != null) {
            userInfoRepository.delete(userInfo);
        }
    }
}
