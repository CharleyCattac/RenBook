package com.itransition.lobach.renbook.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    private Long id; //todo replace with gen key
    private String authorName;
    private String text;
    private Long postTime;
}
