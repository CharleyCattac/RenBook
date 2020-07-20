package com.itransition.lobach.renbook.repository;

import com.itransition.lobach.renbook.entity.Fandom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FandomRepository extends JpaRepository<Fandom, Long> {

    Fandom findByName(String name);

    List<Fandom> getAllByOrderByNameAsc();
    List<Fandom> getAllByTypeOrderByNameAsc(String type);
}
