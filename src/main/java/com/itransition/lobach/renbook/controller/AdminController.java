package com.itransition.lobach.renbook.controller;

import com.itransition.lobach.renbook.entity.Tag;
import com.itransition.lobach.renbook.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.itransition.lobach.renbook.constants.PathConstants.INDEX_REDIRECT;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    @Autowired
    private TagService tagService;

    @PostMapping(value = "/delete_tag/{tagName}")
    public String getUsers(@PathVariable("tagName") String tagName,
                           Model model) {
        Tag tag = tagService.findByName(tagName);
        if (tag != null) {
            tagService.deleteTag(tag);
        }
        return INDEX_REDIRECT;
    }
}
