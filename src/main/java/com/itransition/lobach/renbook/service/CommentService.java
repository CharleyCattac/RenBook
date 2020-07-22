package com.itransition.lobach.renbook.service;

import com.itransition.lobach.renbook.entity.Chapter;
import com.itransition.lobach.renbook.entity.Comment;
import com.itransition.lobach.renbook.entity.User;
import com.itransition.lobach.renbook.entity.Work;
import com.itransition.lobach.renbook.enums.MessagePurpose;
import com.itransition.lobach.renbook.repository.CommentRepository;
import com.itransition.lobach.renbook.transfer.CommentOutcoming;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public Comment addNewComment(Chapter chapter,
                                 User author,
                                 String commentText,
                                 Comment receiver) {
        return saveComment(null, chapter, author, commentText, receiver);
    }

    public Comment updateComment(Comment comment,
                                 User author,
                                 String commentText,
                                 Comment receiver) {
        return saveComment(comment, comment.getChapter(), author, commentText, receiver);
    }

    private Comment saveComment(Comment comment,
                               Chapter chapter,
                               User author,
                               String commentText,
                               Comment receiver) {
        if (author != null && !commentText.isBlank()) {
            if (comment == null) {
                comment = Comment.builder()
                        .chapter(chapter)
                        .author(author)
                        .text(commentText)
                        //.receiver(receiver)
                        .postTimeMillis(System.currentTimeMillis())
                        .build();
            } else {
                comment.setText(commentText);
                //comment.setReceiver(receiver);
            }
            return commentRepository.save(comment);
        }
        return null;
    }

    public Comment findById(Long id) {
        return commentRepository.findById(id).get();
    }

    public void deleteComment(Comment comment) {
        if (comment != null) {
            commentRepository.delete(comment);
        }
    }

    public CommentOutcoming buildCommentFor(String purpose, Comment comment) {
        if (EnumUtils.isValidEnum(MessagePurpose.class, purpose.toUpperCase()) && comment != null) {
            return CommentOutcoming.builder()
                    .purpose(purpose.toLowerCase())
                    .id(comment.getId())
                    .authorName(comment.getAuthor().getUsername())
                    .text(comment.getText())
                    .postTime(comment.getPostTimeMillis())
                    .build();
        }
        return null;
    }
}
