package ved.firstproject.gestionnairetaches.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import ved.firstproject.gestionnairetaches.dao.ITaskRepository;
import ved.firstproject.gestionnairetaches.dao.IUserRepository;
import ved.firstproject.gestionnairetaches.model.Task;
import org.springframework.stereotype.Service;
import ved.firstproject.gestionnairetaches.model.TaskCategory;
import ved.firstproject.gestionnairetaches.model.User;
import ved.firstproject.gestionnairetaches.service.dto.TaskDto;
import ved.firstproject.gestionnairetaches.service.dto.UserDto;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ServiceTaskManager {
    private final ITaskRepository taskRepository;
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ServiceTaskManager(ITaskRepository taskRepository, IUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
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

    @Transactional
    public TaskDto createTask(Long userId, TaskDto taskDto) {
        Objects.requireNonNull(taskDto);
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Task taskSaved = taskRepository.save(TaskDto.toTask(taskDto));
        user.addTask(taskSaved);
        userRepository.save(user);
        return TaskDto.toTaskDto(taskSaved);
    }

    @Transactional
    public Set<TaskDto> listTasks(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        return user.getTasks().stream().map(TaskDto::toTaskDto).collect(Collectors.toSet());
    }

    public void updateTask(Long userId, TaskDto taskDto) {
        Objects.requireNonNull(taskDto);
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Task task = TaskDto.toTask(taskDto);
        user.updateTask(task);
        userRepository.save(user);
        taskRepository.save(task);
    }

    public Set<TaskDto> filterByCategory(Long userId, TaskCategory category) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        return user.getTasks().stream()
                .filter(task -> task.getCategory().equals(category))
                .map(TaskDto::toTaskDto)
                .collect(Collectors.toSet());
    }

    private boolean validationLoginInfos(UserDto userDto) {
        return userRepository.findByUsername(userDto.username())
                .map(user -> passwordEncoder.matches(userDto.password(), user.getPassword()))
                .orElse(false);
    }

    private void validationUsernameTaken(UserDto userDto) {
        if (userRepository.findByUsername(userDto.username()).isPresent()) {
            throw new IllegalArgumentException("User with username " + userDto.username() + " already exists");
        }
    }
}
