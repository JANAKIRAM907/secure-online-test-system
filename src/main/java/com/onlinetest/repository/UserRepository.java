package com.onlinetest.repository;

import com.onlinetest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByRollNumber(String rollNumber);

    Optional<User> findByNameAndRollNumber(String name, String rollNumber);

    boolean existsByRollNumber(String rollNumber);
}
