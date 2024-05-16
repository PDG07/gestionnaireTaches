package ved.firstproject.gestionnairetaches.service.dto;

import ved.firstproject.gestionnairetaches.model.User;

import java.util.Set;

public record UserDto (Long id, String username, String password, Set<TaskDto> tasks){
    public static UserDto toUserDto(User user) {
        return new UserDto(user.getId(), user.getUsername(), user.getPassword(), Set.of());
    }
}
