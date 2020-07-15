package com.itransition.lobach.renbook.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "user_info")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String avatarUrl;
    private Date birthDate;
    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private String preferredLanguage;
    @Column(nullable = false)
    private String preferredTheme;
}
