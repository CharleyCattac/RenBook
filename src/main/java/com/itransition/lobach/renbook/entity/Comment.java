package com.itransition.lobach.renbook.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "comment")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "chapter_id", referencedColumnName = "id")
    private Chapter chapter;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private User author;

    @Column(nullable = false, length = 1000)
    private String text;

    @OneToOne
    @JoinColumn(name = "receiver_id", referencedColumnName = "id")
    private Comment receiver;

    private Long postTimeMillis;
}
