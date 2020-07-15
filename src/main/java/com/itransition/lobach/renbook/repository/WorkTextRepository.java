package com.itransition.lobach.renbook.repository;

import com.itransition.lobach.renbook.entity.Work;
import com.itransition.lobach.renbook.entity.WorkText;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkTextRepository extends JpaRepository<WorkText, Long> {

    List<WorkText> getAllByWorkOrderByPostTimeMillisAsc(Work work);
}
