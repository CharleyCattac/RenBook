package com.itransition.lobach.renbook.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditUserDto {

    private String username;
    private String email;
    private String language;
    private String theme;
    private String avatarUrl;
    private Date birthday;
    private String description;
    private Long lastSeenMillis;
}
