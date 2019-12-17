package org.xtremebiker.jsfspring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.xtremebiker.jsfspring.entity.Rle;
import org.xtremebiker.jsfspring.entity.UserProfile;
import org.xtremebiker.jsfspring.repository.UserProfileRepository;

import java.util.Collections;

@Controller
public class MainController {

    @Autowired
    private UserProfileRepository userProfileRepository;

    //Мы могли бы расписать эти 2 маппинга отдельно, но смысла дублировать одинаковый код нет.
    // этот метод будет слушать запросы на "/" и "/index"
    @GetMapping(value = {"/", "/index"})
    public String index() {
        return "/welcomePage.xhtml";
    }

    @GetMapping("/login")
    public String admin() {
        return "/loginPage.xhtml";
    }

    @GetMapping("/registration")
    public String registtration() {
        return "/registrationPage.xhtml";
    }

    @PostMapping("/registration")
    public String addUser(UserProfile user) {
        user.setFio("test");
        user.setRoles(Collections.singleton(Rle.USER));
        userProfileRepository.save(user);
        return "/registrationPage.xhtml";
    }

    @GetMapping("/home")
    public String home() {
        return "/welcomePage.xhtml";
    }

    @GetMapping("/services")
    public String services() {
        return "/servicesPage.xhtml";
    }

    @GetMapping("/timetable")
    public String timetable() {
        return "/timetablePage.xhtml";
    }

    @GetMapping("/contacts")
    public String contacts() {
        return "/contactsPage.xhtml";
    }

    @GetMapping("/create_training")
    public String createTraining() {
        return "/createTrainingPage.xhtml";
    }

}