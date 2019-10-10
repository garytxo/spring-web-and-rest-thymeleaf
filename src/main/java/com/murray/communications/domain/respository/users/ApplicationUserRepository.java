package com.murray.communications.domain.respository.users;

import com.murray.communications.domain.entities.users.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * JPA repo to extract application users
 */
public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Long> {

    Optional<ApplicationUser> findByUsername(String userName);


    Optional<ApplicationUser> findByUsernameAndPassword(String userName, String password);
}
