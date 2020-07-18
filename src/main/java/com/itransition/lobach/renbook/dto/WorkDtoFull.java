package com.itransition.lobach.renbook.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkDtoFull {

    protected String authorName;

    protected String name;
    protected String description;
    protected String comment;

    protected Double averageAssessment;
    protected Integer assessmentCount;

    protected List<FandomDto> fandoms;
    protected List<String> tags;
    protected String status;
    protected String fandomType;
    protected String category;
    protected String rating;
    protected String language;

    private List<ChapterDto> chapters;
    protected Integer wordsCount;

    private Long lastUpdateMillis;

    //comments
}
