package ved.firstproject.gestionnairetaches.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ved.firstproject.gestionnairetaches.dao.ITaskRepository;
import ved.firstproject.gestionnairetaches.dao.IUserRepository;
import ved.firstproject.gestionnairetaches.model.Task;
import ved.firstproject.gestionnairetaches.model.User;
import ved.firstproject.gestionnairetaches.service.dto.TaskDto;
import ved.firstproject.gestionnairetaches.service.dto.UserDto;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServiceTaskManagerTest {
    @InjectMocks
    private ServiceTaskManager serviceTaskManager;

    @Mock
    private ITaskRepository taskRepository;
    @Mock
    private IUserRepository userRepository;

    private TaskDto taskDtoInit;
    private Task taskInit;
    private User user;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        Set<Task> tasks = new HashSet<>();
        Set<TaskDto> taskDtos = new HashSet<>();
        userDto = new UserDto(1L, "username", "password", taskDtos);
        user = new User(1L, "username", "password", tasks);
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
        when(userRepository.findById(anyLong())).thenReturn(java.util.Optional.of(user));
        Task taskInit2 = new Task(2L, "title", "description", "status", "priority", "deadline", "category", user);
        TaskDto taskInitDto2 = new TaskDto(2L, "title", "description", "status", "priority", "deadline", "category", userDto);
        Set<TaskDto> tasks = new HashSet<>(Set.of(taskDtoInit));
        tasks.add(taskInitDto2);
        user.addTask(taskInit);
        user.addTask(taskInit2);

        Set<TaskDto> tasksList = serviceTaskManager.listTasks(user.getId());

        assertEquals(tasksList, tasks);
    }

    @Test
    void updateTask() {
        when(userRepository.findById(anyLong())).thenReturn(java.util.Optional.of(user));
        when(taskRepository.save(any(Task.class))).thenReturn(taskInit);
        user.addTask(taskInit);
        taskDtoInit = new TaskDto(1L, "New title", "description", "status", "priority", "deadline", "category", userDto);

        serviceTaskManager.updateTask(user.getId(), taskDtoInit);

        assertEquals("New title", user.getTasks().stream().findFirst().map(Task::getTitle).orElse(null));
    }


    @Test
    void recoverTask() {

    }
}