package com.itransition.lobach.renbook.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDtoBasic {

    private String role;

    protected String username;
    protected String avatarUrl;

    protected Integer unreadNews;
    protected Integer ownWorks;
    protected Integer savedWorks;
}
