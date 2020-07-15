package com.itransition.lobach.renbook.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkTextDto {

    //null if there-s only one text
    private String name;
    private String text;
    private String comment;

    private Long postTimeMillis;
}
