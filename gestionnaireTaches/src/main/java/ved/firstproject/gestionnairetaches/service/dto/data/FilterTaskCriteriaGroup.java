package ved.firstproject.gestionnairetaches.service.dto.data;

import lombok.Data;
import ved.firstproject.gestionnairetaches.model.enums.TaskCategory;

@Data
public class FilterTaskCriteriaGroup {
    private Long groupId;
    private TaskCategory category;
}
