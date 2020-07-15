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

    @Column(nullable = false)
    private String name;

    //for url
    @Column(nullable = false, unique = true)
    private String codeName;

    @Column(nullable = false)
    private String type;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "fandom_work",
            joinColumns = @JoinColumn(name = "fandom_id"),
            inverseJoinColumns = @JoinColumn(name = "work_id"))
    private Set<Work> works;
}
