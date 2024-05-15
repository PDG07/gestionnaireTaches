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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServiceTaskManagerTest {
    @InjectMocks
    private ServiceTaskManager serviceTaskManager;
    @Mock
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    void createTask() {
        when(taskRepository.save(any(Task.class))).thenReturn(new Task(1L, "title", "description", "status", "priority", "deadline", "category"));
        TaskDto taskDto = new TaskDto("title", "description", "status", "priority", "deadline", "category");
        TaskDto taskDtoCreated = serviceTaskManager.createTask(taskDto);
        assertEquals(taskDtoCreated, new TaskDto(1L, "title", "description", "status", "priority", "deadline", "category"));
    }

    @Test
    void listTasks() {

    }

    @Test
    void updateTask() {

    }

    @Test
    void recoverTask() {

    }
}