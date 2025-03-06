package com.example.task_manager.controller;

import com.example.task_manager.model.User;
import com.example.task_manager.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
    public String register(@Valid @ModelAttribute("user") User user,
                           BindingResult bindingResult,
                           @RequestParam("repeatPassword") String repeatPassword,
                           Model model) {
        if (bindingResult.hasErrors()) {
            return "register";
        }
        if (user.getPassword().length() < 6 || user.getPassword().length() > 8) {
            model.addAttribute("error", "Пароль должен быть длиной от 6 до 8 символов");
            return "register";
        }
        if (!user.getPassword().equals(repeatPassword)) {
            model.addAttribute("error", "Пароли не совпадают");
            return "register";
        } try {
            userService.saveUser(user, false);      // Все новые пользователи - не админы
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
        return "redirect:/login";
    }

}
