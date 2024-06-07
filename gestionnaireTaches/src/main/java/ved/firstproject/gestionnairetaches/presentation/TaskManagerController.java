package ved.firstproject.gestionnairetaches.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ved.firstproject.gestionnairetaches.service.ServiceTaskManager;
import ved.firstproject.gestionnairetaches.service.dto.TaskDto;
import ved.firstproject.gestionnairetaches.service.dto.data.UserData;
import ved.firstproject.gestionnairetaches.service.dto.UserDto;

import java.util.Set;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class TaskManagerController {
    private static final Logger logger = LoggerFactory.getLogger(TaskManagerController.class);
    private final ServiceTaskManager serviceTaskManager;


    public TaskManagerController(ServiceTaskManager serviceTaskManager) {
        this.serviceTaskManager = serviceTaskManager;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDto> createUser(@RequestBody UserData userData) {
        UserDto userDto = new UserDto(null, userData.getUsername(), userData.getPassword(), Set.of());
        UserDto createdUserDto = serviceTaskManager.createUser(userDto);
        logger.info("User created: {}", createdUserDto);
        return new ResponseEntity<>(createdUserDto, HttpStatus.CREATED);
    }

    //TODO @PostMapping("/login")

    @PostMapping("/createtask")
    public ResponseEntity<TaskDto> createTask(@RequestParam Long userId, @RequestBody TaskDto taskDto) {
        TaskDto createdTaskDto = serviceTaskManager.createTask(userId, taskDto);
        logger.info("Task created: {}", createdTaskDto);
        return new ResponseEntity<>(createdTaskDto, HttpStatus.CREATED);
    }
}
