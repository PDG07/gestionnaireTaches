package ved.firstproject.gestionnairetaches.model;

import java.util.Set;
import java.util.function.Function;

public class EntityContainer {
    protected <T> T findById(Set<T> entities, Long id, Function<T, Long> idGetter, String notFoundMessage) {
        return entities.stream()
                .filter(entity -> idGetter.apply(entity).equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(notFoundMessage));
    }

    protected User findUserById(Set<User> users, Long userId) {
        return findById(users, userId, User::getId, "User not found");
    }

    protected Task findTaskById(Set<Task> tasks, Long taskId) {
        return findById(tasks, taskId, Task::getId, "Task not found");
    }
}
