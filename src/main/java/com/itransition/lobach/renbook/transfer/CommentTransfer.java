package com.itransition.lobach.renbook.transfer;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentTransfer {

    private String workName;
    private Integer chapterCount;
    private String chapterName;
    private String commentText;
    private String receiverId;
}
