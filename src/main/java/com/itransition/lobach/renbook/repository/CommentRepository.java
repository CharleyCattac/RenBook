package com.itransition.lobach.renbook.repository;

import com.itransition.lobach.renbook.entity.Chapter;
import com.itransition.lobach.renbook.entity.Comment;
import com.itransition.lobach.renbook.entity.Work;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    //without sort (for whole work)
    List<Comment> findAllByWork(Work work);
    //for chapter
    List<Comment> findAllByWorkOrderByPostTimeMillisAsc(Work work);
}
