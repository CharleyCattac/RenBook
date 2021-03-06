package com.itransition.lobach.renbook.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "work")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Work {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private User author = new User();

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 500)
    private String description;

    @Column(length = 500)
    private String comment;

    @ManyToMany
    @JoinTable(name = "fandom_work",
            joinColumns = @JoinColumn(name = "work_id"),
            inverseJoinColumns = @JoinColumn(name = "fandom_id"))
    private List<Fandom> fandoms;

    @Column(nullable = false)
    private String status;
    @Column(nullable = false)
    private String rating;
    @Column(nullable = false)
    private String category;
    @Column(nullable = false)
    private String type;
    @Column(nullable = false)
    private String language;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "work_tag",
            joinColumns = @JoinColumn(name = "work_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<Tag> tags;

    @OneToMany(mappedBy = "work", cascade = CascadeType.ALL)
    private List<Chapter> content = new ArrayList<>();

    @OneToMany(mappedBy = "work", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @OneToMany(mappedBy = "work", cascade = CascadeType.ALL)
    private List<Assessment> assessmentList;

    private Long lastUpdateMillis;
}
