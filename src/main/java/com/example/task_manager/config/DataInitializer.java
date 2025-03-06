package com.example.task_manager.config;

import com.example.task_manager.model.User;
import com.example.task_manager.repository.UserRepository;
import com.example.task_manager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder, UserService userService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // Проверяем, существует ли уже администратор
        if (userRepository.findByEmail("admin@mail.ru").isEmpty()) {
            User admin = new User();
            admin.setUsername("Admin");
            admin.setEmail("admin@mail.ru");
            admin.setPassword("123456");
            admin.setRoles(Arrays.asList("USER", "ADMIN"));
            userService.saveUser(admin,true);
//            userRepository.save(admin);
            System.out.println("Администратор создан: userName=admin, password=12345");
        } else {
            System.out.println("Администратор уже существует.");
        }
    }
}
