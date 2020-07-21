package com.itransition.lobach.renbook.dto;

import lombok.*;

import java.util.List;

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
    private List<CommentDto> comments;

    private Long postTimeMillis;
}
