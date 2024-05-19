package ved.firstproject.gestionnairetaches.model;

import jakarta.persistence.*;
import lombok.*;
import ved.firstproject.gestionnairetaches.model.enums.TaskCategory;
import ved.firstproject.gestionnairetaches.model.enums.TaskState;

import java.time.LocalDate;

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
    private TaskState status = TaskState.TODO;
    @Column(name = "priority")
    private String priority;
    @Column(name = "deadline")
    private LocalDate deadline;
    @Column(name = "completinDate")
    private LocalDate completionDate = null;
    @Column(name = "category")
    private TaskCategory category;
    @ManyToOne
    @ToString.Exclude
    private User user;
    @ManyToOne
    private TaskGroup taskGroupTask;

    public Task(Long id, String title, String description, String priority, LocalDate deadline, TaskCategory category, User user) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.deadline = deadline;
        this.category = category;
        this.user = user;
    }

    public Task(Long id, String title, String description, TaskState status, String priority, LocalDate deadline, LocalDate completionDate, TaskCategory category, User user) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.deadline = deadline;
        this.completionDate = completionDate;
        this.category = category;
        this.user = user;
    }

}
