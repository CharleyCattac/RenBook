package com.itransition.lobach.renbook.util;

import com.itransition.lobach.renbook.entity.Role;
import com.itransition.lobach.renbook.entity.User;
import com.itransition.lobach.renbook.service.RoleService;
import com.itransition.lobach.renbook.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class SecurityHelper {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    public User getLoggedUser() {
        org.springframework.security.core.userdetails.User author = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String loggedUserName = author.getUsername();
        return userService.findUserByUsername(loggedUserName);
    }

    public boolean isUserTheLoggedOne(String username) {
        return getLoggedUser().getUsername().equals(username);
    }

    public boolean isLoggedUserAdmin() {
        return getLoggedUser().getRoles().contains(roleService.getAdminRole());
    }

    public boolean isUserAdmin(User user) {
        for (Role role : user.getRoles()) {
            if (role.getName().equalsIgnoreCase("ADMIN")) {
                return true;
            }
        }
        return false;
    }
}
