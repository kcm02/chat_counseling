package com.project.securelogin.user.repository;

import com.project.securelogin.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByMailVerificationToken(String token);

    Optional<User> findBySocialId(String socialId);

    boolean existsByEmail(String email);

}
