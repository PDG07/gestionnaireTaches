package ved.firstproject.gestionnairetaches.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
    @ToString.Exclude
    private Set<Task> tasks = new HashSet<>();

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void removeTask(Task task) {
        tasks.remove(task);
    }

    public void updateTask(Task task) {
        Task existingTask = tasks.stream()
                .filter(t -> t.getId().equals(task.getId()))
                .findFirst()
                .orElse(null);

        if (existingTask != null) {
            existingTask.setTitle(task.getTitle());
            existingTask.setDescription(task.getDescription());
            existingTask.setStatus(task.getStatus());
            existingTask.setPriority(task.getPriority());
            existingTask.setDeadline(task.getDeadline());
            existingTask.setCategory(task.getCategory());
        }
    }


}


