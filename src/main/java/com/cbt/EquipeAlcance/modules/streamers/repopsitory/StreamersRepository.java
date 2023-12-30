package com.cbt.EquipeAlcance.modules.streamers.repopsitory;

import com.cbt.EquipeAlcance.modules.streamers.model.Streamers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StreamersRepository extends JpaRepository<Streamers, UUID> {
    Optional<Streamers> findByTwitchName(String name);

    Optional<Streamers>findByIdPublic(UUID id);

}
