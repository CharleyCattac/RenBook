package com.itransition.lobach.renbook.service;

import com.itransition.lobach.renbook.entity.Tag;
import com.itransition.lobach.renbook.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    public Tag saveTag(String name) {
        if (name != null) {
            return tagRepository.save(Tag.builder()
                    .name(name)
                    .build());
        } else {
            return null;
        }
    }

    public Tag findByName(String s) {
        if (s != null) {
            return tagRepository.getTagByName(s);
        } else {
            return null;
        }
    }

    public List<Tag> findAll() {
        return tagRepository.getAllBy();
    }

    public List<Tag> findTopFifteen() {
        return tagRepository.getAllBy()
                .stream()
                .sorted(Comparator.comparingInt(o -> o.getWorks().size()))
                .collect(Collectors.toList());
    }

    public List<Tag> findAllStartingWith(String s) {
        return tagRepository.getAllByNameStartingWithOrderByNameAsc(s);
    }
}
