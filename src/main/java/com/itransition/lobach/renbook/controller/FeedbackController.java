package com.itransition.lobach.renbook.controller;

import com.itransition.lobach.renbook.entity.*;
import com.itransition.lobach.renbook.enums.MessagePurpose;
import com.itransition.lobach.renbook.service.*;
import com.itransition.lobach.renbook.transfer.AssessIncoming;
import com.itransition.lobach.renbook.transfer.AssessOutcoming;
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

import static com.itransition.lobach.renbook.constants.OtherConstants.*;

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
    private CommentService commentService;

    @Autowired
    private AssessmentService assessmentService;

    @MessageMapping(value = "/add_comment")
    public void performAddComment(@Payload CommentIncoming comment,
                                  @AuthenticationPrincipal Principal principal) {
        if (principal == null) {
         return;
        }

        Work work = workService.findByName(comment.getWorkName());
        User author = userService.findUserByUsername(principal.getName());
        Comment newComment = commentService.addNewComment(work, author, comment.getCommentText(), null);

        createCommentResponseUrl(comment, commentService.buildCommentFor(MessagePurpose.ADD.name().toLowerCase(), newComment));
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

        createCommentResponseUrl(incoming, commentService.buildCommentFor(MessagePurpose.DELETE.name().toLowerCase(), comment));
    }

    @MessageMapping(value = "/assessment")
    public void performSaveAssessment(@Payload AssessIncoming incoming,
                                      @AuthenticationPrincipal Principal principal) {

        User sender = userService.findUserByUsername(principal.getName());
        Work work = workService.findByName(incoming.getWorkName());
        if (work.getAuthor().getUsername().equals(sender.getUsername())) {
            return;
        }
        Assessment processed = assessmentService.processAssessment(work, sender, incoming.getValue());

        if (processed != null) {
            messagingTemplate.convertAndSendToUser(principal.getName(),
                    work.getName() + "?",
                    AssessOutcoming.builder().purpose("ass_suc").build());
        }
    }

    private void createCommentResponseUrl(CommentIncoming comment, CommentOutcoming outcoming) {
        if (comment.getChapterCount() == 1 || comment.getChapterName().equalsIgnoreCase("chapter")) {
            messagingTemplate.convertAndSend(WORK_VIEW_RESPONSE_BASE
                    + comment.getWorkName() + "?",
                    outcoming);
        } else {
            messagingTemplate.convertAndSend(WORK_VIEW_RESPONSE_BASE
                    + comment.getWorkName() + "/"
                    + comment.getChapterName() + "?",
                    outcoming);
        }
    }
}
