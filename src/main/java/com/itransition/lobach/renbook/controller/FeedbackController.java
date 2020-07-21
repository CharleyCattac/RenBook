package com.itransition.lobach.renbook.controller;

import com.itransition.lobach.renbook.dto.CommentDto;
import com.itransition.lobach.renbook.entity.Chapter;
import com.itransition.lobach.renbook.entity.Comment;
import com.itransition.lobach.renbook.entity.User;
import com.itransition.lobach.renbook.entity.Work;
import com.itransition.lobach.renbook.service.ChapterService;
import com.itransition.lobach.renbook.service.CommentService;
import com.itransition.lobach.renbook.service.UserService;
import com.itransition.lobach.renbook.service.WorkService;
import com.itransition.lobach.renbook.transfer.CommentTransfer;
import com.itransition.lobach.renbook.util.EntityToDtoConverter;
import com.itransition.lobach.renbook.util.SecurityHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import java.security.Principal;

import static com.itransition.lobach.renbook.util.EntityToDtoConverter.*;

@Controller
public class FeedbackController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private WorkService workService;

    @Autowired
    private ChapterService chapterService;

    @Autowired
    private CommentService commentService;

    @MessageMapping(value = "/comment")
    public void performAddComment(@Payload CommentTransfer comment,
                                  @AuthenticationPrincipal Principal principal) {

        Work work = workService.findByName(comment.getWorkName());
        Chapter chapter = chapterService.findByWorkAndName(work, comment.getChapterName());
        User author = userService.findUserByUsername(principal.getName());
        Comment newComment = commentService.addNewComment(chapter, author, comment.getCommentText(), null);

        createUrl(comment, newComment);
    }

    @MessageMapping(value = "/deletecomment")
    public void performDeleteComment(@Payload Long commentId,
                                  @AuthenticationPrincipal Principal principal) {

        User author = userService.findUserByUsername(principal.getName());
        Comment comment = commentService.findById(commentId);
        if (comment != null) {

        }

        //createUrl(comment, newComment);
    }

    private void createUrl(CommentTransfer comment, Comment newComment) {
        if (comment.getChapterCount() == 1) {
            messagingTemplate.convertAndSend("/works/view/" + comment.getWorkName() + "?", convertComment(newComment));
        } else {
            messagingTemplate.convertAndSend("/works/view/" + comment.getWorkName() + "/" + comment.getChapterName() + "?", convertComment(newComment));
        }
    }
}
