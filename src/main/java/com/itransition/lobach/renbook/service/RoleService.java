package com.itransition.lobach.renbook.service;

import com.itransition.lobach.renbook.entity.Role;
import com.itransition.lobach.renbook.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    public static final Long USER_ID = 30L;
    public static final Long ADMIN_ID = 28L;

    @Autowired
    private RoleRepository roleRepository;

    public Role saveRole(Role role) {
        return roleRepository.getRoleByName(role.getName()) == null
                ? roleRepository.save(role)
                : null;
    }

    public Role getRoleById(Long id) {
        if(id != null){
            return roleRepository.getRoleById(id);
        }
        return null;
    }

    public Role getUserRole() {
        return roleRepository.getRoleById(USER_ID);
    }

    public Role getAdminRole() {
        return roleRepository.getRoleById(ADMIN_ID);
    }
}
