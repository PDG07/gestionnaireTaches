package ved.firstproject.gestionnairetaches.model;

import jakarta.persistence.*;
import lombok.*;
import ved.firstproject.gestionnairetaches.model.enums.TaskState;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class TaskGroup {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;
    @Column(name = "title")
    private String title;
    @OneToMany(mappedBy = "taskGroupUser", cascade = CascadeType.PERSIST)
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

    public void removeUser(User user) {
        Objects.requireNonNull(user);
        this.usersGroup.remove(user);
    }

    public void addTask(Task task) {
        Objects.requireNonNull(task);
        this.tasksGroup.add(task);
    }

    public void removeTask(Task task) {
        Objects.requireNonNull(task);
        this.tasksGroup.remove(task);
    }

    public void completeTask(Task task) {
        Objects.requireNonNull(task);
        this.tasksGroup.remove(task);
        task.setStatus(TaskState.COMPLETED);
        this.tasksGroupHistory.add(task);
    }

    public void addTasksHistory(Task task) {
        Objects.requireNonNull(task);
        this.tasksGroupHistory.add(task);
    }


}
