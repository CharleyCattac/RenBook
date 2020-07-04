package com.itransition.lobach.renbook.service;

import com.itransition.lobach.renbook.builder.UserBuilder;
import com.itransition.lobach.renbook.entity.Role;
import com.itransition.lobach.renbook.entity.User;
import com.itransition.lobach.renbook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User saveUser(String email, String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            Set<Role> roles = new HashSet<>();
            roles.add(roleService.getUserRole());

            return saveToRepo(email, username, passwordEncoder.encode(password), roles);
        }
        return null;
    }

    public User saveAdmin(String email, String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            Set<Role> roles = new HashSet<>();
            roles.add(roleService.getAdminRole());

            return saveToRepo(email, username, passwordEncoder.encode(password), roles);
        }
        return null;
    }

    private User saveToRepo(String email, String username, String password, Set<Role> roles) {
        User newUser = UserBuilder.buildUser(
                email,
                username,
                passwordEncoder.encode(password),
                roles,
                System.currentTimeMillis());

        return userRepository.save(newUser);
    }

    public void blockUser(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            user.setBlocked(true);
            userRepository.save(user);
        }
    }

    public void unblockUser(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            user.setBlocked(false);
            userRepository.save(user);
        }
    }

    public void updateLastCheckoutTime(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            user.setMillisWhenLastLogin(System.currentTimeMillis());
            userRepository.save(user);
        }
    }

    public void deleteUser(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            userRepository.delete(user);
        }
    }
}
