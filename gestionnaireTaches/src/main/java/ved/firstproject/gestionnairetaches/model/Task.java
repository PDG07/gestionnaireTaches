package ved.firstproject.gestionnairetaches.model;

import jakarta.persistence.*;
import lombok.*;
import ved.firstproject.gestionnairetaches.model.enums.TaskCategory;
import ved.firstproject.gestionnairetaches.model.enums.TaskPriority;
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
    private TaskPriority priority;
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
    @ToString.Exclude
    private TaskGroup taskGroupTask;

    public Task(Long id, String title, String description, TaskPriority priority, LocalDate deadline, TaskCategory category, User user) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.deadline = deadline;
        this.category = category;
        this.user = user;
    }

    public Task(Long id, String title, String description, TaskState status, TaskPriority priority, LocalDate deadline, LocalDate completionDate, TaskCategory category, User user) {
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

    public Task updateTask(Task task) {
        this.title = task.getTitle();
        this.description = task.getDescription();
        this.status = task.getStatus();
        this.priority = task.getPriority();
        this.deadline = task.getDeadline();
        this.completionDate = task.getCompletionDate();
        this.category = task.getCategory();
        return this;
    }

    public void completeTask() {
        this.status = TaskState.COMPLETED;
        this.completionDate = LocalDate.now();
    }

}
