package ved.firstproject.gestionnairetaches.dao;

import ved.firstproject.gestionnairetaches.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITaskRepository extends JpaRepository<Task, Long> {
}
