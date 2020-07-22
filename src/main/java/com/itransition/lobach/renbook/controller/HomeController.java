package com.itransition.lobach.renbook.controller;

import com.itransition.lobach.renbook.entity.Chapter;
import com.itransition.lobach.renbook.entity.User;
import com.itransition.lobach.renbook.entity.Work;
import com.itransition.lobach.renbook.service.ChapterService;
import com.itransition.lobach.renbook.service.FandomService;
import com.itransition.lobach.renbook.service.TagService;
import com.itransition.lobach.renbook.service.WorkService;
import com.itransition.lobach.renbook.util.EntityToDtoConverter;
import com.itransition.lobach.renbook.util.SecurityHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.itransition.lobach.renbook.constants.PathConstants.*;
import static com.itransition.lobach.renbook.constants.Attributes.*;
import static com.itransition.lobach.renbook.constants.MessageConstants.*;

@Controller
@RequestMapping(value = "/home")
public class HomeController {

    @Autowired
    private SecurityHelper securityHelper;

    @Autowired
    private WorkService workService;

    @Autowired
    private ChapterService chapterService;

    @Autowired
    private FandomService fandomService;

    @Autowired
    private TagService tagService;

    @GetMapping(value = "/news")
    public String getNews(Model model) {
        return NOT_READY_URL;
    }

    @GetMapping(value = "/profile")
    public String getMyProfile(Model model) {
        return NOT_READY_URL;
    }

    @GetMapping(value = "/works")
    public String getMyWorks(Model model) {
        int pageNumInt = 0;
        User author = securityHelper.getLoggedUser();
        Page<Work> workPage = workService.findAllByAuthor(author, pageNumInt);
        model.addAttribute(PAGE_COUNT, workPage.getTotalPages());
        model.addAttribute(CUR_PAGE,1);
        if (1 < workPage.getTotalPages()) {
            model.addAttribute(NEXT_PAGE,2);
        }
        List<Work> works = workPage.toList();
        model.addAttribute(MY_WORKS, EntityToDtoConverter.convertWorkBasicList(works));

        return HOME_WORKS;
    }

    @GetMapping(value = "/works/{pageNumber}")
    public String getMyWorks(@PathVariable(name = "pageNumber") String pageNumber,
                             Model model) {
        int pageNumInt = 1;
        if (pageNumber != null) {
            try {
                pageNumInt = Integer.parseInt(pageNumber);
            } catch (NumberFormatException ignored) {
            }
        }
        User author = securityHelper.getLoggedUser();
        Page<Work> workPage = workService.findAllByAuthor(author, pageNumInt - 1);
        model.addAttribute(PAGE_COUNT, workPage.getTotalPages());
        if (pageNumInt > 1) {
            model.addAttribute(PREV_PAGE,pageNumInt - 1);
        }
        model.addAttribute(CUR_PAGE, pageNumInt);
        if (pageNumInt < workPage.getTotalPages()) {
            model.addAttribute(NEXT_PAGE,pageNumInt + 1);
        }

        List<Work> works = workPage.toList();
        model.addAttribute(MY_WORKS, EntityToDtoConverter.convertWorkBasicList(works));

        return HOME_WORKS;
    }

    @GetMapping(value = "/works/add")
    public String showAddWork(Model model) {
        model.addAttribute(TAGS, EntityToDtoConverter.convertTagList(tagService.findAll()));
        model.addAttribute(FANDOMS, EntityToDtoConverter.convertFandomList(fandomService.findAll()));
        return HOME_ADD_WORK;
    }

    @PostMapping(value = "/works/add")
    public String performAddWork(@RequestParam(name = "name") String name,
                                 @RequestParam(name = "workType") String workType,
                                 @RequestParam(name = "fandoms") List<String> fandoms,
                                 @RequestParam(name = "fandomTypes") List<String> fandomsTypes,
                                 @RequestParam(name = "rating") String rating,
                                 @RequestParam(name = "category") String category,
                                 @RequestParam(name = "language") String language,
                                 @RequestParam(name = "tags") List<String> tags,
                                 @RequestParam(name = "description") String description,
                                 @RequestParam(name = "comment") String comment,
                                 Model model) {
        User author = securityHelper.getLoggedUser();
        Work newWork = workService.addNewWork(author, name, workType, fandoms, fandomsTypes, rating, category, language, tags, description, comment);
        if (newWork == null) {
            model.addAttribute(ERROR, ERROR);
            return HOME_ADD_WORK;
        }
        return INDEX_REDIRECT; //todo fix redirects
    }

    //todo CREATE KEYS TO PUT IN URL INSTEAD OF NAMES

    @GetMapping(value = "/works/{workName}/edit")
    public String showEditWork(@PathVariable(name = "workName") String workName,
                               Model model) {
        Work work = workService.findByName(workName);
        if (work == null) {
            model.addAttribute(ERROR, ERROR);
            return HOME_WORKS;
        }
        if (!securityHelper.isUserTheLoggedOne(work.getAuthor().getUsername())
                && !securityHelper.isLoggedUserAdmin()) {
            model.addAttribute(ERROR, INVALID_ACCESS);
            return ERROR_URL;
        }

        model.addAttribute(EDITABLE_WORK, EntityToDtoConverter.convertWorkBasic(work));
        model.addAttribute(TAGS, EntityToDtoConverter.convertTagList(tagService.findAll()));
        model.addAttribute(FANDOMS, EntityToDtoConverter.convertFandomList(fandomService.findAll()));
        return HOME_EDIT_WORK;
    }

    @PostMapping(value = "/works/{name}/edit")
    public String performEditWork(@PathVariable(name = "name") String workName,
                                  @RequestParam(name = "workType") String workType,
                                  @RequestParam(name = "fandoms") List<String> fandoms,
                                  @RequestParam(name = "fandomTypes") List<String> fandomsTypes,
                                  @RequestParam(name = "rating") String rating,
                                  @RequestParam(name = "category") String category,
                                  @RequestParam(name = "status") String status,
                                  @RequestParam(name = "language") String language,
                                  @RequestParam(name = "tags") List<String> tags,
                                  @RequestParam(name = "description") String description,
                                  @RequestParam(name = "comment") String comment,
                                  Model model) {
        Work work = workService.findByName(workName);
        if (work == null) {
            model.addAttribute(EDITABLE_WORK, EntityToDtoConverter.convertWorkBasic(workService.findByName(workName)));
            model.addAttribute(TAGS, EntityToDtoConverter.convertTagList(tagService.findAll()));
            model.addAttribute(FANDOMS, EntityToDtoConverter.convertFandomList(fandomService.findAll()));

            model.addAttribute(ERROR, ERROR);
            return HOME_EDIT_WORK;
        }
        if (!securityHelper.isUserTheLoggedOne(work.getAuthor().getUsername())
                && !securityHelper.isLoggedUserAdmin()) {
            model.addAttribute(ERROR, INVALID_ACCESS);
            return ERROR_URL;
        }

        User author = securityHelper.getLoggedUser();
        work = workService.updateWork(author, work, workName, workType, fandoms, fandomsTypes, rating, category, status, language, tags, description, comment);
        if (work == null) {
            model.addAttribute(EDITABLE_WORK, EntityToDtoConverter.convertWorkBasic(workService.findByName(workName)));
            model.addAttribute(TAGS, EntityToDtoConverter.convertTagList(tagService.findAll()));
            model.addAttribute(FANDOMS, EntityToDtoConverter.convertFandomList(fandomService.findAll()));

            model.addAttribute(ERROR, ERROR);
            return HOME_EDIT_WORK;
        }

        return INDEX_REDIRECT; //todo fix redirects
    }

    @PostMapping(value = "/works/{workName}/delete")
    public String deleteWork(@PathVariable(name = "workName") String workName,
                             Model model) {
        Work work = workService.findByName(workName);
        if (work == null) {
            model.addAttribute(ERROR, ERROR);
            return HOME_WORKS;
        }
        if (!securityHelper.isUserTheLoggedOne(work.getAuthor().getUsername())
                && !securityHelper.isLoggedUserAdmin()) {
            model.addAttribute(ERROR, INVALID_ACCESS);
            return ERROR_URL;
        }

        workService.deleteWork(work);
        return INDEX_REDIRECT; //todo fix redirects
    }

    @GetMapping(value = "/works/{workName}/add")
    public String showAddChapter(@PathVariable(name = "workName") String name,
                                 Model model) {
        Work work = workService.findByName(name);
        if (work == null) {
            model.addAttribute(ERROR, ERROR);
            return HOME_WORKS;
        }
        if (!securityHelper.isUserTheLoggedOne(work.getAuthor().getUsername())
                && !securityHelper.isLoggedUserAdmin()) {
            model.addAttribute(ERROR, INVALID_ACCESS);
            return ERROR_URL;
        }

        model.addAttribute(WORK_NAME, work.getName());
        model.addAttribute(CHAPTERS_COUNT, work.getContent().size());
        return HOME_ADD_CHAPTER;
    }

    @PostMapping(value = "/works/{workName}/add")
    public String performAddChapter(@PathVariable(name = "workName") String workName,
                                    @RequestParam(name = "chapterName") String chapterName,
                                    @RequestParam(name = "chapterText") String chapterText,
                                    @RequestParam(name = "notes") String notes,
                                    @RequestParam(name = "status") String status,
                                    Model model) {
        Work work = workService.findByName(workName);
        if (work == null) {
            model.addAttribute(ERROR, ERROR);
            return HOME_WORKS;
        }
        if (!securityHelper.isUserTheLoggedOne(work.getAuthor().getUsername())
                && !securityHelper.isLoggedUserAdmin()) {
            model.addAttribute(ERROR, INVALID_ACCESS);
            return ERROR_URL;
        }

        Chapter newChapter = chapterService.addNewChapter(work, chapterName, chapterText, notes);
        if (newChapter == null) {
            model.addAttribute(ERROR, ERROR);
            return HOME_ADD_CHAPTER;
        }
        workService.addNewChapter(work, newChapter);
        workService.updateStatus(work, status);

        return INDEX_REDIRECT; //todo fix redirects
    }

    @GetMapping(value = "/works/{workName}/{chapterName}/edit")
    public String showEditChapter(@PathVariable(name = "workName") String workName,
                                  @PathVariable(name = "chapterName") String chapterName,
                                  Model model) {
        Work work = workService.findByName(workName);
        if (work == null) {
            model.addAttribute(ERROR, ERROR);
            return HOME_WORKS;
        }
        if (!securityHelper.isUserTheLoggedOne(work.getAuthor().getUsername())
                && !securityHelper.isLoggedUserAdmin()) {
            model.addAttribute(ERROR, INVALID_ACCESS);
            return ERROR_URL;
        }

        Chapter chapter = chapterService.findByWorkAndName(work, chapterName);
        if (chapter == null) {
            model.addAttribute(ERROR, ERROR);
            return HOME_WORKS;
        }
        model.addAttribute(EDITABLE_CHAPTER, EntityToDtoConverter.convertChapter(chapter));

        return HOME_EDIT_CHAPTER;
    }

    @PostMapping(value = "/works/{workName}/{chapterName}/edit")
    public String performEditChapter(@PathVariable(name = "workName") String workName,
                                     @PathVariable(name = "chapterName") String chapterName,
                                     @RequestParam(name = "chapterText") String chapterText,
                                     @RequestParam(name = "notes") String notes,
                                     Model model) {
        Work work = workService.findByName(workName);
        if (work == null) {
            model.addAttribute(ERROR, ERROR);
            return HOME_WORKS;
        }
        if (!securityHelper.isUserTheLoggedOne(work.getAuthor().getUsername())
                && !securityHelper.isLoggedUserAdmin()) {
            model.addAttribute(ERROR, INVALID_ACCESS);
            return ERROR_URL;
        }

        Chapter chapter = chapterService.findByWorkAndName(work, chapterName);
        if (chapter == null) {
            model.addAttribute(ERROR, ERROR);
            return HOME_WORKS;
        }
        chapter = chapterService.updateChapter(chapter, chapterName, chapterText, notes);
        if (chapter == null) {
            model.addAttribute(ERROR, ERROR);
            return HOME_EDIT_CHAPTER;
        }

        return INDEX_REDIRECT; //todo fix redirects
    }

    @PostMapping(value = "/works/{workName}/{chapterName}/delete")
    public String deleteChapter(@PathVariable(name = "workName") String workName,
                                @PathVariable(name = "chapterName") String chapterName,
                                Model model) {
        Work work = workService.findByName(workName);
        if (work == null) {
            model.addAttribute(ERROR, ERROR);
            return HOME_WORKS;
        }
        if (!securityHelper.isUserTheLoggedOne(work.getAuthor().getUsername())
                && !securityHelper.isLoggedUserAdmin()) {
            model.addAttribute(ERROR, INVALID_ACCESS);
            return ERROR_URL;
        }

        Chapter chapter = chapterService.findByWorkAndName(work, chapterName);
        if (chapter == null) {
            model.addAttribute(ERROR, ERROR);
            return HOME_WORKS;
        }
        chapterService.deleteChapter(chapter);

        return INDEX_REDIRECT; //todo fix redirects
    }

    @GetMapping(value = "/saved")
    public String getSaved(Model model) {
        return NOT_READY_URL;
    }
}
