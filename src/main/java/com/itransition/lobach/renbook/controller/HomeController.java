package com.itransition.lobach.renbook.controller;

import com.itransition.lobach.renbook.entity.Chapter;
import com.itransition.lobach.renbook.entity.Work;
import com.itransition.lobach.renbook.service.ChapterService;
import com.itransition.lobach.renbook.service.WorkService;
import com.itransition.lobach.renbook.util.EntityToDtoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.itransition.lobach.renbook.constants.PathConstants.*;
import static com.itransition.lobach.renbook.constants.Attributes.*;

@Controller
@RequestMapping(value = "/home")
public class HomeController {

    @Autowired
    private WorkService workService;

    @Autowired
    private ChapterService chapterService;

    @GetMapping(value = "/news")
    public String getNews(Model model) {
        return HOME_WORKS;
    }

    @GetMapping(value = "/profile")
    public String getMyProfile(Model model) {
        return HOME_WORKS;
    }

    @GetMapping(value = "/works")
    public String getMyWorks(String pageNumber,
                             Model model) {
        int pageNumInt = 0;
        if (pageNumber != null) {
            try {
                pageNumInt = Integer.parseInt(pageNumber);
            } catch (NumberFormatException ignored) {
            }
        }
        Page<Work> workPage = workService.findAllOwnWorks(pageNumInt);
        model.addAttribute(PAGE_COUNT, workPage.getTotalPages());

        List<Work> works = workPage.toList();
        model.addAttribute(MY_WORKS, EntityToDtoConverter.convertWorkBasicList(works));

        return HOME_WORKS;
    }

    @GetMapping(value = "/saved")
    public String getSaved(Model model) {
        return HOME_WORKS;
    }

    @GetMapping(value = "/works/add")
    public String getAddWork(Model model) {
        return HOME_ADD_WORK;
    }

    @PostMapping(value = "/works/add")
    public String performAddWork(@RequestParam(name = "name") String name,
                              @RequestParam(name = "fandomType") String fandomType,
                              @RequestParam(name = "rating") String rating,
                              @RequestParam(name = "category") String category,
                              @RequestParam(name = "language") String language,
                              @RequestParam(name = "description") String description,
                              @RequestParam(name = "comment") String comment,
                              Model model) {
        Work newWork = workService.addNewWork(name, fandomType, rating, category, language, description, comment);
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
        model.addAttribute(EDITABLE_WORK, EntityToDtoConverter.convertWorkBasic(work));
        return HOME_EDIT_WORK;
    }

    @PostMapping(value = "/works/{name}/edit")
    public String performEditWork(@PathVariable(name = "name") String workName,
                                  @RequestParam(name = "fandomType") String fandomType,
                                  @RequestParam(name = "rating") String rating,
                                  @RequestParam(name = "category") String category,
                                  @RequestParam(name = "status") String status,
                                  @RequestParam(name = "language") String language,
                                  @RequestParam(name = "description") String description,
                                  @RequestParam(name = "comment") String comment,
                                  Model model) {
        Work work = workService.findByName(workName);
        if (work == null) {
            work = workService.addNewWork(workName, fandomType, rating, category, language, description, comment);
        } else {
            work = workService.updateWork(work, workName, fandomType, rating, category, status, language, description, comment);
        }
        if (work == null) {
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
            return HOME_ADD_CHAPTER;
        }
        Chapter newChapter = chapterService.saveChapter(work, chapterName, chapterText, notes);
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
        Chapter chapter = chapterService.findByWorkAndName(work, chapterName);
        if (chapter == null) {
            chapter = chapterService.saveChapter(work, chapterName, chapterText, notes);
        } else {
            chapter = chapterService.updateChapter(chapter, chapterName, chapterText, notes);
        }
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
        Chapter chapter = chapterService.findByWorkAndName(work, chapterName);
        if (chapter == null) {
            model.addAttribute(ERROR, ERROR);
            return HOME_WORKS;
        }
        chapterService.deleteChapter(chapter);
        return INDEX_REDIRECT; //todo fix redirects
    }
}
