package ved.firstproject.gestionnairetaches.service;

import ved.firstproject.gestionnairetaches.dao.TaskRepository;
import ved.firstproject.gestionnairetaches.dao.UserRepository;
import ved.firstproject.gestionnairetaches.model.Task;
import org.springframework.stereotype.Service;
import ved.firstproject.gestionnairetaches.model.User;
import ved.firstproject.gestionnairetaches.service.dto.TaskDto;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ServiceTaskManager {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public ServiceTaskManager(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public TaskDto createTask(TaskDto taskDto) {
        Task task = TaskDto.toTask(taskDto);
        return TaskDto.toTaskDto(taskRepository.save(task));
    }

    public Set<TaskDto> listTasks(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(null);
        return user.getTasks().stream().map(TaskDto::toTaskDto).collect(Collectors.toSet());
    }

    public void updateTask(TaskDto taskDto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(null);
        Task task = TaskDto.toTask(taskDto);
        user.updateTask(task);
        userRepository.save(user);
        taskRepository.save(task);
    }

}
