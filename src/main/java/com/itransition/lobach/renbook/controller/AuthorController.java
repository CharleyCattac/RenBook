package com.itransition.lobach.renbook.controller;

import com.itransition.lobach.renbook.entity.User;
import com.itransition.lobach.renbook.entity.Work;
import com.itransition.lobach.renbook.service.UserService;
import com.itransition.lobach.renbook.service.WorkService;
import com.itransition.lobach.renbook.util.EntityToDtoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import static com.itransition.lobach.renbook.constants.Attributes.*;
import static com.itransition.lobach.renbook.constants.PathConstants.*;

@Controller
@RequestMapping(value = "/authors")
public class AuthorController {

    @Autowired
    private UserService userService;

    @Autowired
    private WorkService workService;

    @GetMapping
    public String showAuthors() {
        return AUTHORS_URL;
    }

    @GetMapping(value = "/{username}")
    public String getAuthorProfile(@PathVariable(name = "username") String username,
                                   Model model) {
        User author = userService.findUserByUsername(username);
        if (author == null) {
            model.addAttribute(ERROR, ERROR);
            return AUTHORS_URL;
        }
        int pageNumInt = 0;
        Page<Work> workPage = workService.findAllNonemptyByAuthor(author, pageNumInt);
        if (workPage == null) {
            model.addAttribute(ERROR, ERROR);
            return AUTHORS_URL;
        }
        List<Work> works = workPage.toList();
        model.addAttribute(VIEWED_USER, EntityToDtoConverter.convertViewUser(author, works));
        model.addAttribute(PAGE_COUNT, workPage.getTotalPages());
        model.addAttribute(CUR_PAGE,1);
        if (1 < workPage.getTotalPages()) {
            model.addAttribute(NEXT_PAGE,2);
        }
        return USER_PROFILE;
    }

    @GetMapping(value = "/{username}/{pageNumber}")
    public String getMyProfile(@PathVariable(name = "username") String username,
                               @PathVariable(name = "pageNumber") String pageNumber,
                               Model model) {
        User author = userService.findUserByUsername(username);
        if (author == null) {
            model.addAttribute(ERROR, ERROR);
            return AUTHORS_URL;
        }
        int pageNumInt = 1;
        if (pageNumber != null) {
            try {
                pageNumInt = Integer.parseInt(pageNumber);
            } catch (NumberFormatException ignored) {
            }
        }
        Page<Work> workPage = workService.findAllByAuthor(author, pageNumInt - 1);
        if (workPage == null) {
            model.addAttribute(ERROR, ERROR);
            return USER_PROFILE;
        }
        List<Work> works = workPage.toList();
        model.addAttribute(VIEWED_USER, EntityToDtoConverter.convertViewUser(author, works));

        model.addAttribute(PAGE_COUNT, workPage.getTotalPages());
        if (pageNumInt > 1) {
            model.addAttribute(PREV_PAGE,pageNumInt - 1);
        }
        model.addAttribute(CUR_PAGE, pageNumInt);
        if (pageNumInt < workPage.getTotalPages()) {
            model.addAttribute(NEXT_PAGE,pageNumInt + 1);
        }
        return HOME_WORKS;
    }
}
