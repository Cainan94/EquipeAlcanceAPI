package com.cbt.EquipeAlcance.modules.update.repository;

import com.cbt.EquipeAlcance.modules.update.model.UpdateModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UpdateModelRepository extends JpaRepository<UpdateModel, UUID> {
    Optional<UpdateModel> findByVisible(boolean visible);
}
