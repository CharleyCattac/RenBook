package com.itransition.lobach.renbook.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "fandom")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Fandom {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    //for url
    @Column(nullable = false, unique = true)
    private String codeName;

    @Column(nullable = false)
    private String type;

    @ManyToMany(mappedBy = "fandoms")
    private Set<Work> works;
}
