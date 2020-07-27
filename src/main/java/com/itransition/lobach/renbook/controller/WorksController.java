package com.itransition.lobach.renbook.controller;

import com.itransition.lobach.renbook.entity.*;
import com.itransition.lobach.renbook.enums.FandomType;
import com.itransition.lobach.renbook.service.*;
import com.itransition.lobach.renbook.util.EntityToDtoConverter;
import com.itransition.lobach.renbook.util.MessageManager;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

import static com.itransition.lobach.renbook.constants.PathConstants.*;
import static com.itransition.lobach.renbook.constants.Attributes.*;
import static com.itransition.lobach.renbook.constants.MessageConstants.*;
import static com.itransition.lobach.renbook.constants.OtherConstants.*;

import static com.itransition.lobach.renbook.util.EntityToDtoConverter.*;

@Controller
@RequestMapping(value = "/works")
public class WorksController {

    @Autowired
    private UserService userService;

    @Autowired
    private WorkService workService;

    @Autowired
    private ChapterService chapterService;

    @Autowired
    private FandomService fandomService;

    @Autowired
    private TagService tagService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private AssessmentService assessmentService;

    @GetMapping
    public String showWorks(Model model) {
        return WORKS_URL;
    }

    @GetMapping(value = "/update")
    public String showWorksByUpdate(Model model) {
        int pageNumInt = 0;
        Page<Work> workPage = workService.findAllByLastUpdate(pageNumInt);
        model.addAttribute(PAGE_COUNT, workPage.getTotalPages());
        model.addAttribute(CUR_PAGE,1);
        if (1 < workPage.getTotalPages()) {
            model.addAttribute(NEXT_PAGE,2);
        }
        List<Work> works = workPage.toList();
        model.addAttribute(ALL_WORKS, EntityToDtoConverter.convertWorkBasicList(works));
        model.addAttribute(WORKS_SORT_SUBJECT, "update");

        return WORK_LIST_URL;
    }

    @GetMapping(value = "/update/{pageNumber}")
    public String showWorksByUpdate(@PathVariable(name = "pageNumber") String pageNumber,
                                    Model model) {
        int pageNumInt = 1;
        if (pageNumber != null) {
            try {
                pageNumInt = Integer.parseInt(pageNumber);
            } catch (NumberFormatException ignored) {
            }
        }
        Page<Work> workPage = workService.findAllByLastUpdate(pageNumInt - 1);
        model.addAttribute(PAGE_COUNT, workPage.getTotalPages());
        model.addAttribute(CUR_PAGE,1);
        if (pageNumInt > 1) {
            model.addAttribute(PREV_PAGE,pageNumInt - 1);
        }
        model.addAttribute(CUR_PAGE, pageNumInt);
        if (pageNumInt < workPage.getTotalPages()) {
            model.addAttribute(NEXT_PAGE,pageNumInt + 1);
        }
        List<Work> works = workPage.toList();
        model.addAttribute(ALL_WORKS, EntityToDtoConverter.convertWorkBasicList(works));
        model.addAttribute(WORKS_SORT_SUBJECT, "update");

        return WORK_LIST_URL;
    }

    @GetMapping(value = "/popular")
    public String showWorksByAssessment(Model model) {
        int pageNumInt = 0;
        Page<Work> workPage = workService.findAllByAssessment(pageNumInt);
        model.addAttribute(PAGE_COUNT, workPage.getTotalPages());
        model.addAttribute(CUR_PAGE,1);
        if (1 < workPage.getTotalPages()) {
            model.addAttribute(NEXT_PAGE,2);
        }
        List<Work> works = workPage.toList();
        model.addAttribute(ALL_WORKS, EntityToDtoConverter.convertWorkBasicList(works));
        model.addAttribute(WORKS_SORT_SUBJECT, "popular");

        return WORK_LIST_URL;
    }

    @GetMapping(value = "/popular/{pageNumber}")
    public String showWorksByAssessment(@PathVariable(name = "pageNumber") String pageNumber,
                                    Model model) {
        int pageNumInt = 1;
        if (pageNumber != null) {
            try {
                pageNumInt = Integer.parseInt(pageNumber);
            } catch (NumberFormatException ignored) {
            }
        }
        Page<Work> workPage = workService.findAllByAssessment(pageNumInt - 1);
        model.addAttribute(PAGE_COUNT, workPage.getTotalPages());
        model.addAttribute(CUR_PAGE,1);
        if (pageNumInt > 1) {
            model.addAttribute(PREV_PAGE,pageNumInt - 1);
        }
        model.addAttribute(CUR_PAGE, pageNumInt);
        if (pageNumInt < workPage.getTotalPages()) {
            model.addAttribute(NEXT_PAGE,pageNumInt + 1);
        }
        List<Work> works = workPage.toList();
        model.addAttribute(ALL_WORKS, EntityToDtoConverter.convertWorkBasicList(works));
        model.addAttribute(WORKS_SORT_SUBJECT, "popular");

        return WORK_LIST_URL;
    }

    @GetMapping(value = "/{fandom_type}")
    public String showFandomsByFandomType(@PathVariable("fandom_type") String fandomType,
                                        Model model) {
        if (EnumUtils.isValidEnum(FandomType.class, fandomType.toUpperCase())) {
            if (fandomType.equalsIgnoreCase(FandomType.ORIGINAL.name())) {
                model.addAttribute(ERROR, ERROR);
                return WORKS_URL;
            } else {
                model.addAttribute(FANDOM_TYPE, MessageManager.getMessage(FANDOM_CODE + fandomType));
                model.addAttribute(FANDOMS, convertFandomList(fandomService.findAllByType(fandomType)));
                return WORK_FANDOMS_URL;
            }
        } else {
            model.addAttribute(ERROR, FANDOM_TYPE_UNDEFINED);
            return WORKS_URL;
        }
    }

    @GetMapping(value = "/tag/{tag}/{pageNum}")
    public String showWorksByTag(@PathVariable("tag") String tagName,
                                 @PathVariable("pageNum") String pageNumber,
                                 Model model) {
        int pageNumInt = 1;
        if (pageNumber != null) {
            try {
                pageNumInt = Integer.parseInt(pageNumber);
            } catch (NumberFormatException ignored) {
            }
        }
        List<Work> works;
        double pageCount;
        if (!tagName.contains("/")) {
            Tag tag = tagService.findByName(tagName);
            if (tag == null) {
                model.addAttribute(ERROR, ERROR);
                return INDEX_REDIRECT;
            }
            works = tagService.findWorksByTagName(tag, pageNumInt - 1);
            pageCount = (double) tag.getWorks().size() / WORKS_PER_PAGE;
            model.addAttribute(WORKS_SORT_SUBJECT, tag.getName());
        } else {
            Page<Work> workPage = workService.findAllByLastUpdate(pageNumInt);
            works = workPage.toList();
            pageCount = workPage.getTotalPages();
            model.addAttribute(WORKS_SORT_SUBJECT, "update");
            model.addAttribute(ERROR, ERROR);
        }
        model.addAttribute(ALL_WORKS, EntityToDtoConverter.convertWorkBasicList(works));
        int totalPages = pageCount - (int) pageCount > 0.01 ? (int) pageCount + 1 : (int) pageCount;
        model.addAttribute(PAGE_COUNT, totalPages);
        model.addAttribute(CUR_PAGE,1);
        if (pageNumInt > 1) {
            model.addAttribute(PREV_PAGE,pageNumInt - 1);
        }
        model.addAttribute(CUR_PAGE, pageNumInt);
        if (pageNumInt < totalPages) {
            model.addAttribute(NEXT_PAGE,pageNumInt + 1);
        }

        return WORK_LIST_URL;
    }

    @GetMapping(value = "/view/{workName}")
    public String showWork(@PathVariable("workName") String workName,
                           @AuthenticationPrincipal Principal principal,
                           Model model) {
        Work work = workService.findByName(workName);
        if (work == null) {
            model.addAttribute(ERROR, ERROR);
            return WORKS_URL;
        }
        commentService.reorderComments(work);
        model.addAttribute(VIEWED_WORK, convertWorkFull(work));
        if (principal != null) {
            User loggedUser = userService.findUserByUsername(principal.getName());
            Assessment assessment = assessmentService.findByUserAndWork(loggedUser, work);
            if (assessment != null) {
                model.addAttribute(USER_ASSESSMENT, assessment.getValue());
            }
        }
        return WORK_VIEW_URL;
    }

    @GetMapping(value = "/view/{workName}/{chapterName}")
    public String showChapter(@PathVariable("workName") String workName,
                              @PathVariable("chapterName") String chapterName,
                              Model model) {
        Work work = workService.findByName(workName);
        Chapter chapter = chapterService.findByWorkAndName(work, chapterName);
        if (chapter == null) {
            model.addAttribute(ERROR, ERROR);
            return WORKS_URL;
        }
        commentService.reorderComments(work);
        model.addAttribute(VIEWED_WORK, convertWorkFull(work));
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
        return CHAPTER_VIEW_URL;
    }
}
