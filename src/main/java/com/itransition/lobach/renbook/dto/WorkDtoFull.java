package com.itransition.lobach.renbook.dto;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkDtoFull extends WorkDtoBasic {

    private String comment;
    private String lastUpdateMillis;

    private Set<ChapterDto> texts;

    //comments
}
