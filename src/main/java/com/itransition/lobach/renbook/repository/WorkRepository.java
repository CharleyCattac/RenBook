package com.itransition.lobach.renbook.repository;

import com.itransition.lobach.renbook.entity.User;
import com.itransition.lobach.renbook.entity.Work;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkRepository extends JpaRepository<Work, Long> {

    Work findByName(String name);
    // returns all works
    Page<Work> findAllBy(Pageable pageable);
    // returns all works, which belong to a particular user
    Page<Work> findAllByAuthor(User author, Pageable pageable);

    // returns all works on a particular fandom

    //pageable by everything
}
