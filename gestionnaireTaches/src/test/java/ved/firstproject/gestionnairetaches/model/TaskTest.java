package ved.firstproject.gestionnairetaches.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ved.firstproject.gestionnairetaches.model.enums.TaskState;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void completeTask() {
        TaskState status = TaskState.TODO;
        Task task = new Task(1L, "title", "description", "priority", null, null, null);

        task.completeTask();

        assertEquals(TaskState.COMPLETED, task.getStatus());
        assertEquals(task.getCompletionDate(), LocalDate.now());
    }
}