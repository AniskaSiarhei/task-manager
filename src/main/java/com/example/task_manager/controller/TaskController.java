package com.example.task_manager.controller;

import com.example.task_manager.model.Task;
import com.example.task_manager.model.User;
import com.example.task_manager.repository.TaskRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/tasks") // Добавим базовый путь для всех методов
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private boolean isAdmin() {
        return getCurrentUser().getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
    }

    @GetMapping
    public String getAllTasks(Model model) {
        User currentUser = getCurrentUser();
        List<Task> allTasks = taskRepository.findByUser(currentUser);
        List<Task> incompleteTasks = allTasks.stream()
                .filter(task -> !task.isCompleted())
                .collect(Collectors.toList());
        List<Task> completedTasks = allTasks.stream()
                .filter(Task::isCompleted)
                .collect(Collectors.toList());

        model.addAttribute("incompleteTasks", incompleteTasks);
        model.addAttribute("completedTasks", completedTasks);
        model.addAttribute("displayName", currentUser.getDisplayName());
        return "tasks";
    }

    @GetMapping("/add")
    public String showAddTaskForm(Model model) {
        model.addAttribute("task", new Task());
        return "fragments/add-task-modal";
    }

    @PostMapping("/add")
    public String addTask(@Valid @ModelAttribute("task") Task task, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "fragments/add-task-modal";
        }
        task.setCompleted(false);
        task.setUser(getCurrentUser());
        taskRepository.save(task);
        return "redirect:/tasks";
    }

    @GetMapping("/edit/{id}")
    public String showEditTaskForm(@PathVariable("id") Long id, Model model) {
        Optional<Task> task = taskRepository.findById(id);
        if (task.isPresent()) {
            User currentUser = getCurrentUser();
            if (isAdmin() || task.get().getUser().getId().equals(currentUser.getId())) {
                model.addAttribute("task", task.get());
                return "fragments/edit-task-modal";
            }
        }
        return "redirect:/tasks";
    }

    @PostMapping("/edit/{id}")
    public String updateTask(@PathVariable("id") Long id, @Valid @ModelAttribute("task") Task task,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            task.setId(id);
            return "fragments/edit-task-modal";
        }
        Optional<Task> existingTask = taskRepository.findById(id);
        if (existingTask.isPresent()) {
            User currentUser = getCurrentUser();
            if (isAdmin() || existingTask.get().getUser().getId().equals(currentUser.getId())) {
                task.setId(id);
                task.setUser(existingTask.get().getUser());
                taskRepository.save(task);
            }
        }
        return "redirect:/tasks";
    }

    @PostMapping("/delete/{id}")
    public String deleteTask(@PathVariable("id") Long id) {
        Optional<Task> task = taskRepository.findById(id);
        if (task.isPresent()) {
            User currentUser = getCurrentUser();
            if (isAdmin() || task.get().getUser().getId().equals(currentUser.getId())) {
                taskRepository.deleteById(id);
            }
        }
        return "redirect:/tasks";
    }

    @PostMapping("/complete/{id}")
    public String completeTask(@PathVariable("id") Long id) {
        System.out.println("Запрос на завершение задачи ID: " + id); // Отладка
        Optional<Task> task = taskRepository.findById(id);
        if (task.isPresent()) {
            User currentUser = getCurrentUser();
            if (isAdmin() || task.get().getUser().getId().equals(currentUser.getId())) {
                Task existingTask = task.get();
                existingTask.setCompleted(true);
                taskRepository.saveAndFlush(existingTask);
                System.out.println("Задача завершена: " + existingTask.getId());
            }
        }
        return "redirect:/tasks";
    }
}









