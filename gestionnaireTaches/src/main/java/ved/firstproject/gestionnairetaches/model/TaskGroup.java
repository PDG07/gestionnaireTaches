package ved.firstproject.gestionnairetaches.model;

import jakarta.persistence.*;

@Entity
public class TaskGroup {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    //Set of users
    //Set of tasks
}
