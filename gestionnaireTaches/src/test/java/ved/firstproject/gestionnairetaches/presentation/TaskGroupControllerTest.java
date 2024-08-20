package ved.firstproject.gestionnairetaches.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ved.firstproject.gestionnairetaches.service.ServiceTaskManager;
import ved.firstproject.gestionnairetaches.service.dto.TaskDto;
import ved.firstproject.gestionnairetaches.service.dto.TaskGroupDto;
import ved.firstproject.gestionnairetaches.service.dto.UserDto;
import ved.firstproject.gestionnairetaches.service.dto.data.TaskForGroupData;
import ved.firstproject.gestionnairetaches.service.dto.data.TaskGroupData;
import ved.firstproject.gestionnairetaches.service.dto.data.FilterTaskCriteriaGroup;
import ved.firstproject.gestionnairetaches.model.enums.TaskCategory;
import ved.firstproject.gestionnairetaches.model.enums.TaskPriority;
import ved.firstproject.gestionnairetaches.model.enums.TaskState;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskGroupControllerTest {

    @Mock
    private ServiceTaskManager serviceTaskManager;

    @InjectMocks
    private TaskGroupController taskGroupController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTaskGroup() {
        TaskGroupData taskGroupData = new TaskGroupData();
        taskGroupData.setTitle("Group Title");
        taskGroupData.setUserId(1L);

        TaskGroupDto taskGroupDto = new TaskGroupDto(1L, "Group Title", Set.of(), Set.of());
        when(serviceTaskManager.createTaskGroup(anyString(), anyLong())).thenReturn(taskGroupDto);

        ResponseEntity<TaskGroupDto> response = taskGroupController.createTaskGroup(taskGroupData);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(taskGroupDto, response.getBody());
        verify(serviceTaskManager).createTaskGroup("Group Title", 1L);
    }

    @Test
    void addTaskToGroup() {
        TaskForGroupData taskData = new TaskForGroupData();
        taskData.setTitle("Task Title");
        taskData.setDescription("Task Description");
        taskData.setStatus(TaskState.TODO);
        taskData.setPriority(TaskPriority.HIGH);
        taskData.setDeadline(LocalDate.now().plusDays(1));
        taskData.setCompletionDate(LocalDate.now().plusDays(2));
        taskData.setCategory(TaskCategory.WORK);
        taskData.setUserId(1L);
        taskData.setGroupId(1L);

        UserDto userDto = new UserDto(1L, "testuser", "password", Set.of());
        when(serviceTaskManager.findUserById(1L)).thenReturn(UserDto.toUser(userDto));

        TaskDto taskDto = new TaskDto(
                "Task Title",
                "Task Description",
                TaskState.TODO,
                TaskPriority.HIGH,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(2),
                TaskCategory.WORK,
                userDto
        );

        TaskGroupDto taskGroupDto = new TaskGroupDto(1L, "Group Title", Set.of(), Set.of(taskDto));
        when(serviceTaskManager.addTaskToGroup(anyLong(), any(TaskDto.class))).thenReturn(taskGroupDto);

        ResponseEntity<TaskGroupDto> response = taskGroupController.addTaskToGroup(taskData);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(taskGroupDto, response.getBody());
        verify(serviceTaskManager).addTaskToGroup(1L, taskDto);
        verify(serviceTaskManager).findUserById(1L);
    }

    @Test
    void findGroupById() throws JsonProcessingException {
        TaskGroupDto taskGroupDto = new TaskGroupDto(1L, "Group Title", Set.of(), Set.of());
        when(serviceTaskManager.findGroupById(1L)).thenReturn(taskGroupDto);

        ResponseEntity<Set<TaskGroupDto>> response = taskGroupController.findGroupById("[1]");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).contains(taskGroupDto));
        verify(serviceTaskManager).findGroupById(1L);
    }

    @Test
    void findGroupByTitle() {
        TaskGroupDto taskGroupDto = new TaskGroupDto(1L, "Group Title", Set.of(), Set.of());
        when(serviceTaskManager.findGroupByTitle("Group Title")).thenReturn(taskGroupDto);

        ResponseEntity<TaskGroupDto> response = taskGroupController.findGroupByTitle("Group Title");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(taskGroupDto, response.getBody());
        verify(serviceTaskManager).findGroupByTitle("Group Title");
    }

    @Test
    void addUserToGroup() {
        TaskGroupData taskGroupData = new TaskGroupData();
        taskGroupData.setGroupId(1L);
        taskGroupData.setUserId(2L);

        TaskGroupDto taskGroupDto = new TaskGroupDto(1L, "Group Title", Set.of(), Set.of());
        when(serviceTaskManager.addUserToGroup(anyLong(), anyLong())).thenReturn(taskGroupDto);

        ResponseEntity<TaskGroupDto> response = taskGroupController.addUserToGroup(taskGroupData);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(taskGroupDto, response.getBody());
        verify(serviceTaskManager).addUserToGroup(1L, 2L);
    }

    @Test
    void removeUserFromGroup() {
        TaskGroupData taskGroupData = new TaskGroupData();
        taskGroupData.setGroupId(1L);
        taskGroupData.setUserId(2L);

        TaskGroupDto taskGroupDto = new TaskGroupDto(1L, "Group Title", Set.of(), Set.of());
        when(serviceTaskManager.removeUserFromGroup(anyLong(), anyLong())).thenReturn(taskGroupDto);

        ResponseEntity<TaskGroupDto> response = taskGroupController.removeUserFromGroup(taskGroupData);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(taskGroupDto, response.getBody());
        verify(serviceTaskManager).removeUserFromGroup(1L, 2L);
    }

    @Test
    void completeTaskFromGroup() {
        TaskForGroupData taskData = new TaskForGroupData();
        taskData.setGroupId(1L);
        taskData.setId(1L);

        TaskGroupDto taskGroupDto = new TaskGroupDto(1L, "Group Title", Set.of(), Set.of());
        when(serviceTaskManager.completeTaskFromGroup(anyLong(), anyLong())).thenReturn(taskGroupDto);

        ResponseEntity<TaskGroupDto> response = taskGroupController.completeTaskFromGroup(taskData);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(taskGroupDto, response.getBody());
        verify(serviceTaskManager).completeTaskFromGroup(1L, 1L);
    }

    @Test
    void getTasksOfGroup() {
        TaskDto taskDto = new TaskDto(
                "Task Title", "Task Description", TaskState.TODO, TaskPriority.HIGH,
                LocalDate.now().plusDays(1), LocalDate.now().plusDays(2),
                TaskCategory.WORK, null
        );
        Set<TaskDto> tasks = Set.of(taskDto);
        when(serviceTaskManager.getTasksOfGroup(anyLong())).thenReturn(tasks);

        ResponseEntity<Set<TaskDto>> response = taskGroupController.getTasksOfGroup(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains(taskDto));
        verify(serviceTaskManager).getTasksOfGroup(1L);
    }

    @Test
    void getGroupsFromUserId() {
        TaskGroupDto taskGroupDto = new TaskGroupDto(1L, "Group Title", Set.of(), Set.of());
        Set<TaskGroupDto> taskGroups = Set.of(taskGroupDto);
        when(serviceTaskManager.getGroupsFromUserId(anyLong())).thenReturn(taskGroups);

        ResponseEntity<Set<TaskGroupDto>> response = taskGroupController.getGroupsFromUserId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains(taskGroupDto));
        verify(serviceTaskManager).getGroupsFromUserId(1L);
    }

    @Test
    void updateTaskForGroup() {
        // Créer des données de test pour TaskForGroupData
        TaskForGroupData taskData = new TaskForGroupData();
        taskData.setTitle("Updated Task Title");
        taskData.setDescription("Updated Task Description");
        taskData.setStatus(TaskState.COMPLETED);
        taskData.setPriority(TaskPriority.AVERAGE);
        taskData.setDeadline(LocalDate.now().plusDays(3));
        taskData.setCompletionDate(LocalDate.now().plusDays(4));
        taskData.setCategory(TaskCategory.PERSONAL);
        taskData.setUserId(1L);
        taskData.setGroupId(1L);
        taskData.setId(1L);

        // Créer un UserDto simulé (mock) pour l'utilisateur associé à la tâche
        UserDto userDto = new UserDto(1L, "testuser", "password", Set.of());

        // Simuler la réponse de serviceTaskManager.findUserById
        when(serviceTaskManager.findUserById(1L)).thenReturn(UserDto.toUser(userDto));

        // Créer un TaskDto avec le UserDto
        TaskDto taskDto = new TaskDto(
                "Updated Task Title",
                "Updated Task Description",
                TaskState.COMPLETED,
                TaskPriority.AVERAGE,
                LocalDate.now().plusDays(3),
                LocalDate.now().plusDays(4),
                TaskCategory.PERSONAL,
                userDto
        );

        // Simuler la réponse de serviceTaskManager.updateTaskForGroup
        when(serviceTaskManager.updateTaskForGroup(anyLong(), any(TaskDto.class))).thenReturn(taskDto);

        // Appeler la méthode du contrôleur
        ResponseEntity<TaskDto> response = taskGroupController.updateTaskForGroup(taskData);

        // Vérifier la réponse et les interactions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(taskDto, response.getBody());
        verify(serviceTaskManager).updateTaskForGroup(1L, taskDto);
        verify(serviceTaskManager).findUserById(1L);
    }

    @Test
    void filterByCategoryGroup() {
        TaskDto taskDto = new TaskDto(
                "Task Title", "Task Description", TaskState.TODO, TaskPriority.HIGH,
                LocalDate.now().plusDays(1), LocalDate.now().plusDays(2),
                TaskCategory.WORK, null
        );
        Set<TaskDto> tasks = Set.of(taskDto);

        FilterTaskCriteriaGroup filterCriteria = new FilterTaskCriteriaGroup();
        filterCriteria.setGroupId(1L);
        filterCriteria.setCategory(TaskCategory.WORK);

        when(serviceTaskManager.filterByCategoryGroup(anyLong(), any(TaskCategory.class))).thenReturn(tasks);

        ResponseEntity<Set<TaskDto>> response = taskGroupController.filterByCategoryGroup(filterCriteria);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains(taskDto));
        verify(serviceTaskManager).filterByCategoryGroup(1L, TaskCategory.WORK);
    }

    @Test
    void assignTaskForGrTo() {
        TaskForGroupData taskData = new TaskForGroupData();
        taskData.setGroupId(1L);
        taskData.setUserId(2L);
        taskData.setId(1L);

        TaskDto taskDto = new TaskDto(
                "Task Title", "Task Description", TaskState.TODO, TaskPriority.HIGH,
                LocalDate.now().plusDays(1), LocalDate.now().plusDays(2),
                TaskCategory.WORK, null
        );

        when(serviceTaskManager.assignTaskForGrTo(anyLong(), anyLong(), anyLong())).thenReturn(taskDto);

        ResponseEntity<TaskDto> response = taskGroupController.assignTaskForGrTo(taskData);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(taskDto, response.getBody());
        verify(serviceTaskManager).assignTaskForGrTo(1L, 2L, 1L);
    }
}
