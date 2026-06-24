package com.sipokar.webapp.repository;

import com.sipokar.webapp.model.User; 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
<<<<<<< HEAD
=======
    Optional<User> findByEmail(String email);
>>>>>>> f58bcb704ca04e133dbe4515d85768387958ed38
}