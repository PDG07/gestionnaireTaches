package ved.firstproject.gestionnairetaches.service.dto.data;

import lombok.Data;
import ved.firstproject.gestionnairetaches.model.Task;

import java.util.Set;

@Data
public class UserData {
    private String username;
    private String password;
    private Set<Task> tasks;
}
