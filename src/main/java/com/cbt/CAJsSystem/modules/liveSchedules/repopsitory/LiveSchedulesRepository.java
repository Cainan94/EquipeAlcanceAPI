package com.cbt.CAJsSystem.modules.liveSchedules.repopsitory;

import com.cbt.CAJsSystem.modules.liveSchedules.model.LiveSchedule;
import com.cbt.CAJsSystem.modules.streamers.model.Streamers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LiveSchedulesRepository extends JpaRepository<LiveSchedule, UUID> {
    Optional<LiveSchedule> findByIdPublic(UUID id);
    List<LiveSchedule> findByStartTimeGreaterThanEqualAndStartTimeLessThanEqualAndVisibleOrderByStartTime (long start, long end, boolean visible);

    Optional<LiveSchedule> findFirstByStreamerOrderByStartTimeDesc(Streamers streamers);

    Optional<LiveSchedule> findByStartTime(Long startTime);

    long countByStartTimeGreaterThanEqualAndStartTimeLessThanEqualAndVisible(long start, long end, boolean visible);
}
