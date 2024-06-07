package ved.firstproject.gestionnairetaches.service.dto.data;

import lombok.Data;
import ved.firstproject.gestionnairetaches.model.enums.TaskCategory;
import ved.firstproject.gestionnairetaches.model.enums.TaskPriority;
import ved.firstproject.gestionnairetaches.model.enums.TaskState;

import java.time.LocalDate;

@Data
public class TaskData {
    private String title;
    private String description;
    private TaskState status;
    private TaskPriority priority;
    private LocalDate deadline;
    private LocalDate completionDate;
    private TaskCategory category;
    private Long userId;
}
