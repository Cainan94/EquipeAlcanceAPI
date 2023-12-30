package com.cbt.CAJsSystem.modules.users.repopsitory;

import com.cbt.CAJsSystem.modules.users.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsersRepository extends JpaRepository<User,UUID> {
    Optional<User> findByUsernameIgnoreCase(String login);
    Optional<User>findByIdPublic(UUID id);
}
