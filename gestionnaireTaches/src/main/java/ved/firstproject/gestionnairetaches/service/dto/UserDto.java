package ved.firstproject.gestionnairetaches.service.dto;

import java.util.Set;

public record UserDto (Long id, String username, String password, Set<TaskDto> tasks){
}
