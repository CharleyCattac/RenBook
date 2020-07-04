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
    public void run(String... strings) throws Exception{
        /*createAdmin();
        createUser();*/
    }

    public void createAdmin() {
        userService.saveAdmin(
                "rnbkspprt@gmail.com",
                "support#1",
                "digital12345admin");
    }

    public void createUser() {
        userService.saveUser(
                "ren.n.lol.l.sama@gmail.com",
                "ren shartac",
                "digital12345user");
    }
}
