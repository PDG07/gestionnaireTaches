package ved.firstproject.gestionnairetaches.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "app_user")
public class User extends EntityContainer{
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @ToString.Exclude
    private Set<Task> tasks = new HashSet<>();
    @Column(name = "tasks_history")
    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private Set<Task> tasksHistory = new HashSet<>();
    @ManyToOne
    private TaskGroup taskGroupUser;

    public User(Long id, String username, String password, Set<Task> tasks) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.tasks = tasks;
    }

    public void addTask(Task task) {
        Objects.requireNonNull(task);
        tasks.add(task);
    }

    public void addTaskHistory(Task task) {
        Objects.requireNonNull(task);
        tasksHistory.add(task);
    }

    public void removeTask(Task task) {
        Objects.requireNonNull(task);
        tasks.remove(task);
    }

    @Transactional
    public Task updateTask(Task task) {
        Objects.requireNonNull(task);
        Task existingTask = existingTask(task.getId());
        return existingTask.updateTask(task);
    }

    private Task existingTask(Long taskId) {
        return findTaskById(tasks, taskId);
    }

}


