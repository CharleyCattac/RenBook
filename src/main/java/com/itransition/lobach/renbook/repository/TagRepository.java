package com.itransition.lobach.renbook.repository;

import com.itransition.lobach.renbook.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    Tag getTagByName(String string);

    List<Tag> getAllBy();
    List<Tag> getAllByNameStartingWithOrderByNameAsc(String s);
}
