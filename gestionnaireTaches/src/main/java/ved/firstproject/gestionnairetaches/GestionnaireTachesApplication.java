package ved.firstproject.gestionnairetaches;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ved.firstproject.gestionnairetaches.model.Task;
import ved.firstproject.gestionnairetaches.model.TaskGroup;
import ved.firstproject.gestionnairetaches.model.enums.TaskCategory;
import ved.firstproject.gestionnairetaches.model.enums.TaskPriority;
import ved.firstproject.gestionnairetaches.model.enums.TaskState;
import ved.firstproject.gestionnairetaches.service.ServiceTaskManager;
import ved.firstproject.gestionnairetaches.service.dto.TaskDto;
import ved.firstproject.gestionnairetaches.service.dto.TaskGroupDto;
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
        /*// Création des utilisateurs
        UserDto firstUser = serviceTaskManager.createUser(new UserDto(null, "UserForTest", "password", Set.of()));
        UserDto secondUser = serviceTaskManager.createUser(new UserDto(null, "UserForTest2", "password", Set.of()));

        // Ajout de journaux pour vérifier les utilisateurs créés
        System.out.println("First user created: " + firstUser);
        System.out.println("Second user created: " + secondUser);

        // Vérification si les utilisateurs existent avant de les utiliser
        try {
            UserDto fetchedFirstUser = serviceTaskManager.findUserByUsername("UserForTest");
            TaskGroupDto taskGroupDto = serviceTaskManager.createTaskGroup("GroupForTest", fetchedFirstUser.id());
            System.out.println("Task group created: " + taskGroupDto);
        } catch (Exception e) {
            System.err.println("Error creating task group: " + e.getMessage());
        }

        try {
            UserDto fetchedSecondUser = serviceTaskManager.findUserByUsername("UserForTest2");
            System.out.println("Second user fetched: " + fetchedSecondUser);
        } catch (Exception e) {
            System.err.println("Error fetching second user: " + e.getMessage());
        }

        // Ajout de secondUser au groupe
        try {
            UserDto fetchedSecondUser = serviceTaskManager.findUserByUsername("UserForTest2");
            TaskGroupDto updatedTaskGroupDto = serviceTaskManager.addUserToGroup(1L, fetchedSecondUser.id());
            System.out.println("Updated task group with second user: " + updatedTaskGroupDto);
        } catch (Exception e) {
            System.err.println("Error adding user to group: " + e.getMessage());
        }*/

    }

}
