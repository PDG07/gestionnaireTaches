package ved.firstproject.gestionnairetaches.presentation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ved.firstproject.gestionnairetaches.model.enums.TaskCategory;
import ved.firstproject.gestionnairetaches.model.enums.TaskPriority;
import ved.firstproject.gestionnairetaches.model.enums.TaskState;
import ved.firstproject.gestionnairetaches.service.ServiceTaskManager;
import ved.firstproject.gestionnairetaches.service.dto.TaskDto;
import ved.firstproject.gestionnairetaches.service.dto.UserDto;
import ved.firstproject.gestionnairetaches.service.dto.data.TaskData;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskManagerController.class)
class TaskManagerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ServiceTaskManager serviceTaskManager;

    private UserDto userDto;
    private TaskDto taskDto;
    private Set<TaskDto> taskDtos;

    @BeforeEach
    void setUp() {
        userDto = new UserDto(1L, "testuser", "password", new HashSet<>());
        taskDto = new TaskDto(1L, "title", "description", TaskState.TODO, TaskPriority.HIGH, LocalDate.of(2024, 8, 3), null, TaskCategory.WORK, userDto);
        taskDtos = new HashSet<>();
        taskDtos.add(taskDto);
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void createUser() throws Exception {
        when(serviceTaskManager.createUser(new UserDto(null, "testuser", "password", Set.of()))).thenReturn(userDto);

        mockMvc.perform(post("/api/signup")
                        .contentType("application/json")
                        .content("{\"username\":\"testuser\",\"password\":\"password\"}")
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"id\":1,\"username\":\"testuser\",\"password\":\"password\",\"tasks\":[]}"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void login() throws Exception {
        when(serviceTaskManager.login(new UserDto(null, "testuser", "password", Set.of()))).thenReturn(userDto);

        mockMvc.perform(post("/api/login")
                        .contentType("application/json")
                        .content("{\"username\":\"testuser\",\"password\":\"password\"}")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"username\":\"testuser\",\"password\":\"password\",\"tasks\":[]}"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void createTask() throws Exception {
        when(serviceTaskManager.findUserById(1L)).thenReturn(UserDto.toUser(userDto));
        when(serviceTaskManager.createTask(1L, taskDto)).thenReturn(taskDto);

        mockMvc.perform(post("/api/createtask")
                        .contentType("application/json")
                        .content("{\"id\":1,\"title\":\"title\",\"description\":\"description\",\"status\":\"TODO\",\"priority\":\"HIGH\",\"deadline\":\"2024-08-03\",\"completionDate\":null,\"category\":\"WORK\",\"userId\":1}")
                        .with(csrf()))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void getTasks() throws Exception {
        when(serviceTaskManager.findAllTasksByUserId(1L)).thenReturn(taskDtos);

        mockMvc.perform(get("/api/tasks")
                        .param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1,\"title\":\"title\",\"description\":\"description\",\"status\":\"TODO\",\"priority\":\"HIGH\",\"deadline\":\"2024-08-03\",\"completionDate\":null,\"category\":\"WORK\",\"user\":{\"id\":1,\"username\":\"testuser\",\"password\":\"password\",\"tasks\":[]}}]"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void updateTask() throws Exception {
        UserDto userDto = new UserDto(1L, "testuser", "password", Set.of());

        TaskData taskData = new TaskData();
        taskData.setId(1L);
        taskData.setTitle("title");
        taskData.setDescription("description");
        taskData.setStatus(TaskState.TODO);
        taskData.setPriority(TaskPriority.HIGH);
        taskData.setDeadline(LocalDate.of(2024, 8, 3));
        taskData.setCompletionDate(null);
        taskData.setCategory(TaskCategory.WORK);
        taskData.setUserId(1L);

        TaskDto updatedTaskDto = new TaskDto(
                taskData.getId(),
                taskData.getTitle(),
                taskData.getDescription(),
                taskData.getStatus(),
                taskData.getPriority(),
                taskData.getDeadline(),
                taskData.getCompletionDate(),
                taskData.getCategory(),
                userDto
        );

        when(serviceTaskManager.findUserById(1L)).thenReturn(UserDto.toUser(userDto));
        when(serviceTaskManager.updateTask(anyLong(), any(TaskDto.class))).thenReturn(updatedTaskDto);

        mockMvc.perform(put("/api/updatetask")
                        .contentType("application/json")
                        .content("{\"id\":1,\"title\":\"title\",\"description\":\"description\",\"status\":\"TODO\",\"priority\":\"HIGH\",\"deadline\":\"2024-08-03\",\"completionDate\":null,\"category\":\"WORK\",\"userId\":1}")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"title\":\"title\",\"description\":\"description\",\"status\":\"TODO\",\"priority\":\"HIGH\",\"deadline\":\"2024-08-03\",\"completionDate\":null,\"category\":\"WORK\",\"user\":{\"id\":1,\"username\":\"testuser\",\"password\":\"password\",\"tasks\":[]}}"));
    }



    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void completeTask() throws Exception {
        when(serviceTaskManager.completeTask(1L, 1L)).thenReturn(taskDto);

        mockMvc.perform(put("/api/completetask/1")
                        .param("userId", "1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"title\":\"title\",\"description\":\"description\",\"status\":\"TODO\",\"priority\":\"HIGH\",\"deadline\":\"2024-08-03\",\"completionDate\":null,\"category\":\"WORK\",\"user\":{\"id\":1,\"username\":\"testuser\",\"password\":\"password\",\"tasks\":[]}}"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void getCompletedTasks() throws Exception {
        when(serviceTaskManager.completedTasks(1L)).thenReturn(taskDtos);

        mockMvc.perform(get("/api/completedtasks")
                        .param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1,\"title\":\"title\",\"description\":\"description\",\"status\":\"TODO\",\"priority\":\"HIGH\",\"deadline\":\"2024-08-03\",\"completionDate\":null,\"category\":\"WORK\",\"user\":{\"id\":1,\"username\":\"testuser\",\"password\":\"password\",\"tasks\":[]}}]"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void getTasksByCategory() throws Exception {
        when(serviceTaskManager.filterByCategory(1L, TaskCategory.WORK)).thenReturn(taskDtos);

        mockMvc.perform(get("/api/tasks/filter")
                        .param("userId", "1")
                        .param("category", "WORK"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1,\"title\":\"title\",\"description\":\"description\",\"status\":\"TODO\",\"priority\":\"HIGH\",\"deadline\":\"2024-08-03\",\"completionDate\":null,\"category\":\"WORK\",\"user\":{\"id\":1,\"username\":\"testuser\",\"password\":\"password\",\"tasks\":[]}}]"));
    }
}
