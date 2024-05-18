package ved.firstproject.gestionnairetaches.model.enums;

import lombok.Getter;

@Getter
public enum TaskCategory {
    WORK("Work"),
    PERSONAL("Personal"),
    SHOPPING("Shopping"),
    SPORTS("Sports"),
    OTHER("Other");

    private final String name;

    TaskCategory(String name) {
        this.name = name;
    }

}
