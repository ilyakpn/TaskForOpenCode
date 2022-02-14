package com.test.taskopencode.controller;

import com.test.taskopencode.model.User;
import com.test.taskopencode.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class RegistrationController {


    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @RequestMapping(value="/registration", method=RequestMethod.GET)
    public String registration(Model model) {

        model.addAttribute("user", new User());

        return "registration";
    }

    @RequestMapping(value="/registration", method=RequestMethod.POST)
    public String registration(@ModelAttribute("user") User user, Model model) {

        if (userDetailsService.saveUser(user)) {
            return "redirect:/auth/login";
        }

        model.addAttribute("usernameError", "Пользователь с таким именем уже существует");
        return "/registration";
    }

}
