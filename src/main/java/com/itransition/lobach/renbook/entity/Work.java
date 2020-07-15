package com.itransition.lobach.renbook.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 500)
    private String description;

    @Column(length = 500)
    private String comment;

    @ManyToMany(mappedBy = "works", cascade = CascadeType.ALL)
    private Set<Fandom> fandoms;

    @Column(nullable = false)
    private String state;
    @Column(nullable = false)
    private String rating;
    @Column(nullable = false)
    private String category;
    @Column(nullable = false)
    private String language;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "work_tag",
            joinColumns = @JoinColumn(name = "work_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags;

    @OneToMany(mappedBy = "work", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<WorkText> content = new ArrayList<>();

    private Long lastUpdateMillis;
}
