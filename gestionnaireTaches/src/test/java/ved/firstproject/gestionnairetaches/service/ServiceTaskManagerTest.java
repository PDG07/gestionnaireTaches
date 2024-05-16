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
import ved.firstproject.gestionnairetaches.model.TaskCategory;
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
    private TaskCategory workCategory = TaskCategory.WORK;

    @BeforeEach
    void setUp() {
        userDto = new UserDto(1L, "username", "password", new HashSet<>());
        user = new User(1L, "username", "password", new HashSet<>());
        taskDtoInit = new TaskDto(1L, "title", "description", "status", "priority", "deadline", workCategory, userDto);
        taskInit = new Task(1L, "title", "description", "status", "priority", "deadline", workCategory, user);
    }

    @Test
    void createUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDto userDtoCreated = serviceTaskManager.createUser(userDto);

        assertEquals(userDtoCreated, userDto);
    }

    @Test
    void createTask() {
        when(taskRepository.save(any(Task.class))).thenReturn(new Task(1L, "title", "description", "status", "priority", "deadline", workCategory, user));
        when(userRepository.findById(anyLong())).thenReturn(java.util.Optional.of(user));
        TaskDto taskDto = new TaskDto("title", "description", "status", "priority", "deadline", workCategory, userDto);

        TaskDto taskDtoCreated = serviceTaskManager.createTask(user.getId(), taskDto);

        assertEquals(taskDtoCreated, taskDtoInit);
        assertEquals(taskDtoInit, user.getTasks().stream().findFirst().map(TaskDto::toTaskDto).orElse(null));
    }

    @Test
    void listTasks() {
        when(userRepository.findById(anyLong())).thenReturn(java.util.Optional.of(user));
        Task taskInit2 = new Task(2L, "title", "description", "status", "priority", "deadline", workCategory, user);
        TaskDto taskInitDto2 = new TaskDto(2L, "title", "description", "status", "priority", "deadline", workCategory, userDto);
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
        taskDtoInit = new TaskDto(1L, "New title", "description", "status", "priority", "deadline", workCategory, userDto);

        serviceTaskManager.updateTask(user.getId(), taskDtoInit);

        assertEquals("New title", user.getTasks().stream().findFirst().map(Task::getTitle).orElse(null));
    }

    @Test
    void filterByCategory(){
        when(userRepository.findById(anyLong())).thenReturn(java.util.Optional.of(user));
        TaskCategory personalCategory = TaskCategory.PERSONAL;
        Task taskInit2 = new Task(2L, "title", "description", "status", "priority", "deadline", personalCategory, user);
        TaskDto taskInitDto2 = new TaskDto(2L, "title", "description", "status", "priority", "deadline", personalCategory, userDto);
        user.addTask(taskInit);
        user.addTask(taskInit2);

        Set<TaskDto> tasksListForWork = serviceTaskManager.filterByCategory(user.getId(), TaskCategory.WORK);
        Set<TaskDto> tasksListForPersonal = serviceTaskManager.filterByCategory(user.getId(), TaskCategory.PERSONAL);

        assertEquals(Set.of(taskDtoInit), tasksListForWork);
        assertEquals(Set.of(taskInitDto2), tasksListForPersonal);
    }

    @Test
    void recoverTask() {

    }
}