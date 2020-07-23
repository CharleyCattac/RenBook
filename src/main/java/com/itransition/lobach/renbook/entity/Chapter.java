package com.itransition.lobach.renbook.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "chapter")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Chapter {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Lob @Basic(fetch = FetchType.LAZY)
    @Column(nullable = false, length = 45000)
    private String text;

    @Column(length = 500)
    private String notes;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "work_id", referencedColumnName = "id")
    private Work work = new Work();

    @OneToMany(mappedBy = "chapter", cascade = CascadeType.ALL)
    private List<Comment> comments;

    private Long postTimeMillis;
}
