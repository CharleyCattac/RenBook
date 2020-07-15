package com.itransition.lobach.renbook.controller;

import com.itransition.lobach.renbook.entity.User;
import com.itransition.lobach.renbook.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/users")
    public List<User> getUsers(Model model) {
        return userService.findAll();
    }
}
