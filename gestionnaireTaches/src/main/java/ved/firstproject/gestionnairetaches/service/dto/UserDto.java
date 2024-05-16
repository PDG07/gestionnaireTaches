package ved.firstproject.gestionnairetaches.service.dto;

import ved.firstproject.gestionnairetaches.model.User;

import java.util.Set;
import java.util.stream.Collectors;

public record UserDto (Long id, String username, String password, Set<TaskDto> tasks){

    public static UserDto toUserDto(User user) {
        return new UserDto(user.getId(), user.getUsername(), user.getPassword(), TaskDto.toListTaskDto(user.getTasks()));
    }

    public static User toUser(UserDto userDto) {
        return new User(userDto.id(), userDto.username(), userDto.password(), userDto.tasks().stream().map(TaskDto::toTask).collect(Collectors.toSet()));
    }
}
