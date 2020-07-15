package com.itransition.lobach.renbook.repository;

import com.itransition.lobach.renbook.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role getRoleById(Long id);
    Role getRoleByName(String name);
}
