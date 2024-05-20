package ved.firstproject.gestionnairetaches.model.enums;

import lombok.Getter;

@Getter
public enum TaskPriority {
    HIGH("High"),
    AVERAGE("Average"),
    LOW("Low");

    private final String name;

    TaskPriority(String name) {
        this.name = name;
    }
}
