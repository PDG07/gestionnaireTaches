package ved.firstproject.gestionnairetaches.service.dto;

import ved.firstproject.gestionnairetaches.model.Task;
import ved.firstproject.gestionnairetaches.model.User;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public record TaskDto (Long id, String title, String description, String status, String priority, String deadline, String category, UserDto user){
    public TaskDto (String title, String description, String status, String priority, String deadline, String category, UserDto user){
        this(null, title, description, status, priority, deadline, category, user);
    }

    public static TaskDto toTaskDto (Task task) {
        Objects.requireNonNull(task);
        TaskDto taskDto = new TaskDto(task.getId(), task.getTitle(), task.getDescription(), task.getStatus(), task.getPriority(), task.getDeadline(), task.getCategory(), UserDto.toUserDto(task.getUser()));
        //UserDto userDto = new UserDto(task.getUser().getId(), task.getUser().getUsername(), task.getUser().getPassword(), task.getUser().getTasks().stream().map(TaskDto::toTaskDto).collect(Collectors.toSet()));
        return taskDto;
    }

    public static Task toTask(TaskDto taskDto) {
        Objects.requireNonNull(taskDto);
        User user = new User(taskDto.user().id(), taskDto.user().username(), taskDto.user().password(), taskDto.user().tasks().stream().map(TaskDto::toTask).collect(Collectors.toSet()));
        return new Task(taskDto.id(), taskDto.title(), taskDto.description(), taskDto.status(), taskDto.priority(), taskDto.deadline(), taskDto.category(), user);
    }

    public static Set<TaskDto> toListTaskDto (Set<Task> tasks) {
        Objects.requireNonNull(tasks);
        return tasks.stream().map(TaskDto::toTaskDto).collect(Collectors.toSet());
    }
}
