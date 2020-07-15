package com.itransition.lobach.renbook.dto;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkDtoBasic {

    protected String authorName;

    protected String name;
    protected String description;

    protected Double averageAssessment;
    protected Integer assessmentCount;

    protected Set<FandomDto> fandoms;
    protected Set<String> tags;
    protected String state;
    protected String category;
    protected String rating;
    protected String language;
}
