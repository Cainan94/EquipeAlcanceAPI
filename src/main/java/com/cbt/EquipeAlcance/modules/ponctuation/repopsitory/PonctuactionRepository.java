package com.cbt.EquipeAlcance.modules.ponctuation.repopsitory;

import com.cbt.EquipeAlcance.modules.ponctuation.model.Ponctuation;
import com.cbt.EquipeAlcance.modules.streamers.model.Streamers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PonctuactionRepository extends JpaRepository<Ponctuation,UUID> {
    List<Ponctuation> findByDateGreaterThanEqualAndDateLessThanEqualOrderByStreamers(long start, long end);

    List<Ponctuation> findByDateGreaterThanEqualAndDateLessThanEqualAndStreamers(long start, long end,Streamers streamers);

    List<Ponctuation> findByDateGreaterThanEqualAndDateLessThanEqualAndStreamersOrderByDateCreate(long start, long end,Streamers streamers);

    List<Ponctuation> findByStreamers(Streamers streamers);

    Optional<Ponctuation> findByIdPublic(UUID id);
}
