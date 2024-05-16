package ved.firstproject.gestionnairetaches.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ved.firstproject.gestionnairetaches.dao.TaskRepository;
import ved.firstproject.gestionnairetaches.model.Task;
import ved.firstproject.gestionnairetaches.model.User;
import ved.firstproject.gestionnairetaches.service.dto.TaskDto;
import ved.firstproject.gestionnairetaches.service.dto.UserDto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServiceTaskManagerTest {
    @InjectMocks
    private ServiceTaskManager serviceTaskManager;

    @Mock
    private TaskRepository taskRepository;

    private TaskDto taskDtoInit;
    private Task taskInit;
    private User user;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        userDto = new UserDto(1L, "username", "password", Set.of());
        user = new User(1L, "username", "password", Set.of());
        taskDtoInit = new TaskDto(1L, "title", "description", "status", "priority", "deadline", "category", userDto);
        taskInit = new Task(1L, "title", "description", "status", "priority", "deadline", "category", user);
    }

    @Test
    void createTask() {
        when(taskRepository.save(any(Task.class))).thenReturn(new Task(1L, "title", "description", "status", "priority", "deadline", "category", user));
        TaskDto taskDto = new TaskDto("title", "description", "status", "priority", "deadline", "category", userDto);

        TaskDto taskDtoCreated = serviceTaskManager.createTask(taskDto);

        assertEquals(taskDtoCreated, taskDtoInit);
    }

    @Test
    void listTasks() {
        List<Task> taskListInit = new ArrayList<>(Set.of(taskInit));
        List<TaskDto> taskDtoInitListInit = new ArrayList<>(Set.of(taskDtoInit));
        when(taskRepository.findAll()).thenReturn(taskListInit);

        Set<TaskDto> tasksList = serviceTaskManager.listTasks();

        assertEquals(tasksList, new HashSet<>(taskDtoInitListInit));
    }

    @Test
    void updateTask() {

    }

    @Test
    void recoverTask() {

    }
}