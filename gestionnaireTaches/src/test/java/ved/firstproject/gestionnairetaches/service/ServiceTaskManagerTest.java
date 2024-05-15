package ved.firstproject.gestionnairetaches.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ved.firstproject.gestionnairetaches.dao.TaskRepository;
import ved.firstproject.gestionnairetaches.model.Task;
import ved.firstproject.gestionnairetaches.service.dto.TaskDto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServiceTaskManagerTest {
    @InjectMocks
    private ServiceTaskManager serviceTaskManager;

    @Mock
    private TaskRepository taskRepository;

    TaskDto taskDtoInit;
    Task taskInit;

    @BeforeEach
    void setUp() {
        taskDtoInit = new TaskDto(1L, "title", "description", "status", "priority", "deadline", "category");
        taskInit = new Task(2L, "title", "description", "status", "priority", "deadline", "category");
    }

    @Test
    void createTask() {
        when(taskRepository.save(any(Task.class))).thenReturn(new Task(1L, "title", "description", "status", "priority", "deadline", "category"));
        TaskDto taskDto = new TaskDto("title", "description", "status", "priority", "deadline", "category");
        TaskDto taskDtoCreated = serviceTaskManager.createTask(taskDto);
        assertEquals(taskDtoCreated, taskDtoInit);
    }

    @Test
    void listTasks() {
        List<Task> taskListInit = new ArrayList<>(Set.of(taskInit));
        when(taskRepository.findAll()).thenReturn(taskListInit);
        Set<Task> tasksList = serviceTaskManager.listTasks();
        assertEquals(tasksList, new HashSet<>(taskListInit));
    }

    @Test
    void updateTask() {

    }

    @Test
    void recoverTask() {

    }
}