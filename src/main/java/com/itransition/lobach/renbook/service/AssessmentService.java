package com.itransition.lobach.renbook.service;

import com.itransition.lobach.renbook.entity.Assessment;
import com.itransition.lobach.renbook.entity.User;
import com.itransition.lobach.renbook.entity.Work;
import com.itransition.lobach.renbook.repository.AssessmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssessmentService {

    @Autowired
    private AssessmentRepository assessmentRepository;

    public Assessment processAssessment(Work work,
                                        User user,
                                        Double value) {
        if (value < 0 || value > 5 || work == null || user == null) {
            return null;
        }
        Assessment assessment = assessmentRepository.findAssessmentByWorkAndUser(work, user);
        if (value > 0) {
            return save(assessment, work, user, value);
        } else {
            if (assessment != null) {
                assessmentRepository.delete(assessment);
            }
            return null;
        }
    }

    private Assessment save(Assessment assessment,
                            Work work,
                            User user,
                            Double value) {
        if (work != null && user != null && value > 0) {
            if (assessment == null) {
                assessment = Assessment.builder()
                        .work(work)
                        .user(user)
                        .value(value)
                        .build();
            } else {
                assessment.setValue(value);
            }
            return  assessmentRepository.save(assessment);
        } else {
            return null;
        }
    }

    public Assessment findByUserAndWork(User user, Work work) {
        if (user != null && work != null) {
            return assessmentRepository.findAssessmentByWorkAndUser(work, user);
        } else {
            return null;
        }
    }
}
