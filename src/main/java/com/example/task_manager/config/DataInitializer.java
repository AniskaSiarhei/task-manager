package com.example.task_manager.config;

import com.example.task_manager.model.User;
import com.example.task_manager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataInitializer implements ApplicationRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // Проверяем, существует ли уже администратор
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@gmail.com");
            admin.setPassword(passwordEncoder.encode("12345"));
            admin.setRoles(Arrays.asList("USER", "ADMIN"));
            userRepository.save(admin);
            System.out.println("Администратор создан: userName=admin, password=12345");
        } else {
            System.out.println("Администратор уже существует");
        }
    }
}
