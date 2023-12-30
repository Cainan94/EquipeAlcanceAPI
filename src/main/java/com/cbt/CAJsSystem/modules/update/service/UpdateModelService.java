package com.cbt.CAJsSystem.modules.update.service;

import com.cbt.CAJsSystem.infra.errors.exceptions.BadRequestException;
import com.cbt.CAJsSystem.modules.liveSchedules.model.LiveSchedule;
import com.cbt.CAJsSystem.modules.update.http.dto.UpdateModelDTORequest;
import com.cbt.CAJsSystem.modules.update.model.UpdateModel;
import com.cbt.CAJsSystem.modules.update.repository.UpdateModelRepository;
import com.cbt.CAJsSystem.utils.DateUtils;
import com.cbt.CAJsSystem.utils.ID;
import com.cbt.CAJsSystem.utils.Security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UpdateModelService {

    @Autowired
    private UpdateModelRepository repository;

    public UpdateModel getCurrentVersion(){
        Optional<UpdateModel> optional =  repository.findByVisible(true);
        return optional.orElseGet(UpdateModel::new);
    }

    public UpdateModel insertVersion(UpdateModelDTORequest request) {
        if(!Security.isADM()){
            throw new BadRequestException("Seu usuário não tem privilégios para esta operação", "Falha de segurança");
        }
        Optional<UpdateModel> optionalVersion = repository.findByVisible(true);
        optionalVersion.ifPresent(updateModel -> repository.delete(updateModel));
        return repository.save(UpdateModel.builder()
                .id(ID.generate())
                .idPublic(ID.generate())
                .dateCreate(DateUtils.localDateTimeToEpoch(LocalDateTime.now()))
                .lastModificationDate(0)
                .urlDownload(request.getUrlDownload())
                .visible(true)
                .deleted(false)
                .version(request.getVersion())
                .build());
    }
}
