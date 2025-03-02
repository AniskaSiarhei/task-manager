package com.example.task_manager.controller;

import com.example.task_manager.model.Task;
import com.example.task_manager.repository.TaskRepository;
import com.example.task_manager.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final TaskRepository taskRepository;

    private final UserRepository userRepository;

    public AdminController(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/tasks")
    public String getAllTasks(Model model) {
        model.addAttribute("tasks", taskRepository.findAll());
        return "admin-tasks";
    }

    @GetMapping("/tasks/edit/{id}")
    public String showEditTaskForm(@PathVariable("id") Long id, Model model) {
        Optional<Task> task = taskRepository.findById(id);
        if (task.isPresent()) {
            model.addAttribute("task", task.get());
            model.addAttribute("users", userRepository.findAll());
            return "admin-edit-task";
        }
        return "redirect:/admin/tasks";
    }

    @PostMapping("/tasks/edit/{id}")
    public String updateTask(@PathVariable("id") Long id,
                             @Valid @ModelAttribute("tasks") Task task,
                             BindingResult result,
                             Model model) {
        if (result.hasErrors()) {
            task.setId(id);
            model.addAttribute("user", userRepository.findAll());
            return "admin-edit-task";
        }
        task.setId(id);
        taskRepository.save(task);
        return "redirect:/admin/tasks";
    }

    @PostMapping("tasks/delete/{id}")
    public String deleteTask(@PathVariable("id") Long id) {
        taskRepository.deleteById(id);
        return "redirect:/admin/tasks";
    }

    @GetMapping("/users")
    public String getAllUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "admin-users";
    }

    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userRepository.deleteById(id);
        return "redirect:/admin/users";
    }
}
