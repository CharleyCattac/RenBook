package com.itransition.lobach.renbook.service;

import com.itransition.lobach.renbook.entity.Chapter;
import com.itransition.lobach.renbook.entity.User;
import com.itransition.lobach.renbook.entity.Work;
import com.itransition.lobach.renbook.enums.*;
import com.itransition.lobach.renbook.repository.WorkRepository;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import static com.itransition.lobach.renbook.constants.OtherConstants.*;

@Service
public class WorkService {

    @Autowired
    private UserService userService;

    @Autowired
    private ChapterService chapterService;

    @Autowired
    private WorkRepository workRepository;

    public Work addNewWork(String name,
                           String fandomType,
                           String rating,
                           String category,
                           String language,
                           String description,
                           String comment) {
        if (name != null && EnumUtils.isValidEnum(FandomType.class, fandomType.toUpperCase())
                && EnumUtils.isValidEnum(Rating.class, rating.toUpperCase())
                && EnumUtils.isValidEnum(Category.class, category.toUpperCase())
                && EnumUtils.isValidEnum(Language.class, language.toUpperCase())
                && description != null) {
            User author = getLoggedUser();
            return workRepository.save(Work.builder()
                    .name(name)
                    .type(fandomType.toUpperCase())
                    .rating(rating.toUpperCase())
                    .category(category.toUpperCase())
                    .language(language)
                    .description(description)
                    .comment(comment)
                    .state(State.IN_PROGRESS.name())
                    .lastUpdateMillis(System.currentTimeMillis())
                    .author(author)
                    .build());
        }
        return null;
    }

    public Page<Work> findAllOwnWorks(int pageNumber) {
        User author = getLoggedUser();
        Pageable workRequest = PageRequest.of(pageNumber, WORKS_PER_PAGE, Sort.by(SORT_PARAM_LAST_UPDATE).descending());
        return workRepository.findAllByAuthor(author, workRequest);
    }

    public Page<Work> findAllByAuthorName(String authorName, int pageNumber) {
        if (authorName != null) {
            User author = userService.findUserByUsername(authorName);
            Pageable workRequest = PageRequest.of(pageNumber, WORKS_PER_PAGE, Sort.by(SORT_PARAM_LAST_UPDATE).descending());
            return workRepository.findAllByAuthor(author, workRequest);
        }
        return null;
    }

    public Page<Work> findAll(int pageNumber) {
        Pageable workRequest = PageRequest.of(pageNumber, WORKS_PER_PAGE, Sort.by(SORT_PARAM_LAST_UPDATE).descending());
        return workRepository.findAllBy(workRequest);
    }

    public Work findByName(String name) {
        if (name != null) {
            return workRepository.findByName(name);
        }
        return null;
    }

    public void updateStatus(Work work, String newStatus) {
        if (EnumUtils.isValidEnum(State.class, newStatus.toUpperCase())) {
            work.setState(newStatus.toUpperCase());
            workRepository.save(work);
        }
    }

    public void addNewChapter(Work work, Chapter newChapter) {
        if (work != null && newChapter != null) {
            work.getContent().add(newChapter);
            workRepository.save(work);
            if (newChapter.getPostTimeMillis() > work.getLastUpdateMillis()) {
                work.setLastUpdateMillis(newChapter.getPostTimeMillis());
                workRepository.save(work);
            }
        }
    }

    private User getLoggedUser() {
        org.springframework.security.core.userdetails.User author = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userService.findUserByUsername(author.getUsername());
    }
}
