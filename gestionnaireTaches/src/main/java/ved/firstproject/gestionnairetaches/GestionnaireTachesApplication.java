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
        TaskCategory workCategory = TaskCategory.WORK;
        TaskCategory personalCategory = TaskCategory.PERSONAL;
        TaskPriority priorityHigh = TaskPriority.HIGH;
        UserDto userDto = serviceTaskManager.createUser(new UserDto(1L, "username", "password", Set.of()));
        TaskDto taskDto = new TaskDto(1L, "title", "description", TaskState.TODO, priorityHigh, LocalDate.now().plusWeeks(1), null,  workCategory, userDto);
        TaskDto taskDto2 = new TaskDto(2L,"title2", "description2", TaskState.TODO, priorityHigh, LocalDate.now().plusWeeks(2), null, personalCategory, userDto);
        TaskDto tastUpdated = new TaskDto(1L, "titleUpdated", "descriptionUpdated", TaskState.TODO, TaskPriority.AVERAGE, LocalDate.now().plusWeeks(1), LocalDate.now(), workCategory, userDto);
        String username = userDto.username();

        System.out.println("\n");
        System.out.println("Creation d'une tache pour " + username + " : " + serviceTaskManager.createTask(userDto.id(), taskDto));
        System.out.println("Creation d'une 2e tache pour " + username + " : " + serviceTaskManager.createTask(userDto.id(), taskDto2) + "\n");

        System.out.println("Taches de l'utilistaeur " + username + " : " + serviceTaskManager.findAllTasksByUserId(userDto.id()) + "\n");
        System.out.println("Tache mise a jour : " + serviceTaskManager.updateTask(userDto.id(), tastUpdated));
        System.out.println("Taches de l'utilistaeur " + username + " filtrer par "+ workCategory + " : " + serviceTaskManager.filterByCategory(userDto.id(), workCategory));
        System.out.println("Taches de l'utilistaeur " + username + " filtrer par "+ personalCategory + " : " + serviceTaskManager.filterByCategory(userDto.id(), personalCategory));
        System.out.println("Tache complété : " + serviceTaskManager.completeTask(userDto.id(), taskDto.id()) + "\n");

        TaskGroupDto taskGroupDtoG = serviceTaskManager.createTaskGroup("Groupe 1", userDto.id());
        System.out.println("Creation d'un groupe : " + taskGroupDtoG);
        TaskGroupDto taskGroupDto = serviceTaskManager.addUserToGroup(taskGroupDtoG.id(), 1L);
        System.out.println("Ajout d'un utilisateur au groupe :  " + taskGroupDto);
        System.out.println("Retiration d'un utilisateur du groupe : " + serviceTaskManager.removeUserFromGroup(taskGroupDto.id(), userDto.id()));
        System.out.println("Ajout d'un utilisateur au groupe2 :  " + serviceTaskManager.addUserToGroup(taskGroupDtoG.id(), 1L));
        TaskDto taskDto3 = new TaskDto(3L, "title3", "description3", TaskState.TODO, priorityHigh, LocalDate.now().plusWeeks(3), null, workCategory, userDto);
        serviceTaskManager.addTaskToGroup(taskGroupDto.id(), serviceTaskManager.createTask(userDto.id(), taskDto3));
        TaskGroupDto taskGroupDto2 = serviceTaskManager.addTaskToGroup(taskGroupDto.id(), serviceTaskManager.createTask(userDto.id(), taskDto));
        System.out.println("Ajout de tache au groupe : " + taskGroupDto2);
        System.out.println("Taches du groupe : " + serviceTaskManager.findAllTasksByGroupId(taskGroupDto2.id()));
        //TaskGroupDto gr2 = serviceTaskManager.removeTaskFromGroup(taskGroupDto2.id(), 1L);
        //System.out.println(gr2);
        System.out.println("Taches du groupe filtrer par "+ workCategory + " : " + serviceTaskManager.filterByCategoryGroup(taskGroupDto.id(), workCategory));
        System.out.println("Task assigned to : " + serviceTaskManager.assignTaskTo(taskGroupDto2.id(), 1L, 1L));
        TaskDto taskDto4 = new TaskDto(1L, "title4", "description4", TaskState.TODO, priorityHigh, LocalDate.now().plusWeeks(4), null, personalCategory, userDto);
        System.out.println("Task group updated : " + serviceTaskManager.updateTaskForGroup(taskGroupDto2.id(), taskDto4));


    }

}
