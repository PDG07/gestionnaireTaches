package ved.firstproject.gestionnairetaches.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ved.firstproject.gestionnairetaches.model.TaskGroup;

import java.util.Optional;

public interface ITaskGroupRepository extends JpaRepository<TaskGroup, Long> {
}
