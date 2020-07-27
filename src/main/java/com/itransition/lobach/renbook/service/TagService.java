package com.itransition.lobach.renbook.service;

import com.itransition.lobach.renbook.entity.Tag;
import com.itransition.lobach.renbook.entity.Work;
import com.itransition.lobach.renbook.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.itransition.lobach.renbook.constants.OtherConstants.*;

@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    public Tag saveTag(String name) {
        if (name != null) { //&& name.matches(TAG_PATTERN)
            if (!name.isBlank()) {
                return tagRepository.save(Tag.builder()
                        .name(name)
                        .build());
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public List<Tag> saveTagList(List<String> names) {
        Tag tag;
        List<Tag> tags = new ArrayList<>();
        if (names == null) {
            return tags;
        }
        for (String name : names) {
            if (name != null) {
                tag = findByName(name);
                if (tag == null) {
                    tag = saveTag(name);
                }
                if (tag != null) {
                    tags.add(tag);
                }
            }
        }
        return tags;
    }

    public Tag findByName(String s) {
        if (s != null) {
            return tagRepository.getTagByName(s);
        } else {
            return null;
        }
    }

    public List<Tag> findAll() {
        return findAllSortedByWorkCount();
    }

    public List<Tag> findTopFifteen() {
        List<Tag> allTags = findAllSortedByWorkCount();
        if (allTags.size() > 15) {
            return allTags.subList(0, 15);
        }
        return allTags;
    }

    private List<Tag> findAllSortedByWorkCount() {
        List<Tag> tags = tagRepository.getAllBy()
                .stream()
                .sorted(Comparator.comparingInt(o -> o.getWorks().size()))
                .collect(Collectors.toList());
        Collections.reverse(tags);
        return tags;
    }

    public List<Work> findWorksByTagName(Tag tag, int page) {
        List<Work> works = new ArrayList<>();
        if (tag != null) {
            works = new ArrayList<>(tag.getWorks());
            int toIndex = (page + 1) * WORKS_PER_PAGE > works.size() ? works.size() : (page + 1) * WORKS_PER_PAGE;
            works = works.subList(page * WORKS_PER_PAGE, toIndex);
        }
        return works;
    }

    public void deleteTag(Tag tag) {
        if (tag != null) {
            tagRepository.delete(tag);
        }
    }
}
