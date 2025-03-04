package com.example.task_manager.controller;

import com.example.task_manager.model.User;
import com.example.task_manager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String register(User user,
                           @RequestParam("repeatPassword") String repeatPassword,
                           Model model) {
        if (!user.getPassword().equals(repeatPassword)) {
            model.addAttribute("error", "Passwords do not match");
            return "register";
        }
        userService.saveUser(user, false);      // Все новые пользователи - не админы
        return "redirect:/login";
    }

}
