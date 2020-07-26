package com.itransition.lobach.renbook.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "assessment")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Assessment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private Double value;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user = new User();

    @ManyToOne
    @JoinColumn(name = "work_id", referencedColumnName = "id")
    private Work work = new Work();
}
