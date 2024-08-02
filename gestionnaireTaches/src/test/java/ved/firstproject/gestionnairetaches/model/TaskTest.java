package ved.firstproject.gestionnairetaches.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ved.firstproject.gestionnairetaches.model.enums.TaskPriority;
import ved.firstproject.gestionnairetaches.model.enums.TaskState;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    TaskPriority priorityHigh = TaskPriority.HIGH;

    @BeforeEach
    void setUp() {
    }

    @Test
    void completeTask() {
        TaskState status = TaskState.TODO;
        Task task = new Task(1L, "title", "description", priorityHigh, null, null, null);

        task.completeTask();

        assertEquals(TaskState.COMPLETED, task.getStatus());
        assertEquals(task.getCompletionDate(), LocalDate.now());
    }

    @Test
    void updateTask(){
        Task task = new Task(1L, "title", "description", priorityHigh, null, null, null);
        Task newTask = new Task(1L, "new title", "new description", priorityHigh, null, null, null);

        task.updateTask(newTask);

        assertEquals("new title", task.getTitle());
        assertEquals("new description", task.getDescription());
    }
}