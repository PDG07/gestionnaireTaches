package ved.firstproject.gestionnairetaches.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ved.firstproject.gestionnairetaches.service.ServiceTaskManager;
import ved.firstproject.gestionnairetaches.service.dto.TaskDto;
import ved.firstproject.gestionnairetaches.service.dto.TaskGroupDto;
import ved.firstproject.gestionnairetaches.service.dto.UserDto;
import ved.firstproject.gestionnairetaches.service.dto.data.TaskData;
import ved.firstproject.gestionnairetaches.service.dto.data.TaskGroupData;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    @PostMapping("/addTask")
    public ResponseEntity<TaskGroupDto> addTaskToGroup(@RequestParam Long taskGroupId, @RequestBody TaskData taskData) {
        TaskDto taskDto = new TaskDto(
                taskData.getTitle(),
                taskData.getDescription(),
                taskData.getStatus(),
                taskData.getPriority(),
                taskData.getDeadline(),
                taskData.getCompletionDate(),
                taskData.getCategory(),
                UserDto.toUserDto(serviceTaskManager.findUserById(taskData.getUserId())));
        TaskGroupDto taskGroupDto = serviceTaskManager.addTaskToGroup(taskGroupId, taskDto);
        logger.info("Task added to group: {}", taskGroupDto);
        return new ResponseEntity<>(taskGroupDto, HttpStatus.OK);
    }

    @GetMapping("/findGroupById")
    public ResponseEntity<Set<TaskGroupDto>> findGroupById(@RequestParam("groupIds") String groupIdsParam) throws JsonProcessingException {
        List<Long> groupIds = Arrays.asList(new ObjectMapper().readValue(groupIdsParam, Long[].class));
        Set<TaskGroupDto> taskGroups = groupIds.stream()
                .map(serviceTaskManager::findGroupById)
                .collect(Collectors.toSet());
        logger.info("Task groups found: {}", taskGroups);
        return new ResponseEntity<>(taskGroups, HttpStatus.OK);
    }

}
