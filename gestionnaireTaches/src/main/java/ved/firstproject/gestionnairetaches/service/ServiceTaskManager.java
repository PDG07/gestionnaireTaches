package ved.firstproject.gestionnairetaches.service;

import ved.firstproject.gestionnairetaches.dao.TaskRepository;
import ved.firstproject.gestionnairetaches.model.Task;
import org.springframework.stereotype.Service;
import ved.firstproject.gestionnairetaches.service.dto.TaskDto;

import java.util.HashSet;
import java.util.Set;

@Service
public class ServiceTaskManager {
    private final TaskRepository taskRepository;

    public ServiceTaskManager(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public TaskDto createTask(TaskDto taskDto) {
        Task task = TaskDto.toTask(taskDto);
        return TaskDto.taskDto(taskRepository.save(task));
    }

    public Set<Task> listTasks() {
        return new HashSet<>(taskRepository.findAll());
    }
}
