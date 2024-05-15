package ved.firstproject.gestionnairetaches.service.dto;

import ved.firstproject.gestionnairetaches.model.Task;

public record TaskDto (Long id, String title, String description, String status, String priority, String deadline, String category){
    public TaskDto (String title, String description, String status, String priority, String deadline, String category){
        this(null, title, description, status, priority, deadline, category);
    }

    public static TaskDto taskDto (Task task) {
        return new TaskDto(task.getId(), task.getTitle(), task.getDescription(), task.getStatus(), task.getPriority(), task.getDeadline(), task.getCategory());
    }

    public static Task toTask(TaskDto taskDto) {
        return new Task(taskDto.id(), taskDto.title(), taskDto.description(), taskDto.status(), taskDto.priority(), taskDto.deadline(), taskDto.category());
    }
}
