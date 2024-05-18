package ved.firstproject.gestionnairetaches.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ved.firstproject.gestionnairetaches.model.enums.TaskCategory;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    private Set<Task> tasks;
    private User user;
    private Task task;
    private final TaskCategory workCategory = TaskCategory.WORK;
    private final LocalDate deadline = LocalDate.now().plusWeeks(1);

    @BeforeEach
    void setUp() {
        tasks = new HashSet<>();
        user = new User(1L, "username", "password", tasks);
        task = new Task(1L, "title", "description", "priority", deadline, workCategory, user);
    }

    @Test
    void addTask() {
        user.addTask(task);

        assertEquals(1, user.getTasks().size());
    }

    @Test
    void removeTask() {
        Task task2 = new Task(2L, "title", "description", "priority", deadline, workCategory, user);
        Task task3= new Task(3L, "title", "description", "priority", deadline, workCategory, user);

        user.addTask(task);
        user.addTask(task2);
        user.addTask(task3);

        user.removeTask(task);

        assertEquals(2, user.getTasks().size());
    }

    @Test
    void updateTask() {
        task.setTitle("new title");
        user.addTask(task);

        user.updateTask(task);

        user.getTasks().stream().findFirst().ifPresent(t -> assertEquals("new title", t.getTitle()));
    }
}