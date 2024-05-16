package ved.firstproject.gestionnairetaches.service.dto;

import ved.firstproject.gestionnairetaches.model.Task;
import ved.firstproject.gestionnairetaches.model.User;

import java.util.Set;
import java.util.stream.Collectors;

public record TaskDto (Long id, String title, String description, String status, String priority, String deadline, String category, UserDto user){
    public TaskDto (String title, String description, String status, String priority, String deadline, String category, UserDto user){
        this(null, title, description, status, priority, deadline, category, user);
    }

    public static TaskDto toTaskDto (Task task) {
        UserDto userDto = new UserDto(task.getUser().getId(), task.getUser().getUsername(), task.getUser().getPassword(), task.getUser().getTasks().stream().map(TaskDto::toTaskDto).collect(Collectors.toSet()));
        return new TaskDto(task.getId(), task.getTitle(), task.getDescription(), task.getStatus(), task.getPriority(), task.getDeadline(), task.getCategory(), userDto);
    }

    public static Task toTask(TaskDto taskDto) {
        User user = new User(taskDto.user().id(), taskDto.user().username(), taskDto.user().password(), taskDto.user().tasks().stream().map(TaskDto::toTask).collect(Collectors.toSet()));
        return new Task(taskDto.id(), taskDto.title(), taskDto.description(), taskDto.status(), taskDto.priority(), taskDto.deadline(), taskDto.category(), user);
    }

    public static Set<TaskDto> toListTaskDto (Set<Task> tasks) {
        return tasks.stream().map(TaskDto::toTaskDto).collect(Collectors.toSet());
    }
}