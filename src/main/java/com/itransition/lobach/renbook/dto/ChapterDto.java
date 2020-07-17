package com.itransition.lobach.renbook.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChapterDto {

    //null if there-s only one text
    private String name;
    private String text;
    private String notes;

    private Long postTimeMillis;
}
