package ved.firstproject.gestionnairetaches.service.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ved.firstproject.gestionnairetaches.model.User;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class UserDtoTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void toUserDto() {
        User user = new User(1L, "username", "password", new HashSet<>(), new HashSet<>());
        UserDto userDtoExpected = new UserDto(1L, "username", "password", new HashSet<>());

        UserDto userDto = UserDto.toUserDto(user);

        assertEquals(userDtoExpected.id(), userDto.id());
        assertEquals(userDtoExpected.username(), userDto.username());
        assertEquals(userDtoExpected.password(), userDto.password());
        assertEquals(userDtoExpected.tasks(), userDto.tasks());
    }

    @Test
    void toUser() {
        UserDto userDto = new UserDto(1L, "username", "password", new HashSet<>());
        User userExpected = new User(1L, "username", "password", new HashSet<>(), new HashSet<>());

        User user = UserDto.toUser(userDto);

        assertEquals(userExpected.getId(), user.getId());
        assertEquals(userExpected.getUsername(), user.getUsername());
        assertEquals(userExpected.getPassword(), user.getPassword());
        assertEquals(userExpected.getTasks(), user.getTasks());
    }
}