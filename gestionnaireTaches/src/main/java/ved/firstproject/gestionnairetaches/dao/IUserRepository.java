package ved.firstproject.gestionnairetaches.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ved.firstproject.gestionnairetaches.model.User;

import java.util.Optional;
import java.util.Set;

public interface IUserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
