package com.itransition.lobach.renbook.repository;

import com.itransition.lobach.renbook.entity.User;
import com.itransition.lobach.renbook.entity.Work;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkRepository extends JpaRepository<Work, Long> {

    // count all works that have uploaded chapters
    int countDistinctByContentNotNull();
    // find by work name (unique)
    Work findByName(String name);
    // returns all works
    Page<Work> findDistinctByContentNotNull(Pageable pageable);
    // returns all works, which belong to a particular user
    Page<Work> findDistinctByAuthor(User author, Pageable pageable);
    // returns all works, which belong to a particular user
    Page<Work> findDistinctByAuthorAndContentNotNull(User author, Pageable pageable);
    // returns all works sorted by average assessment
    @Query(value="SELECT DISTINCT * "
            + "FROM work w "
            + "WHERE (SELECT DISTINCT COUNT(*) FROM chapter c WHERE w.id = c.work_id) > 0 "
            + "ORDER BY (SELECT AVG(value) AS AverageAssessment FROM assessment a WHERE a.work_id = w.id) DESC",
            countQuery="SELECT DISTINCT COUNT(*) "
            + "FROM work w "
            + "WHERE (SELECT COUNT(*) FROM chapter c WHERE w.id = c.work_id) > 0 ",
            nativeQuery=true)
    Page<Work> findDistinctByContentNotNullOrderByAvgAssessment(Pageable pageable);

    // returns all works on a particular fandom

    //pageable by everything
}
