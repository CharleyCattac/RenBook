package com.itransition.lobach.renbook.dto;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ViewUserDto {

    private String username;
    private String avatarUrl;
    private Long lastSeenMillis;
    private Date birthday;
    private String description;

    private List<WorkDtoBasic> works;
}
