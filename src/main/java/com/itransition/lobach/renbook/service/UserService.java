package com.itransition.lobach.renbook.service;

import com.itransition.lobach.renbook.builder.UserBuilder;
import com.itransition.lobach.renbook.entity.Role;
import com.itransition.lobach.renbook.entity.User;
import com.itransition.lobach.renbook.entity.UserInfo;
import com.itransition.lobach.renbook.exception.EmptyResultException;
import com.itransition.lobach.renbook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.itransition.lobach.renbook.constants.OtherConstants.DATE_PATTERN;
import static com.itransition.lobach.renbook.constants.OtherConstants.USERNAME_PATTERN;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User saveUser(String email, String username, String password,
                         String avatarUrl, Date birthDate, String description)
            throws EmptyResultException {

        User user = userRepository.findByUsername(username);
        if (user == null) {
            Set<Role> roles = new HashSet<>();
            roles.add(roleService.getUserRole());

            UserInfo userInfo = userInfoService.saveUserInfo(avatarUrl, birthDate, description);
            if (userInfo == null) {
                throw new EmptyResultException("userInfo is null");
            }

            return saveToRepo(email, username, password, roles, userInfo);
        }
        return null;
    }

    public User saveAdmin(String email, String username, String password,
                         String avatarUrl, Date birthDate, String description)
            throws EmptyResultException {

        User user = userRepository.findByUsername(username);
        if (user == null) {
            Set<Role> roles = new HashSet<>();
            roles.add(roleService.getAdminRole());

            UserInfo userInfo = userInfoService.saveUserInfo(avatarUrl, birthDate, description);
            if (userInfo == null) {
                throw new EmptyResultException("userInfo is null");
            }

            return saveToRepo(email, username, password, roles, userInfo);
        }
        return null;
    }

    private User saveToRepo(String email, String username, String password,
                            Set<Role> roles, UserInfo userInfo) {
        if (email != null && username != null && username.matches(USERNAME_PATTERN)
                && password != null && userInfo != null) {
            User newUser = UserBuilder.buildUser(
                    email,
                    username,
                    passwordEncoder.encode(password),
                    roles,
                    System.currentTimeMillis(),
                    userInfo);

            return userRepository.save(newUser);
        } else {
            return null;
        }
    }

    public User updateUserInfo(User user, String email,
                               String avatarUrl, String birthday, String description,
                               String language, String theme) {
        if (user != null) {
            if (!user.getEmail().equals(email)) {
                user.setEmail(email);
            }
            Date birthDate;
            try {
                birthDate = new SimpleDateFormat(DATE_PATTERN).parse(birthday);
            }  catch (ParseException e) {
                return null;
            }
            UserInfo userInfo = user.getUserInfo();
            userInfo = userInfoService.saveUserInfo(userInfo, avatarUrl, birthDate, description, language, theme);
            if (userInfo != null) {
                user.setUserInfo(userInfo);
            }
            return userRepository.save(user);
        }
        return null;
    }

    public void blockUser(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            user.setBlocked(true);
            userRepository.save(user);
        }
    }

    public void unblockUser(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            user.setBlocked(false);
            userRepository.save(user);
        }
    }

    public void updateLastCheckoutTime(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            user.setMillisWhenLastLogin(System.currentTimeMillis());
            userRepository.save(user);
        }
    }

    public void changePassword(String username, String newPassword) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
        }
    }

    public void deleteUser(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            userRepository.delete(user);
            userInfoService.deleteUserInfo(user.getUserInfo());
        }
    }
}
