package com.dandobai.user_handler.repositories;

import com.dandobai.user_handler.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String username);

    boolean existsByEmail(String email);

    Optional<User> findByUsername(String userName);
    @Query("SELECT AVG(EXTRACT(YEAR FROM CURRENT_DATE) - EXTRACT(YEAR FROM u.birthday)) FROM User u")
    Double findAverageAge();
}
