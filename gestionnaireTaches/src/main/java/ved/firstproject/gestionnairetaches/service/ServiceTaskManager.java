package ved.firstproject.gestionnairetaches.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import ved.firstproject.gestionnairetaches.dao.ITaskGroupRepository;
import ved.firstproject.gestionnairetaches.dao.ITaskRepository;
import ved.firstproject.gestionnairetaches.dao.IUserRepository;
import ved.firstproject.gestionnairetaches.model.Task;
import org.springframework.stereotype.Service;
import ved.firstproject.gestionnairetaches.model.TaskGroup;
import ved.firstproject.gestionnairetaches.model.enums.TaskCategory;
import ved.firstproject.gestionnairetaches.model.enums.TaskState;
import ved.firstproject.gestionnairetaches.model.User;
import ved.firstproject.gestionnairetaches.service.dto.TaskDto;
import ved.firstproject.gestionnairetaches.service.dto.TaskGroupDto;
import ved.firstproject.gestionnairetaches.service.dto.UserDto;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class ServiceTaskManager {
    private final ITaskRepository taskRepository;
    private final IUserRepository userRepository;
    private final ITaskGroupRepository taskGroupRepository;
    private final PasswordEncoder passwordEncoder;

    public ServiceTaskManager(ITaskRepository taskRepository, IUserRepository userRepository, ITaskGroupRepository  taskGroupRepository, PasswordEncoder passwordEncoder) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.taskGroupRepository = taskGroupRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDto createUser(UserDto userDto) {
        Objects.requireNonNull(userDto);
        validationUsernameTaken(userDto);
        User user = UserDto.toUser(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return UserDto.toUserDto(user);
    }

    public TaskDto createTask(Long userId, TaskDto taskDto) {
        Objects.requireNonNull(taskDto);
        User user = findUserById(userId);
        Task taskSaved = taskRepository.save(TaskDto.toTask(taskDto));
        user.addTask(taskSaved);
        userRepository.save(user);
        return TaskDto.toTaskDto(taskSaved);
    }

    public TaskDto updateTask(Long userId, TaskDto taskDto) {
        Objects.requireNonNull(taskDto);
        User user = findUserById(userId);
        Task task = TaskDto.toTask(taskDto);
        Task updatedTask = user.updateTask(task);
        userRepository.save(user);
        return TaskDto.toTaskDto(taskRepository.save(updatedTask));
    }

    public TaskDto completeTask(Long userId, Long taskId){
        Task task = findTaskById(taskId);
        User user = findUserById(userId);
        task.completeTask();
        user.addTaskHistory(task);
        userRepository.save(user);
        return TaskDto.toTaskDto(taskRepository.save(task));
    }

    public Set<TaskDto> filterByCategory(Long userId, TaskCategory category) {
        User user = findUserById(userId);
        return user.getTasks().stream()
                .filter(task -> task.getCategory().equals(category))
                .map(TaskDto::toTaskDto)
                .collect(Collectors.toSet());
    }

    public Set<TaskDto> findAllTasksByUserId(Long userId) {
        User user = findUserById(userId);
        return user.getTasks().stream().map(TaskDto::toTaskDto).collect(Collectors.toSet());
    }

    public Set<TaskDto> findAllTasksHistoryByUserId(Long userId) {
        User user = findUserById(userId);
        return user.getTasksHistory().stream().map(TaskDto::toTaskDto).collect(Collectors.toSet());
    }

    public TaskGroupDto createTaskGroup(String title, Long userId) {
        User user = findUserById(userId);
        TaskGroup taskGroup = new TaskGroup(title, user);
        user.setTaskGroupUser(taskGroup);
        userRepository.save(user);
        return TaskGroupDto.toTaskGroupDto(taskGroupRepository.save(taskGroup));
    }

    public TaskGroupDto addUserToGroup(Long taskGroupId, Long userId) {
        User user = findUserById(userId);
        TaskGroup taskGroup = findTaskGroupById(taskGroupId);
        taskGroup.addUser(user);
        user.setTaskGroupUser(taskGroup);
        return TaskGroupDto.toTaskGroupDto(taskGroupRepository.save(taskGroup));
    }

    public TaskGroupDto addTaskToGroup(Long taskGroupId, TaskDto taskDto) {
        Objects.requireNonNull(taskDto);
        TaskGroup taskGroup = findTaskGroupById(taskGroupId);
        Task task = TaskDto.toTask(taskDto);
        taskGroup.addTask(taskRepository.save(task));
        return TaskGroupDto.toTaskGroupDto(taskGroupRepository.save(taskGroup));
    }

    public TaskGroupDto removeUserFromGroup(Long taskGroupId, Long userId) {
        TaskGroup taskGroup = findTaskGroupById(taskGroupId);
        taskGroup.removeUser(userId);
        return TaskGroupDto.toTaskGroupDto(taskGroupRepository.save(taskGroup));
    }

    public TaskGroupDto removeTaskFromGroup(Long taskGroupId, Long taskDtoId) {
        TaskGroup taskGroup = findTaskGroupById(taskGroupId);
        taskGroup.removeTask(taskDtoId);
        return TaskGroupDto.toTaskGroupDto(taskGroupRepository.save(taskGroup));
    }

    public TaskGroupDto completeTaskFromGroup(Long taskGroupId, Long taskDtoId) {
        TaskGroup taskGroup = findTaskGroupById(taskGroupId);
        taskGroup.completeTask(taskDtoId);
        return TaskGroupDto.toTaskGroupDto(taskGroupRepository.save(taskGroup));
    }

    public Set<TaskDto> findAllTasksByGroupId(Long taskGroupId) {
        TaskGroup taskGroup = findTaskGroupById(taskGroupId);
        return taskGroup.getTasksGroup().stream().map(TaskDto::toTaskDto).collect(Collectors.toSet());
    }

    public Set<TaskDto> filterByCategoryGroup(Long groupId, TaskCategory workCategory) {
        TaskGroup taskGroup = findTaskGroupById(groupId);
        return taskGroup.getTasksGroup().stream()
                .filter(task -> task.getCategory().equals(workCategory))
                .map(TaskDto::toTaskDto)
                .collect(Collectors.toSet());
    }

    public TaskDto assignTaskForGrTo(Long groupId, Long userId, Long taskId) {
        TaskGroup taskGroup = findTaskGroupById(groupId);
        Task task = taskGroup.assignTaskTo(userId, taskId);
        taskGroupRepository.save(taskGroup);
        return TaskDto.toTaskDto(taskRepository.save(task));
    }

    public TaskGroupDto createTaskForGroup(Long groupId, Long userId, TaskDto taskDto) {
        Objects.requireNonNull(taskDto);
        TaskGroup taskGroup = findTaskGroupById(groupId);
        User user = findUserById(userId);
        if(!taskGroup.getUsersGroup().contains(user)) throw new IllegalArgumentException("User not found in the group");
        Task task = TaskDto.toTask(taskDto);
        task.setUser(user);
        task.setTaskGroupTask(taskGroup);
        taskRepository.save(task);
        userRepository.save(user);
        return TaskGroupDto.toTaskGroupDto(taskGroupRepository.save(taskGroup));
    }

    public TaskDto updateTaskForGroup(Long groupId, TaskDto taskDto) {
        Objects.requireNonNull(taskDto);
        TaskGroup taskGroup = findTaskGroupById(groupId);
        Task task = TaskDto.toTask(taskDto);
        Task updatedTask = taskGroup.updateTask(task);
        taskGroupRepository.save(taskGroup);
        return TaskDto.toTaskDto(taskRepository.save(updatedTask));
    }

    private void validateLoginInfos(UserDto userDto) {
        userRepository.findByUsername(userDto.username())
                .filter(user -> passwordEncoder.matches(userDto.password(), user.getPassword()))
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));
    }


    private void validationUsernameTaken(UserDto userDto) {
        if (userRepository.findByUsername(userDto.username()).isPresent()) {
            throw new IllegalArgumentException("User with username " + userDto.username() + " already exists");
        }
    }


    private User findUserById(Long userId){
        return userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    private TaskGroup findTaskGroupById(Long taskGroupId){
        return taskGroupRepository.findById(taskGroupId).orElseThrow(() -> new IllegalArgumentException("TaskGroup not found"));
    }

    private Task findTaskById(Long taskId){
        return taskRepository.findById(taskId).orElseThrow(() -> new IllegalArgumentException("Task not found"));
    }

}
