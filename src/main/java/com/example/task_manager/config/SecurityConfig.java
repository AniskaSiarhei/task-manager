package com.example.task_manager.config;

import com.example.task_manager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/login", "/register", "/css/**", "/js/**", "/images/**").permitAll() // доступ без авторизации
                        .requestMatchers("/admin/**").hasRole("ADMIN")      // Только для админов
                        .requestMatchers("tasks/**").hasRole("USER")        // Для всех пользователей
                        .anyRequest().authenticated()       // все остальное требует авторизации
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .permitAll()
                        .defaultSuccessUrl("/tasks", true) // перенаправление после успешного входа
                        .failureHandler(((request, response, exception) -> {
                            String errorMessage = "Неверный email или пароль";
                            if (exception instanceof UsernameNotFoundException) {
                                errorMessage = "Пользователь с таким email не найден";
                            } else if (exception instanceof BadCredentialsException) {
                                errorMessage = "Неверный пароль";
                            }
                            request.getSession().setAttribute("error", errorMessage);
                            response.sendRedirect("/login?error");
                        }))
                )
                .logout((logout) -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // указываем URL выхода
                        .permitAll()
                        .logoutSuccessUrl("/login?logout")
                )
                .exceptionHandling((exceptionHandling) -> exceptionHandling
                        .accessDeniedPage("/403")
                );
                return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}





