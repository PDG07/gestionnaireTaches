package ved.firstproject.gestionnairetaches.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ved.firstproject.gestionnairetaches.service.ServiceTaskManager;
import ved.firstproject.gestionnairetaches.service.dto.UserDto;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(TaskManagerController.class);
    private final ServiceTaskManager serviceTaskManager;

    public UserController(ServiceTaskManager serviceTaskManager) {
        this.serviceTaskManager = serviceTaskManager;
    }

    @GetMapping("/findUserByUsername")
    public ResponseEntity<UserDto> findUserByUsername(@RequestParam String username) {
        UserDto userDto = serviceTaskManager.findUserByUsername(username);
        logger.info("User found: {}", userDto);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }
}
