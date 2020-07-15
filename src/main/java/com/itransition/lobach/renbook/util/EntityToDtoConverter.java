package com.itransition.lobach.renbook.util;

import com.itransition.lobach.renbook.dto.FandomDto;
import com.itransition.lobach.renbook.dto.TagDto;
import com.itransition.lobach.renbook.entity.Fandom;
import com.itransition.lobach.renbook.entity.Tag;

import java.util.ArrayList;
import java.util.List;

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
}
