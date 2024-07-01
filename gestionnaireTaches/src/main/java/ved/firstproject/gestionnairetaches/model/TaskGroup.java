package ved.firstproject.gestionnairetaches.model;

import jakarta.persistence.*;
import lombok.*;
import ved.firstproject.gestionnairetaches.model.enums.TaskState;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class TaskGroup extends EntityContainer{
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;
    @Column(name = "title")
    private String title;
    @ManyToMany(mappedBy = "taskGroups", cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<User> usersGroup = new HashSet<>();
    @OneToMany(mappedBy = "taskGroupTask", cascade = CascadeType.PERSIST)
    private Set<Task> tasksGroup = new HashSet<>();
    @OneToMany(mappedBy = "taskGroupTask", cascade = CascadeType.PERSIST)
    private Set<Task> tasksGroupHistory = new HashSet<>();

    public TaskGroup(String title, User user) {
        this.title = title;
        this.usersGroup.add(user);
    }

    public void addUser(User user) {
        Objects.requireNonNull(user);
        this.usersGroup.add(user);
    }

    public void removeUser(Long userId) {
        User userToRemove = existingUser(userId);
        this.usersGroup.remove(userToRemove);
    }

    public void addTask(Task task) {
        Objects.requireNonNull(task);
        task.setTaskGroupTask(this);
        this.tasksGroup.add(task);
    }

    public void removeTask(Long taskId) {
        Task taskToRemove = existingTask(taskId);
        this.tasksGroup.remove(taskToRemove);
    }

    public void completeTask(Long taskId) {
        Task taskToRemove = existingTask(taskId);
        this.tasksGroup.remove(taskToRemove);
        taskToRemove.setStatus(TaskState.COMPLETED);
        taskToRemove.setCompletionDate(LocalDate.now());
        addTasksHistory(taskToRemove);
    }

    public Task assignTaskTo(Long userId, Long taskId){
        Task taskToAssign = existingTask(taskId);
        User userAssignedTo = existingUser(userId);
        taskToAssign.setUser(userAssignedTo);
        return taskToAssign;
    }

    private void addTasksHistory(Task task) {
        Objects.requireNonNull(task);
        this.tasksGroupHistory.add(task);
    }


    public Task updateTask(Task task) {
        Objects.requireNonNull(task);
        Task existingTask = existingTask(task.getId());
        return existingTask.updateTask(task);
    }

    private User existingUser(Long userId) {
        return findUserById(usersGroup, userId);
    }

    private Task existingTask(Long taskId) {
        return findTaskById(tasksGroup, taskId);

    }
}