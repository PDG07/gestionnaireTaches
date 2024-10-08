package ved.firstproject.gestionnairetaches.service.dto;

import ved.firstproject.gestionnairetaches.model.Task;
import ved.firstproject.gestionnairetaches.model.enums.TaskCategory;
import ved.firstproject.gestionnairetaches.model.enums.TaskPriority;
import ved.firstproject.gestionnairetaches.model.enums.TaskState;
import ved.firstproject.gestionnairetaches.model.User;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public record TaskDto (Long id, String title, String description, TaskState status, TaskPriority priority, LocalDate deadline, LocalDate completionDate, TaskCategory category, UserDto user){
    public TaskDto (String title, String description, TaskState status, TaskPriority priority, LocalDate deadline, LocalDate completionDate, TaskCategory category, UserDto user){
        this(null, title, description, status, priority, deadline, completionDate, category, user);
    }

    public static TaskDto toTaskDto (Task task) {
        Objects.requireNonNull(task);
        UserDto userDto = new UserDto(task.getUser().getId(),
                task.getUser().getUsername(),
                task.getUser().getPassword(),
                Set.of());
        return new TaskDto(task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getDeadline(),
                task.getCompletionDate(),
                task.getCategory(),
                userDto);
    }

    public static Task toTask(TaskDto taskDto) {
        Objects.requireNonNull(taskDto);
        User user = new User(taskDto.user().id(),
                taskDto.user().username(),
                taskDto.user().password(),
                taskDto.user().tasks().stream().map(TaskDto::toTask).collect(Collectors.toSet()));
        return new Task(taskDto.id(),
                taskDto.title(),
                taskDto.description(),
                taskDto.status(),
                taskDto.priority(),
                taskDto.deadline(),
                taskDto.completionDate(),
                taskDto.category(), user);
    }

    public static Set<TaskDto> toListTaskDto (Set<Task> tasks) {
        Objects.requireNonNull(tasks);
        return tasks.stream().map(TaskDto::toTaskDto).collect(Collectors.toSet());
    }
}
