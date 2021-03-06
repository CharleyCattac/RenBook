package com.itransition.lobach.renbook.controller;

import com.itransition.lobach.renbook.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import static com.itransition.lobach.renbook.constants.PathConstants.*;
import static com.itransition.lobach.renbook.constants.Attributes.*;

import static com.itransition.lobach.renbook.util.EntityToDtoConverter.*;

@Controller
public class MainController {

    @Autowired
    private WorkService workService;

    @Autowired
    private ChapterService chapterService;

    @Autowired
    private FandomService fandomService;

    @Autowired
    private TagService tagService;

    @GetMapping(value = "/")
    public String showIndex(Model model) {
        model.addAttribute(TAGS, convertTagList(tagService.findTopFifteen()));
        model.addAttribute(TAGS_TOTAL, tagService.findAll().size());
        model.addAttribute(WORKS_COUNT, workService.countAllNonEmptyWorks());
        model.addAttribute(TOP_WORKS_BY_UPDATE, convertWorkBasicList(workService.findTopByLastUpdate()));
        model.addAttribute(TOP_WORKS_BY_ASSESS, convertWorkBasicList(workService.findTopByAssessment()));
        return INDEX;
    }

    @GetMapping(value = "/rules")
    public String showRules() {
        return RULES_URL;
    }
}
