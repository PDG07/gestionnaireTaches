package ved.firstproject.gestionnairetaches.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Task {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;
    @Column(name = "status")
    private String status;
    @Column(name = "priority")
    private String priority;
    @Column(name = "deadline")
    private String deadline;
    @Column(name = "category")
    private String category;
    @ManyToOne
    private User user;

    public Task(String title, String description, String status, String priority, String deadline, String category, User user) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.deadline = deadline;
        this.category = category;
        this.user = user;
    }

}
