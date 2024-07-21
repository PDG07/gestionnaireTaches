package ved.firstproject.gestionnairetaches.service.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ved.firstproject.gestionnairetaches.model.Task;
import ved.firstproject.gestionnairetaches.model.enums.TaskCategory;
import ved.firstproject.gestionnairetaches.model.enums.TaskPriority;
import ved.firstproject.gestionnairetaches.model.enums.TaskState;
import ved.firstproject.gestionnairetaches.model.User;

import java.time.LocalDate;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class TaskDtoTest {
    private User user;
    private UserDto userDto;
    private Task task;
    private TaskDto taskDto;
    private TaskCategory workCategory = TaskCategory.WORK;
    private LocalDate deadline = LocalDate.now().plusWeeks(1);
    private TaskState status = TaskState.TODO;
    TaskPriority priorityHigh = TaskPriority.HIGH;

    @BeforeEach
    void setUp() {
        user = new User(1L, "username", "password", new HashSet<>(), new HashSet<>());
        userDto = new UserDto(1L, "username", "password", new HashSet<>());
        task = new Task(1L, "title", "description", priorityHigh, deadline, workCategory, user);
        taskDto = new TaskDto(1L, "title", "description", status, priorityHigh, deadline, null, workCategory, userDto);
    }

    @Test
    void toTaskDto() {
        TaskDto taskDto = TaskDto.toTaskDto(task);
        assertEquals(task.getId(), taskDto.id());
        assertEquals(task.getTitle(), taskDto.title());
        assertEquals(task.getDescription(), taskDto.description());
        assertEquals(task.getStatus(), taskDto.status());
        assertEquals(task.getPriority(), taskDto.priority());
        assertEquals(task.getDeadline(), taskDto.deadline());
        assertEquals(task.getCategory(), taskDto.category());
        assertEquals(task.getUser().getId(), taskDto.user().id());
        assertEquals(task.getUser().getUsername(), taskDto.user().username());
        assertEquals(task.getUser().getPassword(), taskDto.user().password());
        assertEquals(task.getUser().getTasks().size(), taskDto.user().tasks().size());
    }

    @Test
    void toTask() {
        Task task = TaskDto.toTask(taskDto);
        assertEquals(taskDto.id(), task.getId());
        assertEquals(taskDto.title(), task.getTitle());
        assertEquals(taskDto.description(), task.getDescription());
        assertEquals(taskDto.status(), task.getStatus());
        assertEquals(taskDto.priority(), task.getPriority());
        assertEquals(taskDto.deadline(), task.getDeadline());
        assertEquals(taskDto.category(), task.getCategory());
        assertEquals(taskDto.user().id(), task.getUser().getId());
        assertEquals(taskDto.user().username(), task.getUser().getUsername());
        assertEquals(taskDto.user().password(), task.getUser().getPassword());
        assertEquals(taskDto.user().tasks().size(), task.getUser().getTasks().size());
    }
}