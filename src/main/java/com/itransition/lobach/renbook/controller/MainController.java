package com.itransition.lobach.renbook.controller;

import com.itransition.lobach.renbook.enums.FandomType;
import com.itransition.lobach.renbook.enums.Language;
import com.itransition.lobach.renbook.service.FandomService;
import com.itransition.lobach.renbook.service.TagService;
import com.itransition.lobach.renbook.service.UserService;
import com.itransition.lobach.renbook.util.MessageManager;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import static com.itransition.lobach.renbook.constants.PathConstants.*;
import static com.itransition.lobach.renbook.constants.Attributes.*;
import static com.itransition.lobach.renbook.constants.MessageConstants.*;
import static com.itransition.lobach.renbook.constants.OtherConstants.*;

import static com.itransition.lobach.renbook.util.EntityToDtoConverter.*;

@Controller
public class MainController {

    @Autowired
    private UserService userService;

    @Autowired
    private FandomService fandomService;

    @Autowired
    private TagService tagService;

    @GetMapping(value = "/")
    public String showIndex() {
        return INDEX;
    }

    @GetMapping(value = "/works")
    public String showWorks(Model model) {
        model.addAttribute(TAGS, convertTagList(tagService.findTopFifteen()));
        model.addAttribute(TAGS_TOTAL, tagService.findAll().size());
        return WORKS;
    }

    @GetMapping(value = "/works/{fandom_type}")
    public String showWorksByFandomType(@PathVariable("fandom_type") String fandomType,
                                        Model model) {
        if (EnumUtils.isValidEnum(FandomType.class, fandomType.toUpperCase())) {
            model.addAttribute(FANDOM_TYPE, MessageManager.getMessage(FANDOM_CODE + fandomType));
            model.addAttribute(FANDOMS, convertFandomList(fandomService.findAllByType(fandomType)));
            return FANDOMS_BY_TYPE;
        } else {
            model.addAttribute(ERROR, FANDOM_TYPE_UNDEFINED);
            return WORKS;
        }
    }

    @GetMapping(value = "/authors")
    public String showAuthors() {

        return AUTHORS;
    }

    @GetMapping(value = "/rules")
    public String showRules() {
        return RULES;
    }
}
