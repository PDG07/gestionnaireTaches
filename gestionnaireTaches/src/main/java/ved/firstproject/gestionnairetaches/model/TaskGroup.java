package ved.firstproject.gestionnairetaches.model;

import jakarta.persistence.*;
import lombok.*;

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

    public TaskGroup(String title) {
        this.title = title;
    }

    //Set of users
    //Set of tasks
    //Set of tasksGroupHistory
}
