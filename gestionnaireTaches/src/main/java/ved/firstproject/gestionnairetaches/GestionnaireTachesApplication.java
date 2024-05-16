package ved.firstproject.gestionnairetaches;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ved.firstproject.gestionnairetaches.model.TaskCategory;
import ved.firstproject.gestionnairetaches.service.ServiceTaskManager;
import ved.firstproject.gestionnairetaches.service.dto.TaskDto;
import ved.firstproject.gestionnairetaches.service.dto.UserDto;

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
        TaskDto taskDto = new TaskDto("title", "description", "status", "priority", "deadline", workCategory, null);
        UserDto userDto = new UserDto(1L, "username", "password", Set.of(taskDto));
        System.out.println(serviceTaskManager.createTask(userDto.id(), taskDto));

        System.out.println(serviceTaskManager.listTasks(userDto.id()));
    }

}
