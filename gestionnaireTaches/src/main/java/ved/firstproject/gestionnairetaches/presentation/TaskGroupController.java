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
import ved.firstproject.gestionnairetaches.service.dto.data.FilterTaskCriteriaGroup;
import ved.firstproject.gestionnairetaches.service.dto.data.TaskData;
import ved.firstproject.gestionnairetaches.service.dto.data.TaskForGroupData;
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
    public ResponseEntity<TaskGroupDto> addTaskToGroup(@RequestBody TaskForGroupData taskData) {
        TaskDto taskDto = new TaskDto(
                taskData.getTitle(),
                taskData.getDescription(),
                taskData.getStatus(),
                taskData.getPriority(),
                taskData.getDeadline(),
                taskData.getCompletionDate(),
                taskData.getCategory(),
                UserDto.toUserDto(serviceTaskManager.findUserById(taskData.getUserId())));
        TaskGroupDto taskGroupDto = serviceTaskManager.addTaskToGroup(taskData.getGroupId(), taskDto);
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

    @GetMapping("/findGroupByTitle")
    public ResponseEntity<TaskGroupDto> findGroupByTitle(@RequestParam String title) {
        TaskGroupDto taskGroups = serviceTaskManager.findGroupByTitle(title);
        logger.info("Task groups found: {}", taskGroups);
        return new ResponseEntity<>(taskGroups, HttpStatus.OK);
    }

    @PostMapping("/addUserToGroup")
    public ResponseEntity<TaskGroupDto> addUserToGroup(@RequestBody TaskGroupData taskGroupData) {
        TaskGroupDto taskGroupDto = serviceTaskManager.addUserToGroup(taskGroupData.getGroupId(), taskGroupData.getUserId());
        logger.info("User added to group: {}", taskGroupDto);
        return new ResponseEntity<>(taskGroupDto, HttpStatus.OK);
    }

    //TODO: Patch role ADMIN/MEMBER
    @PostMapping("/removeUserFromGroup")
    public ResponseEntity<TaskGroupDto> removeUserFromGroup(@RequestBody TaskGroupData taskGroupData) {
        TaskGroupDto taskGroupDto = serviceTaskManager.removeUserFromGroup(taskGroupData.getGroupId(), taskGroupData.getUserId());
        logger.info("User removed from group: {}", taskGroupDto);
        return new ResponseEntity<>(taskGroupDto, HttpStatus.OK);
    }

    @PostMapping("/completeTaskFromGroup")
    public ResponseEntity<TaskGroupDto> completeTaskFromGroup(@RequestBody TaskForGroupData taskData) {
        TaskGroupDto taskGroupDto = serviceTaskManager.completeTaskFromGroup(taskData.getGroupId(), taskData.getId());
        logger.info("Task completed from group: {}", taskGroupDto);
        return new ResponseEntity<>(taskGroupDto, HttpStatus.OK);
    }

    @GetMapping("/getTasksOfGroup")
    public ResponseEntity<Set<TaskDto>> getTasksOfGroup(@RequestParam Long groupId) {
        Set<TaskDto> tasks = serviceTaskManager.getTasksOfGroup(groupId);
        logger.info("Tasks of group found: {}", tasks);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @GetMapping("/getGroupsFromUserId")
    public ResponseEntity<Set<TaskGroupDto>> getGroupsFromUserId(@RequestParam Long userId) {
        Set<TaskGroupDto> taskGroups = serviceTaskManager.getGroupsFromUserId(userId);
        logger.info("Groups of user found: {}", taskGroups);
        return new ResponseEntity<>(taskGroups, HttpStatus.OK);
    }

    @PostMapping("/filterByCategoryGroup")
    public ResponseEntity<Set<TaskDto>> filterByCategoryGroup(@RequestBody FilterTaskCriteriaGroup taskGroupData) {
        Set<TaskDto> tasks = serviceTaskManager.filterByCategoryGroup(taskGroupData.getGroupId(), taskGroupData.getCategory());
        logger.info("Tasks filtered by category: {}", tasks);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @PostMapping("/assignTaskForGrTo")
    public ResponseEntity<TaskDto> assignTaskForGrTo(@RequestBody TaskForGroupData taskData) {
        TaskDto taskReassigned = serviceTaskManager.assignTaskForGrTo(taskData.getGroupId(), taskData.getUserId(), taskData.getId());
        logger.info("Task assigned to group: {}", taskReassigned);
        return new ResponseEntity<>(taskReassigned, HttpStatus.OK);
    }

}
