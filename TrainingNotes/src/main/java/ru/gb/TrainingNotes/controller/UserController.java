package ru.gb.TrainingNotes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.gb.TrainingNotes.model.User;
import ru.gb.TrainingNotes.service.UserService;


import java.util.List;


@Controller
public class UserController {
    private UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/")
    public String home() {
        return "home";
    }
    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }
    @GetMapping("register")
    public String showRegistrationForm(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "register";
    }
    @PostMapping("/register/save")
    public String registration(@ModelAttribute("user") User user, Model model) {
        User existing = userService.findByLogin(user.getLogin());
        if (existing != null) {
            model.addAttribute("error", "Account already registered");
            model.addAttribute("user", user);
            return "register";
        }
        userService.saveUser(user);
        return "redirect:/register?success";
    }
    @GetMapping("/users")
    public String listRegisteredUsers(Model model) {
        List<User> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "users";
    }
}

