package ved.firstproject.gestionnairetaches.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ved.firstproject.gestionnairetaches.model.enums.TaskPriority;
import ved.firstproject.gestionnairetaches.model.enums.TaskState;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TaskGroupTest {
    TaskGroup taskGroup;
    User user;
    TaskPriority priorityHigh = TaskPriority.HIGH;

    @BeforeEach
    void setUp() {
        user = new User(1L, "username", "password", Set.of(), Set.of());
        taskGroup = new TaskGroup("name", user);
    }

    @Test
    void addUser() {
        taskGroup.addUser(user);

        assertEquals(1, taskGroup.getUsersGroup().size());
    }

    @Test
    void removeUser() {

        taskGroup.addUser(user);
        taskGroup.removeUser(user.getId());

        assertEquals(0, taskGroup.getUsersGroup().size());
    }

    @Test
    void addTask() {
        Task task = new Task(1L, "title", "description", priorityHigh, null, null, null);
        taskGroup.addTask(task);

        assertEquals(1, taskGroup.getTasksGroup().size());
    }

    @Test
    void removeTask() {
        Task task = new Task(1L, "title", "description", priorityHigh, null, null, null);

        taskGroup.addTask(task);
        taskGroup.removeTask(task.getId());

        assertEquals(0, taskGroup.getTasksGroup().size());
    }

    @Test
    void completeTask() {
        Task task = new Task(1L, "title", "description", priorityHigh, null, null, null);

        taskGroup.addTask(task);
        taskGroup.completeTask(task.getId());

        assertEquals(0, taskGroup.getTasksGroup().size());
        assertEquals(TaskState.COMPLETED, task.getStatus());
        assertEquals(LocalDate.now(), task.getCompletionDate());
        assertEquals(1, taskGroup.getTasksGroupHistory().size());
    }

}