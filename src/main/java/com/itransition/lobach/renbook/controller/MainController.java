package com.itransition.lobach.renbook.controller;

import com.itransition.lobach.renbook.entity.Chapter;
import com.itransition.lobach.renbook.entity.Work;
import com.itransition.lobach.renbook.enums.FandomType;
import com.itransition.lobach.renbook.enums.Language;
import com.itransition.lobach.renbook.service.*;
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
    private WorkService workService;

    @Autowired
    private ChapterService chapterService;

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

    @GetMapping(value = "/works/view/{workName}")
    public String showWork(@PathVariable("workName") String workName,
                           Model model) {
        Work work = workService.findByName(workName);
        if (work == null) {
            model.addAttribute(ERROR, ERROR);
            return WORKS;
        }
        model.addAttribute(VIEWED_WORK, convertWorkFull(work));
        return WORK_VIEW;
    }

    @GetMapping(value = "/works/view/{workName}/{chapterName}")
    public String showChapter(@PathVariable("workName") String workName,
                              @PathVariable("chapterName") String chapterName,
                              Model model) {
        Work work = workService.findByName(workName);
        Chapter chapter = chapterService.findByWorkAndName(work, chapterName);
        if (chapter == null) {
            model.addAttribute(ERROR, ERROR);
            return WORKS;
        }
        model.addAttribute(VIEWED_WORK, convertWorkBasic(work));
        model.addAttribute(VIEWED_CHAPTER, convertChapter(chapter));
        if (work.getContent().size() > 1) {
            int chapterIndex = work.getContent().indexOf(chapter);
            if (chapterIndex != 0) {
                model.addAttribute(PREV_CHAPTER, work.getContent().get(chapterIndex - 1).getName());
            }
            if (chapterIndex != work.getContent().size() - 1) {
                model.addAttribute(NEXT_CHAPTER, work.getContent().get(chapterIndex + 1).getName());
            }
        }
        return CHAPTER_VIEW;
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
