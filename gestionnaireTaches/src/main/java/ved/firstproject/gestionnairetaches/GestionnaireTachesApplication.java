package ved.firstproject.gestionnairetaches;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ved.firstproject.gestionnairetaches.model.enums.TaskCategory;
import ved.firstproject.gestionnairetaches.model.enums.TaskState;
import ved.firstproject.gestionnairetaches.service.ServiceTaskManager;
import ved.firstproject.gestionnairetaches.service.dto.TaskDto;
import ved.firstproject.gestionnairetaches.service.dto.UserDto;

import java.time.LocalDate;
import java.util.Set;

@SpringBootApplication
public class GestionnaireTachesApplication implements CommandLineRunner {
    private final ServiceTaskManager serviceTaskManager;

    public GestionnaireTachesApplication(ServiceTaskManager serviceTaskManager) {
        this.serviceTaskManager = serviceTaskManager;
    }

    public static void main(String[] args) {
        SpringApplication.run(GestionnaireTachesApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        TaskCategory workCategory = TaskCategory.WORK;
        UserDto userDto = new UserDto(1L, "username", "password", Set.of());
        TaskDto taskDto = new TaskDto("title", "description", TaskState.TODO, "priority", LocalDate.now().plusWeeks(1), null,  workCategory, userDto);

        System.out.println(serviceTaskManager.createUser(userDto).password());
        System.out.println(serviceTaskManager.createUser(userDto).password());
        System.out.println(serviceTaskManager.createTask(userDto.id(), taskDto));
        System.out.println(serviceTaskManager.listTasks(userDto.id()));


    }

}
