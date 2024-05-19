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
public class TaskGroup {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;
    @Column(name = "title")
    private String title;
    @OneToMany(mappedBy = "taskGroupUser", cascade = CascadeType.PERSIST)
    private Set<User> users = new HashSet<>();
    @OneToMany(mappedBy = "taskGroupTask", cascade = CascadeType.PERSIST)
    private Set<Task> tasks = new HashSet<>();
    @OneToMany(mappedBy = "taskGroupTask", cascade = CascadeType.PERSIST)
    private Set<Task> tasksGroupHistory = new HashSet<>();

    public TaskGroup(String title, User user) {
        this.title = title;
        this.users.add(user);
    }




}
