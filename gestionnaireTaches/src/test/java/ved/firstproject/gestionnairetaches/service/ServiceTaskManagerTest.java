package ved.firstproject.gestionnairetaches.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ved.firstproject.gestionnairetaches.dao.ITaskGroupRepository;
import ved.firstproject.gestionnairetaches.dao.ITaskRepository;
import ved.firstproject.gestionnairetaches.dao.IUserRepository;
import ved.firstproject.gestionnairetaches.model.Task;
import ved.firstproject.gestionnairetaches.model.TaskGroup;
import ved.firstproject.gestionnairetaches.model.enums.TaskCategory;
import ved.firstproject.gestionnairetaches.model.enums.TaskPriority;
import ved.firstproject.gestionnairetaches.model.enums.TaskState;
import ved.firstproject.gestionnairetaches.model.User;
import ved.firstproject.gestionnairetaches.service.dto.TaskDto;
import ved.firstproject.gestionnairetaches.service.dto.TaskGroupDto;
import ved.firstproject.gestionnairetaches.service.dto.UserDto;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
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
    @Mock
    private ITaskGroupRepository taskGroupRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    private TaskDto taskDtoInit;
    private Task taskInit;
    private User user;
    private UserDto userDto;
    private final TaskCategory workCategory = TaskCategory.WORK;
    private final LocalDate deadline = LocalDate.now().plusWeeks(1);
    private final TaskState status = TaskState.TODO;
    TaskPriority priorityHigh = TaskPriority.HIGH;

    @BeforeEach
    void setUp() {
        userDto = new UserDto(1L, "username", "password", new HashSet<>());
        user = new User(1L, "username", "password", new HashSet<>());
        taskDtoInit = new TaskDto(1L, "title", "description", status, priorityHigh,  deadline, null, workCategory, userDto);
        taskInit = new Task(1L, "title", "description", priorityHigh, deadline, workCategory, user);
    }

    @Test
    void createUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(passwordEncoder.encode(userDto.password())).thenReturn(user.getPassword());

        UserDto userDtoCreated = serviceTaskManager.createUser(userDto);

        assertEquals(userDto, userDtoCreated);
    }

    @Test
    void createTask() {
        when(taskRepository.save(any(Task.class))).thenReturn(new Task(1L, "title", "description", priorityHigh, deadline, workCategory, user));
        when(userRepository.findById(anyLong())).thenReturn(java.util.Optional.of(user));
        TaskDto taskDto = new TaskDto("title", "description", status, priorityHigh, deadline, null, workCategory, userDto);

        TaskDto taskDtoCreated = serviceTaskManager.createTask(user.getId(), taskDto);

        assertEquals(taskDtoCreated, taskDtoInit);
        assertEquals(taskDtoInit, user.getTasks().stream().findFirst().map(TaskDto::toTaskDto).orElse(null));
    }

    @Test
    void listTasks() {
        when(userRepository.findById(anyLong())).thenReturn(java.util.Optional.of(user));
        Task taskInit2 = new Task(2L, "title", "description", priorityHigh, deadline, workCategory, user);
        TaskDto taskInitDto2 = new TaskDto(2L, "title", "description", status, priorityHigh,  deadline, null, workCategory, userDto);
        Set<TaskDto> tasks = new HashSet<>(Set.of(taskDtoInit));
        tasks.add(taskInitDto2);
        user.addTask(taskInit);
        user.addTask(taskInit2);

        Set<TaskDto> tasksList = serviceTaskManager.findAllTasksByUserId(user.getId());

        assertEquals(tasksList, tasks);
    }

    @Test
    void updateTask() {
        when(userRepository.findById(anyLong())).thenReturn(java.util.Optional.of(user));
        when(taskRepository.save(any(Task.class))).thenReturn(taskInit);
        user.addTask(taskInit);
        taskDtoInit = new TaskDto(1L, "New title", "description", status, priorityHigh, deadline, null, workCategory, userDto);

        serviceTaskManager.updateTask(user.getId(), taskDtoInit);

        assertEquals("New title", user.getTasks().stream().findFirst().map(Task::getTitle).orElse(null));
    }

    @Test
    void filterByCategory(){
        when(userRepository.findById(anyLong())).thenReturn(java.util.Optional.of(user));
        TaskCategory personalCategory = TaskCategory.PERSONAL;
        Task taskInit2 = new Task(2L, "title", "description", priorityHigh, LocalDate.now().plusWeeks(1), personalCategory, user);
        TaskDto taskInitDto2 = new TaskDto(2L, "title", "description", status, priorityHigh, deadline, null, personalCategory, userDto);
        user.addTask(taskInit);
        user.addTask(taskInit2);

        Set<TaskDto> tasksListForWork = serviceTaskManager.filterByCategory(user.getId(), TaskCategory.WORK);
        Set<TaskDto> tasksListForPersonal = serviceTaskManager.filterByCategory(user.getId(), TaskCategory.PERSONAL);

        assertEquals(Set.of(taskDtoInit), tasksListForWork);
        assertEquals(Set.of(taskInitDto2), tasksListForPersonal);
    }

    @Test
    void ifUsernameTaken() {
        when(userRepository.findByUsername(userDto.username())).thenReturn(java.util.Optional.of(user));

        assertThrows(IllegalArgumentException.class, () -> serviceTaskManager.createUser(userDto));
    }

    @Test
    void completeTask() {
        when(userRepository.findById(anyLong())).thenReturn(java.util.Optional.of(user));
        when(taskRepository.findById(anyLong())).thenReturn(java.util.Optional.of(taskInit));
        when(taskRepository.save(any(Task.class))).thenReturn(taskInit);
        user.addTask(taskInit);

        serviceTaskManager.completeTask(user.getId(), taskInit.getId());

        assertEquals(TaskState.COMPLETED, user.getTasks().stream().findFirst().map(Task::getStatus).orElse(null));
    }

    @Test
    void findAllTasksHistoryByUserId() {
        when(userRepository.findById(anyLong())).thenReturn(java.util.Optional.of(user));
        Task taskInit2 = new Task(2L, "title", "description", priorityHigh, LocalDate.now().plusWeeks(1), workCategory, user);
        TaskDto taskInitDto2 = new TaskDto(2L, "title", "description", status, priorityHigh, deadline, null, workCategory, userDto);
        user.addTaskHistory(taskInit);
        user.addTaskHistory(taskInit2);

        Set<TaskDto> tasksList = serviceTaskManager.findAllTasksHistoryByUserId(user.getId());

        assertEquals(Set.of(taskDtoInit, taskInitDto2), tasksList);
    }

    @Test
    void createTaskGroup() {
        when(userRepository.findById(anyLong())).thenReturn(java.util.Optional.of(user));
        when(taskGroupRepository.save(any())).thenReturn(new TaskGroup("title", user));
        TaskGroupDto taskGroup = serviceTaskManager.createTaskGroup("title", user.getId());

        assertEquals("title", taskGroup.title());
    }

    @Test
    void addUserToGroup() {
        when(userRepository.findById(anyLong())).thenReturn(java.util.Optional.of(user));
        when(taskGroupRepository.findById(anyLong())).thenReturn(java.util.Optional.of(new TaskGroup("title", user)));
        when(taskGroupRepository.save(any())).thenReturn(new TaskGroup(0L, "title", Set.of(), Set.of(), Set.of()));
        TaskGroupDto taskGroup = serviceTaskManager.createTaskGroup("title", user.getId());

        TaskGroupDto taskGroupDto = serviceTaskManager.addUserToGroup(taskGroup.id(), user.getId());

        assertEquals(user.getId(), taskGroupDto.usersGroup().stream().findFirst().map(UserDto::id).orElse(null));
    }

    @Test
    void removeUserFromGroup(){
        Set<User> users = new HashSet<>();
        Set<Task> tasks = new HashSet<>();
        Set<Task> tasksHistory = new HashSet<>();
        when(userRepository.findById(anyLong())).thenReturn(java.util.Optional.of(user));
        when(taskGroupRepository.findById(anyLong())).thenReturn(java.util.Optional.of(new TaskGroup(0L, "title", users, tasks, tasksHistory)));
        when(taskGroupRepository.save(any())).thenReturn(new TaskGroup(0L, "title", Set.of(), Set.of(), Set.of()));
        TaskGroupDto taskGroup = serviceTaskManager.createTaskGroup("title", user.getId());
        TaskGroupDto taskGroupDto = serviceTaskManager.addUserToGroup(taskGroup.id(), user.getId());

        TaskGroupDto taskGroupDtoRemoved = serviceTaskManager.removeUserFromGroup(taskGroupDto.id(), user.getId());

        assertEquals(0, taskGroupDtoRemoved.usersGroup().size());
    }

    @Test
    void addTaskToGroup(){
        when(taskGroupRepository.findById(anyLong())).thenReturn(java.util.Optional.of(new TaskGroup("title", user)));
        when(taskGroupRepository.save(any())).thenReturn(new TaskGroup(0L, "title", Set.of(), Set.of(), Set.of()));
        when(userRepository.findById(anyLong())).thenReturn(java.util.Optional.of(user));
        TaskGroupDto taskGroup = serviceTaskManager.createTaskGroup("title", user.getId());
        TaskDto taskDto = new TaskDto(1L, "title", "description", status, priorityHigh, deadline, null, workCategory, userDto);

        TaskGroupDto taskGroupDto = serviceTaskManager.addTaskToGroup(taskGroup.id(), taskDto);

        assertEquals(1, taskGroupDto.tasksGroup().size());
    }

    @Test
    void removeTaskFromGroup(){
        Set<User> users = new HashSet<>();
        Set<Task> tasks = new HashSet<>();
        Set<Task> tasksHistory = new HashSet<>();
        when(taskGroupRepository.findById(anyLong())).thenReturn(java.util.Optional.of(new TaskGroup(0L, "title", users, tasks, tasksHistory)));
        when(taskGroupRepository.save(any())).thenReturn(new TaskGroup(0L, "title", Set.of(), Set.of(), Set.of()));
        when(userRepository.findById(anyLong())).thenReturn(java.util.Optional.of(user));
        TaskGroupDto taskGroup = serviceTaskManager.createTaskGroup("title", user.getId());
        TaskDto taskDto = new TaskDto(1L, "title", "description", status, priorityHigh, deadline, null, workCategory, userDto);
        TaskGroupDto taskGroupDto = serviceTaskManager.addTaskToGroup(taskGroup.id(), taskDto);

        TaskGroupDto taskGroupDtoRemoved = serviceTaskManager.removeTaskFromGroup(taskGroupDto.id(), taskDto.id());

        assertEquals(0, taskGroupDtoRemoved.tasksGroup().size());
    }

    @Test
    void completeTaskFromGroup(){
        Set<User> users = new HashSet<>();
        Set<Task> tasks = new HashSet<>();
        Set<Task> tasksHistory = new HashSet<>();
        when(taskGroupRepository.findById(anyLong())).thenReturn(java.util.Optional.of(new TaskGroup(0L, "title", users, tasks, tasksHistory)));
        when(taskGroupRepository.save(any())).thenReturn(new TaskGroup(0L, "title", Set.of(), Set.of(), Set.of()));
        when(userRepository.findById(anyLong())).thenReturn(java.util.Optional.of(user));
        TaskGroupDto taskGroup = serviceTaskManager.createTaskGroup("title", user.getId());
        TaskDto taskDto = new TaskDto(1L, "title", "description", status, priorityHigh, deadline, null, workCategory, userDto);
        TaskGroupDto taskGroupDto = serviceTaskManager.addTaskToGroup(taskGroup.id(), taskDto);

        TaskGroupDto taskGroupDtoCompleted = serviceTaskManager.completeTaskFromGroup(taskGroupDto.id(), taskDto.id());

        assertEquals(0, taskGroupDtoCompleted.tasksGroup().size());
        assertEquals(TaskState.COMPLETED, taskGroupDtoCompleted.tasksGroupHistory().stream().findFirst().map(TaskDto::status).orElse(null));
        assertEquals(LocalDate.now(), taskGroupDtoCompleted.tasksGroupHistory().stream().findFirst().map(TaskDto::completionDate).orElse(null));
    }
}