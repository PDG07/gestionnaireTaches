package ved.firstproject.gestionnairetaches.service.dto.data;

import lombok.Data;

@Data
public class TaskGroupData {
    private Long groupId;
    private Long userId;
    private String title;
}
