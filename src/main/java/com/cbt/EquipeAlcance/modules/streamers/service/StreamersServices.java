package com.cbt.EquipeAlcance.modules.streamers.service;

import com.cbt.EquipeAlcance.infra.errors.exceptions.BadRequestException;
import com.cbt.EquipeAlcance.modules.streamers.http.dto.StreamersDTORequest;
import com.cbt.EquipeAlcance.modules.streamers.model.Streamers;
import com.cbt.EquipeAlcance.modules.streamers.repopsitory.StreamersRepository;
import com.cbt.EquipeAlcance.utils.DateUtils;
import com.cbt.EquipeAlcance.utils.ID;
import com.cbt.EquipeAlcance.utils.Security;
import com.cbt.EquipeAlcance.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class StreamersServices {

    @Autowired
    private StreamersRepository repository;

    @Transactional
    public Streamers doUpdate(StreamersDTORequest dtoRequest) {
        try {
            if(!Security.isADM()){
                throw new BadRequestException("Seu usuário não tem privilégios para esta operação", "Falha de segurança");
            }
            Optional<Streamers> optionalStreamers = repository.findByIdPublic(ID.toUUID(dtoRequest.getId()));
            if (optionalStreamers.isEmpty()) {
                throw new BadRequestException("Streamer não registrado na base de dados.", "Falha ao atualizar streamer");
            }
            if (!canUpdate(optionalStreamers.get(), dtoRequest)) {
                throw new BadRequestException("Não pode ser efetuado atualização desse streamer", "Falha ao atualizar Streamer");
            }

            return repository.save(Streamers.builder()
                    .id(optionalStreamers.get().getId())
                    .idPublic(optionalStreamers.get().getIdPublic())
                    .lastModificationDate(DateUtils.localDateTimeToEpoch(LocalDateTime.now()))
                    .twitchName(dtoRequest.getTwitchName())
                    .birthday(dtoRequest.getBirthday())
                    .isInLive(optionalStreamers.get().isInLive())
                    .dateCreate(optionalStreamers.get().getDateCreate())
                    .deleted(optionalStreamers.get().isDeleted())
                    .visible(optionalStreamers.get().isVisible())
                    .build());

        } catch (Exception e) {
            throw new BadRequestException(e.getMessage(), "Falha critica ao atualizar streamer");
        }
    }

    @Transactional
    public Streamers doRegister(StreamersDTORequest requestDTO) {
        try {
            if(!Security.isADM()){
                throw new BadRequestException("Seu usuário não tem privilégios para esta operação", "Falha de segurança");
            }
            if (!canRegister(requestDTO)) {
                throw new BadRequestException("Não pode ser efetuado registro desse streamer", "Falha ao registrar Streamer");
            }
            return repository.save(Streamers.builder()
                    .id(ID.generate())
                    .idPublic(ID.generate())
                    .birthday(requestDTO.getBirthday())
                    .dateCreate(DateUtils.localDateTimeToEpoch(LocalDateTime.now()))
                    .lastModificationDate(0l)
                    .twitchName(requestDTO.getTwitchName())
                    .deleted(false)
                    .visible(true)
                    .isInLive(false)
                    .build());

        } catch (Exception e) {
            throw new BadRequestException(e.getMessage(), "Falha critica ao registrar streamer");
        }
    }

    private boolean canUpdate(Streamers streamers, StreamersDTORequest s) {
        if (!streamers.getIdPublic().equals(ID.toUUID(s.getId()))) {
            throw new BadRequestException("Streamers recebido invalido", "Falha ao atualizar streamer");
        }
        if (StringUtils.isNullOrEmpty(s.getTwitchName())) {
            throw new BadRequestException("Nome da twitch deve ser informado", "Falha ao registrar Streamer");
        }
        if (streamers.getBirthday() == 0l) {
            throw new BadRequestException("Data de nascimento deve ser informada", "Falha ao registrar Streamer");
        }
        if (repository.findByTwitchName(s.getTwitchName()).isPresent() && !s.getTwitchName().equals(streamers.getTwitchName())) {
            throw new BadRequestException("Já existe um Streamer com esse nome", "Falha ao registrar Streamer");
        }
        return true;
    }

    private boolean canRegister(StreamersDTORequest streamers) {
        if (StringUtils.isNullOrEmpty(streamers.getTwitchName())) {
            throw new BadRequestException("Já existe um Streamer com esse nome", "Falha ao registrar Streamer");
        }
        if (streamers.getBirthday() == 0) {
            throw new BadRequestException("Data de nascimento deve ser informada", "Falha ao registrar Streamer");
        }
        if (repository.findByTwitchName(streamers.getTwitchName()).isPresent()) {
            throw new BadRequestException("Já existe um Streamer com esse nome", "Falha ao registrar Streamer");
        }
        return true;
    }

    public Optional<Streamers> getByLogin(String nick) {
        return repository.findByTwitchName(nick);
    }

    public List<Streamers> getAllActive() {
        List<Streamers> ret = new ArrayList<>();
        repository.findAll().forEach(item -> {
            if (item.isVisible() && !item.isDeleted()) {
                ret.add(item);
            }
        });
        ret.sort(Comparator.comparing(Streamers::getTwitchName));
        return  ret;
    }

    @Transactional
    public Streamers delete(String findByTwitchName) {
        if(!Security.isADM()){
            throw new BadRequestException("Seu usuário não tem privilégios para esta operação", "Falha de segurança");
        }
        Optional<Streamers> optional = repository.findByTwitchName(findByTwitchName);
        if (optional.isEmpty()) {
            throw new BadRequestException("Streamer não registrado na base de dados.", "Falha ao deletar streamer");
        }
        Streamers s = optional.get();
        repository.delete(s);
        return s;
    }

    public Optional<Streamers> getByIdPublic(String id){
        return repository.findByIdPublic(ID.toUUID(id));
    }
}
