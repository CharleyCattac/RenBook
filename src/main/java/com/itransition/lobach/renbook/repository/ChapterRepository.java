package com.itransition.lobach.renbook.repository;

import com.itransition.lobach.renbook.entity.Work;
import com.itransition.lobach.renbook.entity.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Long> {

    List<Chapter> getAllByWorkOrderByPostTimeMillisAsc(Work work);
}
