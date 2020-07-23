package com.itransition.lobach.renbook.controller;

import com.itransition.lobach.renbook.entity.Chapter;
import com.itransition.lobach.renbook.entity.Comment;
import com.itransition.lobach.renbook.entity.User;
import com.itransition.lobach.renbook.entity.Work;
import com.itransition.lobach.renbook.enums.MessagePurpose;
import com.itransition.lobach.renbook.service.*;
import com.itransition.lobach.renbook.transfer.CommentIncoming;
import com.itransition.lobach.renbook.transfer.CommentOutcoming;
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
    private SecurityHelper securityHelper;

    @Autowired
    private UserService userService;

    @Autowired
    private WorkService workService;

    @Autowired
    private ChapterService chapterService;

    @Autowired
    private CommentService commentService;

    @MessageMapping(value = "/add_comment")
    public void performAddComment(@Payload CommentIncoming comment,
                                  @AuthenticationPrincipal Principal principal) {
        if (principal == null) {
         return;
        }

        Work work = workService.findByName(comment.getWorkName());
        User author = userService.findUserByUsername(principal.getName());
        Comment newComment = commentService.addNewComment(work, author, comment.getCommentText(), null);

        createUrl(comment, commentService.buildCommentFor(MessagePurpose.ADD.name().toLowerCase(), newComment));
    }

    @MessageMapping(value = "/delete_comment")
    public void performDeleteComment(@Payload CommentIncoming incoming,
                                     @AuthenticationPrincipal Principal principal) {

        User sender = userService.findUserByUsername(principal.getName());
        Comment comment = commentService.findById(incoming.getCommentId());
        if (securityHelper.isUserAdmin(sender)
            || comment.getAuthor().getUsername().equals(sender.getUsername())
            || comment.getWork().getAuthor().getUsername().equals(sender.getUsername())) {
            commentService.deleteComment(comment);
        } else {
            return;
        }

        createUrl(incoming, commentService.buildCommentFor(MessagePurpose.DELETE.name().toLowerCase(), comment));
    }

    private void createUrl(CommentIncoming comment, CommentOutcoming outcoming) {
        if (comment.getChapterCount() == 1 || comment.getChapterName().equalsIgnoreCase("chapter")) {
            messagingTemplate.convertAndSend("/works/view/" + comment.getWorkName() + "?", outcoming);
        } else {
            messagingTemplate.convertAndSend("/works/view/" + comment.getWorkName() + "/" + comment.getChapterName() + "?", outcoming);
        }
    }
}
