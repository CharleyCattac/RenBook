package com.itransition.lobach.renbook.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.itransition.lobach.renbook.constants.PathConstants.*;
import static com.itransition.lobach.renbook.constants.Attributes.*;
import static com.itransition.lobach.renbook.constants.MessageConstants.*;
import static com.itransition.lobach.renbook.constants.OtherConstants.*;

@Controller
@RequestMapping(value = "/home")
public class HomeController {

    @GetMapping(value = "/news")
    public String getNews(Model model) {
        return HOME_WORKS;
    }

    @GetMapping(value = "/profile")
    public String getMyProfile(Model model) {
        return HOME_WORKS;
    }

    @GetMapping(value = "/works")
    public String getMyWorks(Model model) {
        return HOME_WORKS;
    }

    @GetMapping(value = "/saved")
    public String getSaved(Model model) {
        return HOME_WORKS;
    }

    @GetMapping(value = "/works/add_work")
    public String getAddWork(Model model) {
        return HOME_ADD_WORK;
    }

    @PostMapping(value = "/works/add_work")
    public String postAddWork(Model model) {
        return HOME_WORKS_REDIRECT;
    }
}
