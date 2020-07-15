package com.itransition.lobach.renbook.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "work_text")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkText {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Lob @Basic(fetch = FetchType.LAZY)
    @Column(nullable = false, length = 15728640) // ~15 MB
    private String text;

    @Column(length = 500)
    private String comment;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "work_id", referencedColumnName = "id")
    private Work work = new Work();

    private Long postTimeMillis;
}
