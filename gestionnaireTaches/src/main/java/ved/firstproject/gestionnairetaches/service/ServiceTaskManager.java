package ved.firstproject.gestionnairetaches.service;

import ved.firstproject.gestionnairetaches.dao.ITaskRepository;
import ved.firstproject.gestionnairetaches.dao.IUserRepository;
import ved.firstproject.gestionnairetaches.model.Task;
import org.springframework.stereotype.Service;
import ved.firstproject.gestionnairetaches.model.TaskCategory;
import ved.firstproject.gestionnairetaches.model.User;
import ved.firstproject.gestionnairetaches.service.dto.TaskDto;
import ved.firstproject.gestionnairetaches.service.dto.UserDto;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ServiceTaskManager {
    private final ITaskRepository taskRepository;
    private final IUserRepository userRepository;

    public ServiceTaskManager(ITaskRepository taskRepository, IUserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public UserDto createUser(UserDto userDto) {
        Objects.requireNonNull(userDto);
        User user = userRepository.save(UserDto.toUser(userDto));
        return UserDto.toUserDto(user);
    }

    public TaskDto createTask(Long userId, TaskDto taskDto) {
        Objects.requireNonNull(taskDto);
        User user = userRepository.findById(userId).orElseThrow(null);
        Task taskSaved = taskRepository.save(TaskDto.toTask(taskDto));
        user.addTask(taskSaved);
        userRepository.save(user);
        return TaskDto.toTaskDto(taskSaved);
    }

    public Set<TaskDto> listTasks(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(null);
        return user.getTasks().stream().map(TaskDto::toTaskDto).collect(Collectors.toSet());
    }

    public void updateTask(Long userId, TaskDto taskDto) {
        Objects.requireNonNull(taskDto);
        User user = userRepository.findById(userId).orElseThrow(null);
        Task task = TaskDto.toTask(taskDto);
        user.updateTask(task);
        userRepository.save(user);
        taskRepository.save(task);
    }

    public Set<TaskDto> filterByCategory(Long userId, TaskCategory category) {
        User user = userRepository.findById(userId).orElseThrow(null);
        return user.getTasks().stream()
                .filter(task -> task.getCategory().equals(category))
                .map(TaskDto::toTaskDto)
                .collect(Collectors.toSet());
    }


}
