package com.itransition.lobach.renbook.util;

import com.itransition.lobach.renbook.dto.*;
import com.itransition.lobach.renbook.entity.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class EntityToDtoConverter {

    private EntityToDtoConverter() {
        super();
    }

    public static TagDto convertTag(Tag tag) {
        return TagDto.builder()
                .name(tag.getName())
                .workCount(tag.getWorks().size())
                .build();
    }

    public static List<TagDto> convertTagList(List<Tag> tags) {
        List<TagDto> dtos = new ArrayList<>();
        for (Tag tag : tags) {
            dtos.add(convertTag(tag));
        }
        return dtos;
    }

    public static FandomDto convertFandom(Fandom fandom) {
        return FandomDto.builder()
                .name(fandom.getName())
                .type(fandom.getType())
                .workCount(fandom.getWorks().size())
                .build();
    }

    public static List<FandomDto> convertFandomList(List<Fandom> fandoms) {
        List<FandomDto> dtos = new ArrayList<>();
        for (Fandom fandom : fandoms) {
            dtos.add(convertFandom(fandom));
        }
        return dtos;
    }

    public static WorkDtoBasic convertWorkBasic(Work work) {
        List<String> fandomList = new ArrayList<>();
        List<String> tagList = new ArrayList<>();
        List<String> chapterList = new ArrayList<>();
        for (Fandom fandom : work.getFandoms()) {
            fandomList.add(fandom.getName());
        }
        for (Tag tag : work.getTags()) {
            tagList.add(tag.getName());
        }
        for (Chapter chapter : work.getContent()) {
            chapterList.add(chapter.getName());
        }

        return WorkDtoBasic.builder()
                .authorName(work.getAuthor().getUsername())
                .name(work.getName())
                .fandomType(work.getType())
                .fandoms(convertFandomList(work.getFandoms()))
                .category(work.getCategory())
                .rating(work.getRating())
                .status(work.getStatus())
                .description(work.getDescription())
                .comment(work.getComment())
                .language(work.getLanguage())
                .tags(tagList)
                .assessmentCount(0)
                .averageAssessment(0.0)
                .chapters(chapterList)
                .wordsCount(0) //todo add words counting
                .build();
    }

    public static WorkDtoFull convertWorkFull(Work work) {
        List<String> fandomList = new ArrayList<>();
        List<String> tagList = new ArrayList<>();
        for (Fandom fandom : work.getFandoms()) {
            fandomList.add(fandom.getName());
        }
        for (Tag tag : work.getTags()) {
            tagList.add(tag.getName());
        }

        return WorkDtoFull.builder()
                .authorName(work.getAuthor().getUsername())
                .name(work.getName())
                .fandomType(work.getType())
                .fandoms(convertFandomList(work.getFandoms()))
                .category(work.getCategory())
                .rating(work.getRating())
                .status(work.getStatus())
                .description(work.getDescription())
                .comment(work.getComment())
                .language(work.getLanguage())
                .tags(tagList)
                .assessmentCount(0)
                .averageAssessment(0.0)
                .chapters(convertChapterList(work.getContent()))
                .wordsCount(0) //todo add words counting
                .lastUpdateMillis(work.getLastUpdateMillis())
                .build();
    }

    public static List<WorkDtoBasic> convertWorkBasicList(List<Work> works) {
        List<WorkDtoBasic> dtos = new ArrayList<>();
        for (Work work : works) {
            dtos.add(convertWorkBasic(work));
        }
        return dtos;
    }

    public static ChapterDto convertChapter(Chapter chapter) {
        //List<CommentDto> commentDtos = convertCommentList(chapter.getComments());
        //Collections.reverse(commentDtos);
        return ChapterDto.builder()
                .name(chapter.getName())
                .text(chapter.getText())
                .notes(chapter.getNotes())
                .comments(convertCommentList(chapter.getComments()))
                .postTimeMillis(chapter.getPostTimeMillis())
                .build();
    }

    public static List<ChapterDto> convertChapterList(List<Chapter> chapters) {
        List<ChapterDto> dtos = new ArrayList<>();
        for (Chapter chapter: chapters) {
            dtos.add(convertChapter(chapter));
        }
        return dtos;
    }

    public static CommentDto convertComment(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .authorName(comment.getAuthor().getUsername())
                .text(comment.getText())
                .postTime(comment.getPostTimeMillis())
                .build();
    }

    public static List<CommentDto> convertCommentList(List<Comment> comments) {
        List<CommentDto> dtos = new ArrayList<>();
        for (Comment comment : comments) {
            dtos.add(convertComment(comment));
        }
        return dtos;
    }
}
