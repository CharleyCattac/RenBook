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

    @GetMapping(value = "/works/add_work")
    public String getAddWork(Model model) {
        return HOME_ADD_WORK;
    }

    @PostMapping(value = "/works/add_work")
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
        return HOME_WORKS_REDIRECT;
    }

    @RequestMapping(value = "/works/add_work/done")
    public String postConfirmation() {
        return HOME_WORKS;
    }

    @GetMapping(value = "/works/add_chapter")
    public String showAddChapter(@RequestParam(name = "workName") String name,
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

    @PostMapping(value = "/works/add_chapter")
    public String performAddChapter(@RequestParam(name = "workName") String workName,
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

        return HOME_WORKS_REDIRECT;
    }
}
