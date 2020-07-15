package com.itransition.lobach.renbook.repository;

import com.itransition.lobach.renbook.entity.User;
import com.itransition.lobach.renbook.entity.Work;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkRepository extends JpaRepository<Work, Long> {

    // returns all works
    Page<Work> getAllBy(PageRequest pageRequest);
    // returns all works, which belong to a particular user
    Page<Work> getAllByAuthor(User author, PageRequest pageRequest);

    // returns all works on a particular fandom

    //pageable by everything
}
