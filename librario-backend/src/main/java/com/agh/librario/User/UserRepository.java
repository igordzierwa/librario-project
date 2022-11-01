package com.agh.librario.User;

import com.agh.librario.Role.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByEmail(String email);
    List<User> findUsersByRoles(Role role);
    void deleteUserByUsername(String username);
}
