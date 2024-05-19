package ved.firstproject.gestionnairetaches.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ved.firstproject.gestionnairetaches.model.TaskGroup;

public interface ITaskGroupRepository extends JpaRepository<TaskGroup, Long> {
}
