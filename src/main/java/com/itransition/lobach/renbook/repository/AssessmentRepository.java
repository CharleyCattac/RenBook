package com.itransition.lobach.renbook.repository;

import com.itransition.lobach.renbook.entity.Assessment;
import com.itransition.lobach.renbook.entity.User;
import com.itransition.lobach.renbook.entity.Work;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssessmentRepository extends JpaRepository<Assessment, Long> {

    Assessment findAssessmentByWorkAndUser(Work work, User user);
}
