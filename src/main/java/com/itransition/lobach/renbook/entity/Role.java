package com.itransition.lobach.renbook.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "role")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    public Role(String s) {
        this.name = s;
    }

    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();
}
