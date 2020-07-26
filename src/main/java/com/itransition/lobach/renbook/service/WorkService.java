package com.itransition.lobach.renbook.service;

import com.itransition.lobach.renbook.entity.*;
import com.itransition.lobach.renbook.enums.*;
import com.itransition.lobach.renbook.repository.WorkRepository;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static com.itransition.lobach.renbook.constants.OtherConstants.*;

@Service
public class WorkService {

    @Autowired
    private UserService userService;

    @Autowired
    private ChapterService chapterService;

    @Autowired
    private FandomService fandomService;

    @Autowired
    private TagService tagService;

    @Autowired
    private WorkRepository workRepository;

    public Work addNewWork(User author,
                           String name,
                           String workType,
                           List<String> fandoms,
                           List<String> fandomTypes,
                           String rating,
                           String category,
                           String language,
                           List<String> tags,
                           String description,
                           String comment) {
        return saveWork(author, null, name, workType, fandoms, fandomTypes, rating, category, Status.IN_PROGRESS.name(), language, tags, description, comment);
    }

    public Work updateWork(User author,
                           Work work,
                           String name,
                           String workType,
                           List<String> fandoms,
                           List<String> fandomTypes,
                           String rating,
                           String category,
                           String status,
                           String language,
                           List<String> tags,
                           String description,
                           String comment) {
        return saveWork(author, work, name, workType, fandoms, fandomTypes, rating, category, status, language, tags, description, comment);
    }

    private Work saveWork(User author,
                          Work work,
                          String name,
                          String workType,
                          List<String> fandomNames,
                          List<String> fandomTypes,
                          String rating,
                          String category,
                          String status,
                          String language,
                          List<String> tagNames,
                          String description,
                          String comment) {
        List<Fandom> fandoms = fandomService.saveFandomList(fandomNames, fandomTypes);
        List<Tag> tags = tagService.saveTagList(tagNames);
        if (name != null
                && EnumUtils.isValidEnum(Rating.class, rating.toUpperCase())
                && EnumUtils.isValidEnum(Category.class, category.toUpperCase())
                && EnumUtils.isValidEnum(Language.class, language.toUpperCase())
                && EnumUtils.isValidEnum(Status.class, status.toUpperCase())
                && description != null) {
            if (work == null) {
                work = Work.builder()
                        .name(name)
                        .rating(rating.toUpperCase())
                        .category(category.toUpperCase())
                        .language(language.toUpperCase())
                        .tags(tags)
                        .description(description)
                        .comment(comment)
                        .status(status)
                        .lastUpdateMillis(System.currentTimeMillis())
                        .author(author)
                        .build();
            } else {
                work.setName(name);
                work.setRating(rating.toUpperCase());
                work.setCategory(category.toUpperCase());
                work.setLanguage(language);
                work.setTags(tags);
                work.setDescription(description);
                work.setComment(comment);
                work.setStatus(status.toUpperCase());
                //work.setLastUpdateMillis(System.currentTimeMillis());
            }
            if (workType.equalsIgnoreCase(FandomType.ORIGINAL.name())) {
                work.setType(FandomType.ORIGINAL.name());
                work.setFandoms(Collections.emptyList());
            } else if (!fandoms.isEmpty()){
                work.setType(FandomType.FANDOM.name());
                work.setFandoms(fandoms);
            } else {
                return null;
            }
            return workRepository.save(work);
        }
        return null;
    }

    public Page<Work> findAllByAuthor(User author, int pageNumber) {
        if (author != null) {
            Pageable workRequest = PageRequest.of(pageNumber, WORKS_PER_PAGE, Sort.by(SORT_PARAM_LAST_UPDATE).descending());
            return workRepository.findDistinctByAuthor(author, workRequest);
        }
        return null;
    }

    public Page<Work> findAllNonemptyByAuthor(User author, int pageNumber) {
        if (author != null) {
            Pageable workRequest = PageRequest.of(pageNumber, WORKS_PER_PAGE, Sort.by(SORT_PARAM_LAST_UPDATE).descending());
            return workRepository.findDistinctByAuthorAndContentNotNull(author, workRequest);
        }
        return null;
    }

    public Integer countAllNonEmptyWorks() {
        return workRepository.countDistinctByContentNotNull();
    }

    public List<Work> findTop5ByLastUpdate() {
        Pageable workRequest = PageRequest.of(0, 5, Sort.by(SORT_PARAM_LAST_UPDATE).descending());
        return workRepository.findDistinctByContentNotNull(workRequest).getContent();
    }

    public Page<Work> findAllByLastUpdate(int pageNumber) {
        Pageable workRequest = PageRequest.of(pageNumber, WORKS_PER_PAGE, Sort.by(SORT_PARAM_LAST_UPDATE).descending());
        return workRepository.findDistinctByContentNotNull(workRequest);
    }

    public List<Work> findTop5ByAssessment() {
        Pageable workRequest = PageRequest.of(0, 5);
        return workRepository.findDistinctByContentNotNullOrderByAvgAssessment(workRequest).getContent();
    }

    public Page<Work> findAllByAssessment(int pageNumber) {
        Pageable workRequest = PageRequest.of(pageNumber, WORKS_PER_PAGE);
        return workRepository.findDistinctByContentNotNullOrderByAvgAssessment(workRequest);
    }

    public Work findByName(String name) {
        if (name != null) {
            return workRepository.findByName(name);
        }
        return null;
    }

    public void updateStatus(Work work, String newStatus) {
        if (EnumUtils.isValidEnum(Status.class, newStatus.toUpperCase())) {
            work.setStatus(newStatus.toUpperCase());
            workRepository.save(work);
        }
    }

    public void deleteWork(Work work) {
        if (work != null) {
            workRepository.delete(work);
        }
    }

    public void addNewChapter(Work work, Chapter newChapter) {
        if (work != null && newChapter != null) {
            work.getContent().add(newChapter);
            workRepository.save(work);
            /*if (newChapter.getPostTimeMillis() > work.getLastUpdateMillis()) {
                work.setLastUpdateMillis(newChapter.getPostTimeMillis());
                workRepository.save(work);
            }*/
        }
    }
}
