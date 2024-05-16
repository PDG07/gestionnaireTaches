package ved.firstproject.gestionnairetaches.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ved.firstproject.gestionnairetaches.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
