package com.cbt.EquipeAlcance.modules.users.repopsitory;

import com.cbt.EquipeAlcance.modules.liveSchedules.model.LiveSchedule;
import com.cbt.EquipeAlcance.modules.streamers.model.Streamers;
import com.cbt.EquipeAlcance.modules.users.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsersRepository extends JpaRepository<User,UUID> {
    Optional<User> findByUsernameIgnoreCase(String login);
    Optional<User>findByIdPublic(UUID id);

    List<User> findByDeleted(boolean deleted);

    Optional<User>findByStreamers(Streamers streamers);
}
