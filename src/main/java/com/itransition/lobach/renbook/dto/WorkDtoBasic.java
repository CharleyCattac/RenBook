package com.itransition.lobach.renbook.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkDtoBasic {

    protected String authorName;

    protected String name;
    protected String description;

    protected Double averageAssessment;
    protected Integer assessmentCount;

    protected List<FandomDto> fandoms;
    protected List<String> tags;
    protected String state;
    protected String fandomType;
    protected String category;
    protected String rating;
    protected String language;

    protected List<String> chapters;
    protected Integer wordsCount;
}
