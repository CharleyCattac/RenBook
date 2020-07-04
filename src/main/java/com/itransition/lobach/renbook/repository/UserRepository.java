package com.itransition.lobach.renbook.repository;

import com.itransition.lobach.renbook.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@Repository

@RepositoryRestResource(collectionResourceRel = "users", path = "user-list")
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    List<User> findAll();
}
