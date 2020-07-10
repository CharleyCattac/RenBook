package com.itransition.lobach.renbook.controller;

import com.itransition.lobach.renbook.entity.User;
import com.itransition.lobach.renbook.exception.EmptyResultException;
import com.itransition.lobach.renbook.service.UserDetailsServiceImpl;
import com.itransition.lobach.renbook.service.UserService;
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

import static com.itransition.lobach.renbook.constants.ControllerConstants.*;

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
        return URL_SIGNUP;
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
            model.addAttribute(ATTR_ERROR, "user exists");
            return URL_SIGNUP;
        }

        User user = null;
        try {
            Date birthDate = new SimpleDateFormat("yyyy-MM-dd").parse(birthday);
            user = userService.saveAdmin(email, username, password, avatarUrl, birthDate, description);
        } catch (EmptyResultException e) {
            model.addAttribute(ATTR_ERROR, "fucking " + e.getMessage());
            return URL_SIGNUP;
        } catch (RuntimeException e) {
            model.addAttribute(ATTR_ERROR, "error during signup");
            return URL_SIGNUP;
        } catch (ParseException e) {
            model.addAttribute(ATTR_ERROR, "invalid birth date");
            return URL_SIGNUP;
        }

        if (user == null) {
            model.addAttribute(ATTR_ERROR, "signup successful, but failed to retrieve info");
            return URL_SIGNUP;
        } //todo: email confirmation (get rid of this peace down here)
        try {
            authenticateUser(username, password);
        } catch (RuntimeException e) {
            model.addAttribute(ATTR_ERROR, "invalid password");
            return URL_LOGIN_REDIRECT;
        }

        return URL_INDEX_REDIRECT;
    }

    @GetMapping("/login")
    public String showLogin() {
        return URL_LOGIN;
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        Model model) {
        User user = userService.findUserByUsername(username);
        if (user == null) {
            model.addAttribute(ATTR_ERROR, "user with such username does not exist");
            return URL_LOGIN;
        }

        try {
            authenticateUser(username, password);
        } catch (RuntimeException e) {
            model.addAttribute(ATTR_ERROR, "invalid password");
            return URL_LOGIN;
        }

        return URL_INDEX_REDIRECT;
    }

    private void authenticateUser(String username, String password) throws RuntimeException {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, password);
        authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(token);

        userService.updateLastCheckoutTime(username);
    }
}
