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

    public Chapter addNewChapter(Work work,
                                 String chapterName,
                                 String chapterText,
                                 String notes) {
        return saveChapter(null, work, chapterName, chapterText, notes);
    }

    public Chapter updateChapter(Chapter chapter,
                                 String chapterName,
                                 String chapterText,
                                 String notes) {
        return saveChapter(chapter, chapter.getWork(), chapterName, chapterText, notes);
    }

    private Chapter saveChapter(Chapter chapter,
                                Work work,
                                String chapterName,
                                String chapterText,
                                String notes) {
        if (!chapterName.isBlank() && !chapterText.isBlank() && work != null) {
            if (chapter == null) {
                chapter = Chapter.builder()
                        .work(work)
                        .name(chapterName)
                        .text(chapterText)
                        .notes(notes)
                        .postTimeMillis(System.currentTimeMillis())
                        .build();
            } else {
                chapter.setName(chapterName);
                chapter.setText(chapterText);
                chapter.setNotes(notes);
                chapter.setPostTimeMillis(System.currentTimeMillis());
            }
            work.setLastUpdateMillis(chapter.getPostTimeMillis());
            return chapterRepository.save(chapter);
        }
        return null;
    }

    public Chapter findByWorkAndName(Work work, String name) {
        if (work != null && name != null) {
            return chapterRepository.findByWorkAndName(work, name);
        }
        return null;
    }

    public void deleteChapter(Chapter chapter) {
        if (chapter != null) {
            chapterRepository.delete(chapter);
        }
    }
}
