package com.itransition.lobach.renbook.controller;

import com.itransition.lobach.renbook.entity.User;
import com.itransition.lobach.renbook.service.UserDetailsServiceImpl;
import com.itransition.lobach.renbook.service.UserService;
import com.itransition.lobach.renbook.util.MessageManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.itransition.lobach.renbook.constants.MessageConstants.*;
import static com.itransition.lobach.renbook.constants.OtherConstants.*;
import static com.itransition.lobach.renbook.constants.PathConstants.*;
import static com.itransition.lobach.renbook.constants.Attributes.*;

@Controller
@RequestMapping(value = "/auth")
public class AuthenticationController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/signup")
    public String showSignUp() {
        return SIGNUP_URL;
    }

    @PostMapping("/signup")
    public String sighUp(@RequestParam String email,
                         @RequestParam String username,
                         @RequestParam String password,
                         @RequestParam String avatarUrl,
                         @RequestParam String birthday,
                         @RequestParam String description,
                         Model model) {
        User userFromDb = userService.findUserByUsername(username);

        if (userFromDb != null) {
            model.addAttribute(ERROR, MessageManager.getMessage(SIGNUP_USER_EXISTS));
            return SIGNUP_URL;
        }

        User user = null;
        try {
            Date birthDate = new SimpleDateFormat(DATE_PATTERN).parse(birthday);
            user = userService.saveUser(email, username, password, avatarUrl, birthDate, description);
        } catch (RuntimeException e) {
            model.addAttribute(ERROR, MessageManager.getMessage(SIGNUP_DEFAULT));
            return SIGNUP_URL;
        } catch (ParseException e) {
            model.addAttribute(ERROR, MessageManager.getMessage(SIGNUP_DATE));
            return SIGNUP_URL;
        }

        //todo: email confirmation (get rid of this peace down here)
        if (user == null) {
            model.addAttribute(ERROR, "signup successful, but failed to retrieve info");
            return SIGNUP_URL;
        }
        try {
            authenticateUser(username, password);
        } catch (RuntimeException e) {
            model.addAttribute(ERROR, MessageManager.getMessage(LOGIN_INVALID_PASSWORD));
            return LOGIN_REDIRECT_URL;
        }

        return createUrl(user.getUserInfo().getPreferredLanguage());
    }

    @GetMapping("/login")
    public String showLogin() {
        return LOGIN_URL;
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        Model model) {
        User user = userService.findUserByUsername(username);
        if (user == null) {
            model.addAttribute(ERROR, MessageManager.getMessage(LOGIN_USERNAME_NOT_EXISTS));
            return LOGIN_URL;
        }

        try {
            authenticateUser(username, password);
        } catch (RuntimeException e) {
            model.addAttribute(ERROR, MessageManager.getMessage(LOGIN_INVALID_PASSWORD));
            return LOGIN_URL;
        }

        return createUrl(user.getUserInfo().getPreferredLanguage());
    }

    private void authenticateUser(String username, String password) throws RuntimeException {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, password);
        authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(token);

        userService.updateLastCheckoutTime(username);
    }

    private String createUrl(String preferredLanguage) {
        StringBuilder urlBuilder = new StringBuilder(INDEX_REDIRECT);
        urlBuilder.append(LANG_PARAM);
        urlBuilder.append(preferredLanguage);
        return urlBuilder.toString();
    }
}
