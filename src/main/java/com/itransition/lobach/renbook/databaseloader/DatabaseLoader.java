package com.itransition.lobach.renbook.databaseloader;

import com.itransition.lobach.renbook.service.RoleService;
import com.itransition.lobach.renbook.service.TagService;
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

    @Autowired
    private final TagService tagService;

    @Override
    public void run(String... strings) throws Exception {
        /*tagService.saveTag("Romance");
        tagService.saveTag("Романтика");

        tagService.saveTag("Повседневность");

        tagService.saveTag("Драма");
        tagService.saveTag("Drama");

        tagService.saveTag("AU (alternative universe)");

        tagService.saveTag("Humour");
        tagService.saveTag("Юмор");

        tagService.saveTag("Психология");
        tagService.saveTag("Psychology");*/
    }
}
