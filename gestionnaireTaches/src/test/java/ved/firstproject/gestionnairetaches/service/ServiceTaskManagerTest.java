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
    private final TaskPriority priorityHigh = TaskPriority.HIGH;
    private TaskGroup taskGroup;

    @BeforeEach
    void setUp() {
        userDto = new UserDto(1L, "username", "password", new HashSet<>());
        user = new User(1L, "username", "password", new HashSet<>(), new HashSet<>());
        taskDtoInit = new TaskDto(1L, "title", "description", status, priorityHigh,  deadline, null, workCategory, userDto);
        taskInit = new Task(1L, "title", "description", priorityHigh, deadline, workCategory, user);

        Set<User> users = new HashSet<>();
        Set<Task> tasks = new HashSet<>();
        taskGroup = new TaskGroup(0L, "title", users, tasks);
    }

    @Test
    void createUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(passwordEncoder.encode(userDto.password())).thenReturn(user.getPassword());

        UserDto userDtoCreated = serviceTaskManager.createUser(userDto);

        assertEquals(userDto, userDtoCreated);
    }

    @Test
    void login() {
        when(userRepository.findByUsername(userDto.username())).thenReturn(java.util.Optional.of(user));
        when(passwordEncoder.matches(userDto.password(), user.getPassword())).thenReturn(true);

        UserDto userLogged = serviceTaskManager.login(userDto);

        assertEquals(userDto, userLogged);
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
    void findAllTasksByUserId() {
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
        when(taskRepository.save(any(Task.class))).thenReturn(new Task(1L, "New title", "description", status, priorityHigh, deadline, null, workCategory, user));
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

        TaskDto taskCompleted = serviceTaskManager.completeTask(user.getId(), taskInit.getId());


        assertEquals(TaskState.COMPLETED, taskCompleted.status());
    }

    @Test
    void completedTasks() {
        when(userRepository.findById(anyLong())).thenReturn(java.util.Optional.of(user));
        Task taskInit2 = new Task(2L, "title", "description", priorityHigh, deadline, workCategory, user);
        user.addTask(taskInit);
        user.addTask(taskInit2);
        taskInit.completeTask();

        Set<TaskDto> tasksList = serviceTaskManager.completedTasks(user.getId());

        assertEquals(1, tasksList.size());
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
        when(taskGroupRepository.findById(anyLong())).thenReturn(java.util.Optional.of(taskGroup));
        when(taskGroupRepository.save(any())).thenReturn(new TaskGroup(0L, "title", Set.of(user), Set.of()));
        TaskGroupDto taskGroup = serviceTaskManager.createTaskGroup("title", user.getId());

        TaskGroupDto taskGroupDto = serviceTaskManager.addUserToGroup(taskGroup.id(), user.getId());

        assertEquals(user.getId(), taskGroupDto.usersGroup().stream().findFirst().map(UserDto::id).orElse(null));
    }

    @Test
    void removeUserFromGroup(){
        when(userRepository.findById(anyLong())).thenReturn(java.util.Optional.of(user));
        when(taskGroupRepository.findById(anyLong())).thenReturn(java.util.Optional.of(taskGroup));
        when(taskGroupRepository.save(any())).thenReturn(new TaskGroup(0L, "title", Set.of(), Set.of()));
        TaskGroupDto taskGroup = serviceTaskManager.createTaskGroup("title", user.getId());
        TaskGroupDto taskGroupDto = serviceTaskManager.addUserToGroup(taskGroup.id(), user.getId());

        TaskGroupDto taskGroupDtoRemoved = serviceTaskManager.removeUserFromGroup(taskGroupDto.id(), user.getId());

        assertEquals(0, taskGroupDtoRemoved.usersGroup().size());
    }

    @Test
    void addTaskToGroup(){
        when(taskGroupRepository.findById(anyLong())).thenReturn(java.util.Optional.of(new TaskGroup("title", user)));
        when(taskGroupRepository.save(any())).thenReturn(new TaskGroup(0L, "title", Set.of(), Set.of(taskInit)));
        when(userRepository.findById(anyLong())).thenReturn(java.util.Optional.of(user));
        when(taskRepository.save(any())).thenReturn(taskInit);
        TaskGroupDto taskGroup = serviceTaskManager.createTaskGroup("title", user.getId());
        TaskDto taskDto = new TaskDto(1L, "title", "description", status, priorityHigh, deadline, null, workCategory, userDto);

        TaskGroupDto taskGroupDto = serviceTaskManager.addTaskToGroup(taskGroup.id(), taskDto);

        assertEquals(1, taskGroupDto.tasksGroup().size());
    }

    //TODO TEST removeTaskFromGroup() Error
    @Test
    void removeTaskFromGroup(){
        when(taskGroupRepository.save(any())).thenReturn(new TaskGroup(0L, "title", Set.of(user), Set.of(taskInit)));
        when(taskGroupRepository.findById(anyLong())).thenReturn(java.util.Optional.of(taskGroup));
        when(taskRepository.save(any())).thenReturn(taskInit);
        when(taskRepository.findById(anyLong())).thenReturn(java.util.Optional.of(new Task(1L, "title", "description", priorityHigh, deadline, workCategory, user)));
        when(userRepository.findById(anyLong())).thenReturn(java.util.Optional.of(user));
        TaskGroupDto taskGroup = serviceTaskManager.createTaskGroup("title", user.getId());
        TaskDto taskDto = new TaskDto(1L, "title", "description", status, priorityHigh, deadline, null, workCategory, userDto);
        TaskGroupDto taskGroupDto = serviceTaskManager.addTaskToGroup(taskGroup.id(), taskDto);
        System.out.println();

        TaskGroupDto taskGroupDtoEmpty = serviceTaskManager.removeTaskFromGroup(taskGroupDto.id(), taskDto.id(), user.getId());

        assertEquals(0, taskGroupDtoEmpty.tasksGroup().size());
    }

    @Test
    void completeTaskFromGroup() {
        when(taskGroupRepository.findById(anyLong())).thenReturn(java.util.Optional.of(taskGroup));
        when(taskGroupRepository.save(any())).thenReturn(taskGroup);
        when(taskRepository.save(any())).thenReturn(taskInit);
        TaskDto taskDto = new TaskDto(1L, "title", "description", status, priorityHigh, deadline, null, workCategory, userDto);
        TaskGroupDto taskGroupDto = serviceTaskManager.addTaskToGroup(taskGroup.getId(), taskDto);

        TaskGroupDto taskGroupDtoCompleted = serviceTaskManager.completeTaskFromGroup(taskGroupDto.id(), taskDto.id());

        assertEquals(0, taskGroupDtoCompleted.tasksGroup().size());
    }


    @Test
    void findAllTasksByGroupId(){
        when(taskGroupRepository.findById(anyLong())).thenReturn(java.util.Optional.of(taskGroup));
        when(taskGroupRepository.save(any())).thenReturn(taskGroup);
        when(taskRepository.save(any())).thenReturn(taskInit);
        TaskDto taskDto = new TaskDto(1L, "title", "description", status, priorityHigh, deadline, null, workCategory, userDto);
        TaskGroupDto taskGroupDto = serviceTaskManager.addTaskToGroup(taskGroup.getId(), taskDto);

        Set<TaskDto> tasksList = serviceTaskManager.findAllTasksByGroupId(taskGroupDto.id());

        assertEquals(1, tasksList.size());
    }

    @Test
    void filterByCategoryGroup(){
        when(taskGroupRepository.findById(anyLong())).thenReturn(java.util.Optional.of(taskGroup));
        when(taskGroupRepository.save(any())).thenReturn(taskGroup);
        when(taskRepository.save(any())).thenReturn(taskInit);
        TaskDto taskDto = new TaskDto(1L, "title", "description", status, priorityHigh, deadline, null, workCategory, userDto);
        TaskGroupDto taskGroupDto = serviceTaskManager.addTaskToGroup(taskGroup.getId(), taskDto);

        Set<TaskDto> tasksList = serviceTaskManager.filterByCategoryGroup(taskGroupDto.id(), workCategory);
        Set<TaskDto> tasksListEmpty = serviceTaskManager.filterByCategoryGroup(taskGroupDto.id(), TaskCategory.PERSONAL);

        assertEquals(Set.of(taskDto), tasksList);
        assertEquals(0, tasksListEmpty.size());
    }

    @Test
    void assignTaskForGrTo(){
        when(taskGroupRepository.findById(anyLong())).thenReturn(java.util.Optional.of(taskGroup));
        when(taskGroupRepository.save(any())).thenReturn(taskGroup);
        when(taskRepository.save(any())).thenReturn(taskInit);
        when(userRepository.findById(anyLong())).thenReturn(java.util.Optional.of(user));
        TaskDto taskDto = new TaskDto(1L, "title", "description", status, priorityHigh, deadline, null, workCategory, userDto);
        TaskGroupDto taskGroupDto = serviceTaskManager.addTaskToGroup(taskGroup.getId(), taskDto);
        TaskGroupDto tgdto = serviceTaskManager.addUserToGroup(taskGroupDto.id(), user.getId());


        TaskDto taskDtoAssigned = serviceTaskManager.assignTaskForGrTo(tgdto.id(), user.getId(), taskDto.id());

        assertEquals(user.getId(), taskDtoAssigned.user().id());
    }

    @Test
    void updateTaskForGroup(){
        when(taskGroupRepository.findById(anyLong())).thenReturn(java.util.Optional.of(taskGroup));
        when(taskGroupRepository.save(any())).thenReturn(taskGroup);
        when(taskRepository.save(any())).thenReturn(taskInit);
        TaskDto taskDto = new TaskDto(1L, "title", "description", status, priorityHigh, deadline, null, workCategory, userDto);
        TaskGroupDto taskGroupDto = serviceTaskManager.addTaskToGroup(taskGroup.getId(), taskDto);

        TaskDto taskDtoUpdated = new TaskDto(1L, "titleUpdated", "descriptionUpdated", status, TaskPriority.AVERAGE, deadline, LocalDate.now(), workCategory, userDto);

        TaskDto taskDtoUpdatedForGroup = serviceTaskManager.updateTaskForGroup(taskGroupDto.id(), taskDtoUpdated);

        assertEquals("titleUpdated", taskDtoUpdatedForGroup.title());
        assertEquals("descriptionUpdated", taskDtoUpdatedForGroup.description());
        assertEquals(TaskPriority.AVERAGE, taskDtoUpdatedForGroup.priority());
        assertEquals(LocalDate.now(), taskDtoUpdatedForGroup.completionDate());
    }

    @Test
    void findAllUserFromGroup(){
        when(taskGroupRepository.findById(anyLong())).thenReturn(java.util.Optional.of(taskGroup));
        when(taskGroupRepository.save(any())).thenReturn(taskGroup);
        when(userRepository.findById(anyLong())).thenReturn(java.util.Optional.of(user));
        TaskGroupDto taskGroupDto = serviceTaskManager.addUserToGroup(taskGroup.getId(), user.getId());

        Set<UserDto> users = serviceTaskManager.findAllUserFromGroup(taskGroupDto.id());

        assertEquals(1, users.size());
    }

    @Test
    void getGroupsFromUserId(){
        when(userRepository.findById(anyLong())).thenReturn(java.util.Optional.of(user));
        TaskGroup taskGroup2 = new TaskGroup(2L, "title2", Set.of(user), Set.of());
        user.addTaskGroupToUser(taskGroup);
        user.addTaskGroupToUser(taskGroup2);

        Set<TaskGroupDto> taskGroups = serviceTaskManager.getGroupsFromUserId(user.getId());

        assertEquals(2, taskGroups.size());
    }

    @Test
    void getTasksOfGroup(){
        when(taskGroupRepository.findById(anyLong())).thenReturn(java.util.Optional.of(taskGroup));
        when(taskGroupRepository.save(any())).thenReturn(taskGroup);
        when(taskRepository.save(any())).thenReturn(taskInit);
        TaskDto taskDto = new TaskDto(1L, "title", "description", status, priorityHigh, deadline, null, workCategory, userDto);
        TaskGroupDto taskGroupDto = serviceTaskManager.addTaskToGroup(taskGroup.getId(), taskDto);

        Set<TaskDto> tasks = serviceTaskManager.getTasksOfGroup(taskGroupDto.id());

        assertEquals(1, tasks.size());
    }

    @Test
    void findGroupById(){
        when(taskGroupRepository.findById(anyLong())).thenReturn(java.util.Optional.of(taskGroup));

        TaskGroupDto taskGroupDto = serviceTaskManager.findGroupById(taskGroup.getId());

        assertEquals(taskGroupDto.id(), taskGroup.getId());
    }

    @Test
    void findUserById(){
        when(userRepository.findById(anyLong())).thenReturn(java.util.Optional.of(user));

        User userFound = serviceTaskManager.findUserById(user.getId());

        assertEquals(user, userFound);
    }

    @Test
    void findTaskGroupById(){
        when(taskGroupRepository.findById(anyLong())).thenReturn(java.util.Optional.of(taskGroup));

        TaskGroup taskGroupFound = serviceTaskManager.findTaskGroupById(taskGroup.getId());

        assertEquals(taskGroup, taskGroupFound);
    }

    @Test
    void findUserByUsername(){
        when(userRepository.findByUsername(userDto.username())).thenReturn(java.util.Optional.of(user));

        UserDto userDtoFound = serviceTaskManager.findUserByUsername(userDto.username());

        assertEquals(userDto, userDtoFound);
    }

    @Test
    void findGroupByTitle(){
        when(taskGroupRepository.findByTitle("title")).thenReturn(taskGroup);

        TaskGroupDto taskGroupDto = serviceTaskManager.findGroupByTitle("title");

        assertEquals(taskGroupDto.title(), taskGroup.getTitle());
    }
}