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
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Task taskSaved = taskRepository.save(TaskDto.toTask(taskDto));
        user.addTask(taskSaved);
        userRepository.save(user);
        return TaskDto.toTaskDto(taskSaved);
    }

    public Set<TaskDto> findAllTasksByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        return user.getTasks().stream().map(TaskDto::toTaskDto).collect(Collectors.toSet());
    }

    public TaskDto updateTask(Long userId, TaskDto taskDto) {
        Objects.requireNonNull(taskDto);
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Task task = TaskDto.toTask(taskDto);
        user.updateTask(task);
        userRepository.save(user);
        return TaskDto.toTaskDto(taskRepository.save(task));
    }

    public Set<TaskDto> filterByCategory(Long userId, TaskCategory category) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        return user.getTasks().stream()
                .filter(task -> task.getCategory().equals(category))
                .map(TaskDto::toTaskDto)
                .collect(Collectors.toSet());
    }

    public TaskDto completeTask(Long userId, Long taskId){
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new IllegalArgumentException("Task not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        task.completeTask();
        user.addTaskHistory(task);
        userRepository.save(user);
        return TaskDto.toTaskDto(taskRepository.save(task));
    }

    public Set<TaskDto> findAllTasksHistoryByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        return user.getTasksHistory().stream().map(TaskDto::toTaskDto).collect(Collectors.toSet());
    }

    public TaskGroupDto createTaskGroup(String title, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        TaskGroup taskGroup = new TaskGroup(title, user);
        user.setTaskGroupUser(taskGroup);
        userRepository.save(user);
        return TaskGroupDto.toTaskGroupDto(taskGroupRepository.save(taskGroup));
    }

    public TaskGroupDto addUserToGroup(Long taskGroupId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        TaskGroup taskGroup = taskGroupRepository.findById(taskGroupId).orElseThrow(() -> new IllegalArgumentException("TaskGroup not found"));
        taskGroup.addUser(user);
        taskGroupRepository.save(taskGroup);
        return TaskGroupDto.toTaskGroupDto(taskGroup);
    }

    public TaskGroupDto removeUserFromGroup(Long taskGroupId, Long userId) {
        TaskGroup taskGroup = taskGroupRepository.findById(taskGroupId).orElseThrow(() -> new IllegalArgumentException("TaskGroup not found"));
        taskGroup.removeUser(userId);
        taskGroupRepository.save(taskGroup);
        return TaskGroupDto.toTaskGroupDto(taskGroup);
    }

    public TaskGroupDto addTaskToGroup(Long taskGroupId, TaskDto taskDto) {
        Objects.requireNonNull(taskDto);
        TaskGroup taskGroup = taskGroupRepository.findById(taskGroupId).orElseThrow(() -> new IllegalArgumentException("TaskGroup not found"));
        Task task = TaskDto.toTask(taskDto);
        taskGroup.addTask(task);
        taskGroupRepository.save(taskGroup);
        return TaskGroupDto.toTaskGroupDto(taskGroup);
    }

    public TaskGroupDto removeTaskFromGroup(Long taskGroupId, Long taskDtoId) {
        TaskGroup taskGroup = taskGroupRepository.findById(taskGroupId).orElseThrow(() -> new IllegalArgumentException("TaskGroup not found"));
        taskGroup.removeTask(taskDtoId);
        taskGroupRepository.save(taskGroup);
        return TaskGroupDto.toTaskGroupDto(taskGroup);
    }

    public TaskGroupDto completeTaskFromGroup(Long taskGroupId, Long taskDtoId) {
        TaskGroup taskGroup = taskGroupRepository.findById(taskGroupId).orElseThrow(() -> new IllegalArgumentException("TaskGroup not found"));
        taskGroup.completeTask(taskDtoId);
        taskGroupRepository.save(taskGroup);
        return TaskGroupDto.toTaskGroupDto(taskGroup);
    }

    public Set<TaskDto> findAllTasksByGroupId(Long taskGroupId) {
        TaskGroup taskGroup = taskGroupRepository.findById(taskGroupId).orElseThrow(() -> new IllegalArgumentException("TaskGroup not found"));
        return taskGroup.getTasksGroup().stream().map(TaskDto::toTaskDto).collect(Collectors.toSet());
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
}
