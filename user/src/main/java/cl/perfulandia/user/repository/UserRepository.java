package cl.perfulandia.user.repository;
import cl.perfulandia.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
public interface UserRepository extends JpaRepository<User, Long> {

}