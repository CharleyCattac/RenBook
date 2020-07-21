package com.itransition.lobach.renbook.repository;

import com.itransition.lobach.renbook.entity.Chapter;
import com.itransition.lobach.renbook.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    //without sort (for whole work)
    List<Comment> findAllByChapter(Chapter chapter);
    //for chapter
    List<Comment> findAllByChapterOrderByPostTimeMillisDesc(Chapter chapter);
}
