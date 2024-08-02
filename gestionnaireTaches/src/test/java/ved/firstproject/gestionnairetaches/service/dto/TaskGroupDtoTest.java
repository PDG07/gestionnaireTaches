package ved.firstproject.gestionnairetaches.service.dto;

import org.junit.jupiter.api.Test;
import ved.firstproject.gestionnairetaches.model.Task;
import ved.firstproject.gestionnairetaches.model.TaskGroup;
import ved.firstproject.gestionnairetaches.model.User;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TaskGroupDtoTest {

    @Test
    void toTaskGroupDto() {
        User user = new User(1L, "username", "password", Set.of(), Set.of());
        Task task = new Task(1L, "title", "description", null, null, null, null, null, user);
        TaskGroup taskGroup = new TaskGroup(1L, "title", Set.of(user), Set.of(task));

        TaskGroupDto taskGroupDto = TaskGroupDto.toTaskGroupDto(taskGroup);

        assertEquals(taskGroup.getId(), taskGroupDto.id());
        assertEquals(taskGroup.getTitle(), taskGroupDto.title());
        assertEquals(taskGroup.getUsersGroup().size(), taskGroupDto.usersGroup().size());
        assertEquals(taskGroup.getTasksGroup().size(), taskGroupDto.tasksGroup().size());
    }
}