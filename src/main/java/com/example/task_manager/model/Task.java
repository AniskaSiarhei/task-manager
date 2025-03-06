package com.example.task_manager.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Название не может быть пустым")
    @Size(min = 2, max = 100, message = "Название должно быть от 2 до 100 символов")
    private String title;

    @Size(max = 100, message = "Описание не должно превышать 100 символов")
    private String description;

    @NotNull(message = "Дедлайн обязателен")
    private LocalDateTime deadline;

    @NotBlank(message = "Приоритет обязателен")
    private String priority;    // "LOW", "MEDIUM", "HIGH"
    private boolean completed;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Transient
    private boolean isOverdue;

    public Task() {
    }

    public Long getId() {
        return this.id;
    }

    public @NotBlank(message = "Название не может быть пустым") @Size(min = 2, max = 100, message = "Название должно быть от 2 до 100 символов") String getTitle() {
        return this.title;
    }

    public @Size(max = 100, message = "Описание не должно превышать 100 символов") String getDescription() {
        return this.description;
    }

    public @NotNull(message = "Дедлайн обязателен") LocalDateTime getDeadline() {
        return this.deadline;
    }

    public @NotBlank(message = "Приоритет обязателен") String getPriority() {
        return this.priority;
    }

    public boolean isCompleted() {
        return this.completed;
    }

    public User getUser() {
        return this.user;
    }

    public boolean isOverdue() {
        return this.isOverdue;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(@NotBlank(message = "Название не может быть пустым") @Size(min = 2, max = 100, message = "Название должно быть от 2 до 100 символов") String title) {
        this.title = title;
    }

    public void setDescription(@Size(max = 100, message = "Описание не должно превышать 100 символов") String description) {
        this.description = description;
    }

    public void setDeadline(@NotNull(message = "Дедлайн обязателен") LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public void setPriority(@NotBlank(message = "Приоритет обязателен") String priority) {
        this.priority = priority;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setOverdue(boolean isOverdue) {
        this.isOverdue = isOverdue;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Task)) return false;
        final Task other = (Task) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$title = this.getTitle();
        final Object other$title = other.getTitle();
        if (this$title == null ? other$title != null : !this$title.equals(other$title)) return false;
        final Object this$description = this.getDescription();
        final Object other$description = other.getDescription();
        if (this$description == null ? other$description != null : !this$description.equals(other$description))
            return false;
        final Object this$deadline = this.getDeadline();
        final Object other$deadline = other.getDeadline();
        if (this$deadline == null ? other$deadline != null : !this$deadline.equals(other$deadline)) return false;
        final Object this$priority = this.getPriority();
        final Object other$priority = other.getPriority();
        if (this$priority == null ? other$priority != null : !this$priority.equals(other$priority)) return false;
        if (this.isCompleted() != other.isCompleted()) return false;
        final Object this$user = this.getUser();
        final Object other$user = other.getUser();
        if (this$user == null ? other$user != null : !this$user.equals(other$user)) return false;
        if (this.isOverdue() != other.isOverdue()) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Task;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $title = this.getTitle();
        result = result * PRIME + ($title == null ? 43 : $title.hashCode());
        final Object $description = this.getDescription();
        result = result * PRIME + ($description == null ? 43 : $description.hashCode());
        final Object $deadline = this.getDeadline();
        result = result * PRIME + ($deadline == null ? 43 : $deadline.hashCode());
        final Object $priority = this.getPriority();
        result = result * PRIME + ($priority == null ? 43 : $priority.hashCode());
        result = result * PRIME + (this.isCompleted() ? 79 : 97);
        final Object $user = this.getUser();
        result = result * PRIME + ($user == null ? 43 : $user.hashCode());
        result = result * PRIME + (this.isOverdue() ? 79 : 97);
        return result;
    }

    public String toString() {
        return "Task(id=" + this.getId() + ", title=" + this.getTitle() + ", description=" + this.getDescription() + ", deadline=" + this.getDeadline() + ", priority=" + this.getPriority() + ", completed=" + this.isCompleted() + ", user=" + this.getUser() + ", isOverdue=" + this.isOverdue() + ")";
    }
}
