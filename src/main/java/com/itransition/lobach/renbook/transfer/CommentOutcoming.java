package com.itransition.lobach.renbook.transfer;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentOutcoming {

    private String purpose;

    private Long id;
    private String authorName;
    private String text;
    private Long postTime;
}
