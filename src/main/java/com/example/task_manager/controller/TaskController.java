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

import java.util.Optional;

@Controller
public class TaskController {

    private final TaskRepository taskRepository;

    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private boolean isAdmin() {
        return getCurrentUser().getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
    }

    @GetMapping("/tasks")
    public String getAllTasks(Model model) {
        User currentUser = getCurrentUser();
            model.addAttribute("tasks", taskRepository.findByUser(currentUser));
        return "tasks";
    }

    @GetMapping("/tasks/add")       // displays the form
    public String showAddTaskForm(Model model) {
        model.addAttribute("task", new Task());
        return "add-task";
    }

    @PostMapping("tasks/add")       // saves the task in the database and redirects it to the task list
    public String addTask(@Valid @ModelAttribute("task") Task task, BindingResult result) {
        if (result.hasErrors()) {
            return "add-task";
        }

        task.setCompleted(false);   //
        task.setUser(getCurrentUser());
        taskRepository.save(task);
        return "redirect:/tasks";
    }

    @GetMapping("/tasks/edit/{id}")
    public String showEditTaskForm(@PathVariable("id") Long id, Model model) {

        Optional<Task> task = taskRepository.findById(id);
        if (task.isPresent()) {
            User currentUser = getCurrentUser();
            if (isAdmin() || task.get().getUser().getId().equals(currentUser.getId())) {
                model.addAttribute("task", task.get());
                return "edit-task";
            }
        }
        return "redirect:/tasks";
    }

    @PostMapping("/tasks/edit/{id}")
    public String updateTask(@PathVariable("id") Long id,
                             @Valid @ModelAttribute("task") Task task,
                             BindingResult result,
                             Model model) {

        if (result.hasErrors()) {
            task.setId(id);
            return "edit-task";
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

    @PostMapping("/tasks/delete/{id}")
    public String deleteTask(@PathVariable("id") Long id) {
        Optional<Task> task = taskRepository.findById(id);
        if (task.isPresent()) {
            User currentUser = getCurrentUser();
            if (isAdmin() || task.get().getUser().getId().equals(currentUser.getId())) {
                taskRepository.delete(task.get());
            }
        }
        return "redirect:/tasks";
    }
}









