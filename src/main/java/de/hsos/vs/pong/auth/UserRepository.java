package de.hsos.vs.pong.auth;

import de.hsos.vs.pong.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/*
 * Erklärung was JpaRepository macht: https://www.baeldung.com/the-persistence-layer-with-spring-data-jpa
 *                                    https://www.geeksforgeeks.org/springboot/spring-boot-jparepository-with-example/
 * Erklärung was Optional ist: https://docs.oracle.com/javase/8/docs/api/java/util/Optional.html
 */
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    /** Man könnte hier weitere Methoden implementieren, falls man bestimmte SQL
     * Befehle verwenden möchte wie zu z.B.
     * @Query("SELECT u FROM User u WHERE u.salt = :salt")
     * also User mit einem bestimmten salt gucken oder so.
     */
}
