package ved.firstproject.gestionnairetaches.service.dto;

import ved.firstproject.gestionnairetaches.model.TaskGroup;

import java.util.Set;
import java.util.stream.Collectors;

public record TaskGroupDto (Long id, String title, Set<UserDto> usersGroup, Set<TaskDto> tasksGroup, Set<TaskDto> tasksGroupHistory){
    public static TaskGroupDto toTaskGroupDto(TaskGroup taskGroup) {
        return new TaskGroupDto(taskGroup.getId(), taskGroup.getTitle(), taskGroup.getUsersGroup().stream().map(UserDto::toUserDto).collect(Collectors.toSet()), taskGroup.getTasksGroup().stream().map(TaskDto::toTaskDto).collect(Collectors.toSet()), taskGroup.getTasksGroupHistory().stream().map(TaskDto::toTaskDto).collect(Collectors.toSet()));
    }
}
