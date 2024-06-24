package ved.firstproject.gestionnairetaches.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ved.firstproject.gestionnairetaches.service.ServiceTaskManager;
import ved.firstproject.gestionnairetaches.service.dto.TaskGroupDto;
import ved.firstproject.gestionnairetaches.service.dto.data.TaskGroupData;

@RestController
@RequestMapping("/api/group")
@CrossOrigin(origins = "http://localhost:3000")
public class TaskGroupController {
    private static final Logger logger = LoggerFactory.getLogger(TaskManagerController.class);
    private final ServiceTaskManager serviceTaskManager;

    public TaskGroupController(ServiceTaskManager serviceTaskManager) {
        this.serviceTaskManager = serviceTaskManager;
    }

    @PostMapping("/create")
    public ResponseEntity<TaskGroupDto> createTaskGroup(@RequestBody TaskGroupData taskGroupData) {
        TaskGroupDto taskGroupDto = serviceTaskManager.createTaskGroup(taskGroupData.getTitle(), taskGroupData.getUserId());
        logger.info("Task group created: {}", taskGroupDto);
        return new ResponseEntity<>(taskGroupDto, HttpStatus.CREATED);
    }
}
