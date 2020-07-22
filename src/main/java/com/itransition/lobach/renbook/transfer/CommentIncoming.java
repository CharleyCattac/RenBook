package com.itransition.lobach.renbook.transfer;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentIncoming {

    private String purpose;
    private String authorName;

    private Long commentId;
    private String workName;
    private Integer chapterCount;
    private String chapterName;
    private String commentText;
}
