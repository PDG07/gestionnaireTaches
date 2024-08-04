package ved.firstproject.gestionnairetaches.presentation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ved.firstproject.gestionnairetaches.service.ServiceTaskManager;
import ved.firstproject.gestionnairetaches.service.dto.UserDto;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ServiceTaskManager serviceTaskManager;

    private UserDto userDto;
    private Set<UserDto> userDtos;

    @BeforeEach
    void setUp() {
        userDto = new UserDto(1L, "testuser", "password", new HashSet<>());
        userDtos = new HashSet<>();
        userDtos.add(userDto);
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void findUserByUsername() throws Exception {
        when(serviceTaskManager.findUserByUsername("testuser")).thenReturn(userDto);

        mockMvc.perform(get("/api/user/findUserByUsername")
                        .param("username", "testuser"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"username\":\"testuser\",\"password\":\"password\",\"tasks\":[]}"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void findAllUserFromGroup() throws Exception {
        when(serviceTaskManager.findAllUserFromGroup(1L)).thenReturn(userDtos);

        mockMvc.perform(get("/api/user/findAllUserFromGroup")
                        .param("groupId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1,\"username\":\"testuser\",\"password\":\"password\",\"tasks\":[]}]"));
    }
}