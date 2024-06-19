package ved.firstproject.gestionnairetaches.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ved.firstproject.gestionnairetaches.model.enums.TaskCategory;
import ved.firstproject.gestionnairetaches.service.ServiceTaskManager;
import ved.firstproject.gestionnairetaches.service.dto.TaskDto;
import ved.firstproject.gestionnairetaches.service.dto.data.TaskData;
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
    public ResponseEntity<TaskDto> createTask(@RequestBody TaskData taskData) {
        Long userId = taskData.getUserId();
        TaskDto taskDto = new TaskDto(
                taskData.getTitle(),
                taskData.getDescription(),
                taskData.getStatus(),
                taskData.getPriority(),
                taskData.getDeadline(),
                taskData.getCompletionDate(),
                taskData.getCategory(),
                UserDto.toUserDto(serviceTaskManager.findUserById(userId)));
        TaskDto createdTaskDto = serviceTaskManager.createTask(userId, taskDto);
        logger.info("Task created: {}", createdTaskDto);
        return new ResponseEntity<>(createdTaskDto, HttpStatus.CREATED);
    }

    @GetMapping("/tasks")
    public ResponseEntity<Set<TaskDto>> getTasks(@RequestParam Long userId) {
        Set<TaskDto> tasks = serviceTaskManager.findAllTasksByUserId(userId);
        logger.info("Tasks found: {}", tasks);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @PutMapping("/updatetask")
    public ResponseEntity<TaskDto> updateTask(@RequestBody TaskData taskData) {
        Long userId = taskData.getUserId();
        TaskDto taskDto = new TaskDto(
                taskData.getId(),
                taskData.getTitle(),
                taskData.getDescription(),
                taskData.getStatus(),
                taskData.getPriority(),
                taskData.getDeadline(),
                taskData.getCompletionDate(),
                taskData.getCategory(),
                UserDto.toUserDto(serviceTaskManager.findUserById(userId)));
        TaskDto updatedTaskDto = serviceTaskManager.updateTask(userId, taskDto);
        logger.info("Task updated: {}", updatedTaskDto);
        return new ResponseEntity<>(updatedTaskDto, HttpStatus.OK);
    }

    @PutMapping("/completetask/{taskId}")
    public ResponseEntity<TaskDto> completeTask(@PathVariable Long taskId, @RequestParam Long userId) {
        TaskDto completedTaskDto = serviceTaskManager.completeTask(userId, taskId);
        logger.info("Task completed: {}", completedTaskDto);
        return new ResponseEntity<>(completedTaskDto, HttpStatus.OK);
    }

    @GetMapping("/completedtasks")
    public ResponseEntity<Set<TaskDto>> getCompletedTasks(@RequestParam Long userId) {
        Set<TaskDto> completedTasks = serviceTaskManager.completedTasks(userId);
        logger.info("Completed tasks found: {}", completedTasks);
        return new ResponseEntity<>(completedTasks, HttpStatus.OK);
    }

    @GetMapping("/tasks/filter")
    public ResponseEntity<Set<TaskDto>> getTasksByCategory(@RequestParam Long userId, @RequestParam TaskCategory category) {
        Set<TaskDto> tasks = serviceTaskManager.filterByCategory(userId, category);
        logger.info("Tasks found by category: {}", tasks);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }
}
