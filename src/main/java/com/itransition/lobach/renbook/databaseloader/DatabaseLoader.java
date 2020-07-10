package com.itransition.lobach.renbook.databaseloader;

import com.itransition.lobach.renbook.service.RoleService;
import com.itransition.lobach.renbook.service.UserService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Data
public class DatabaseLoader implements CommandLineRunner {

    @Autowired
    private final UserService userService;

    @Autowired
    private final RoleService roleService;

    @Override
    public void run(String... strings) throws Exception {

    }
}
