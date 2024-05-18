package ved.firstproject.gestionnairetaches.model.enums;

import lombok.Getter;

@Getter
public enum TaskState {
    COMPLETED("Completed"),
    TODO("Todo");

    private final String name;

    TaskState(String name) {
        this.name = name;
    }
}
