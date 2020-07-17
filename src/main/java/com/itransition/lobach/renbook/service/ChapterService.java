package com.itransition.lobach.renbook.service;

import com.itransition.lobach.renbook.entity.Chapter;
import com.itransition.lobach.renbook.entity.Work;
import com.itransition.lobach.renbook.repository.ChapterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChapterService {

    @Autowired
    private ChapterRepository chapterRepository;

    public Chapter saveChapter(Work work,
                               String chapterName,
                               String chapterText,
                               String notes) {
        if (!chapterName.isBlank() && !chapterText.isBlank()) {
            return chapterRepository.save(Chapter.builder()
                    .work(work)
                    .name(chapterName)
                    .text(chapterText)
                    .notes(notes)
                    .postTimeMillis(System.currentTimeMillis())
                    .build());
        }
        return null;
    }

    public List<Chapter> findAllByWork(Work work) {
        if (work != null) {
            return chapterRepository.getAllByWorkOrderByPostTimeMillisAsc(work);
        }
        return null;
    }
}
