package com.itransition.lobach.renbook.entity;

import lombok.*;

import javax.persistence.*;

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
    @Column(nullable = false, length = 67108864) // ~8 MB
    private String text;

    @Column(length = 350)
    private String notes;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "work_id", referencedColumnName = "id")
    private Work work = new Work();

    private Long postTimeMillis;
}
