package com.itransition.lobach.renbook.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import static com.itransition.lobach.renbook.constants.ControllerConstants.*;

@Controller
public class MainController {
    @GetMapping(value = "/")
    public String showIndex() {
        return URL_INDEX;
    }
}
