package com.cbt.EquipeAlcance.modules.ponctuation.service;

import com.cbt.EquipeAlcance.infra.errors.exceptions.BadRequestException;
import com.cbt.EquipeAlcance.modules.ponctuation.http.adapters.PonctuationTable;
import com.cbt.EquipeAlcance.modules.ponctuation.http.dto.PonctuationDTORequest;
import com.cbt.EquipeAlcance.modules.ponctuation.http.dto.PonctuationDTOResponse;
import com.cbt.EquipeAlcance.modules.ponctuation.model.Ponctuation;
import com.cbt.EquipeAlcance.modules.ponctuation.repopsitory.PonctuactionRepository;
import com.cbt.EquipeAlcance.modules.streamers.http.dto.StreamersDTORequest;
import com.cbt.EquipeAlcance.modules.streamers.http.dto.StreamersDTOResponse;
import com.cbt.EquipeAlcance.modules.streamers.model.Streamers;
import com.cbt.EquipeAlcance.modules.streamers.service.StreamersServices;
import com.cbt.EquipeAlcance.utils.DateUtils;
import com.cbt.EquipeAlcance.utils.ID;
import com.cbt.EquipeAlcance.utils.Security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class PonctuationService {

    @Autowired
    private PonctuactionRepository repository;
    @Autowired
    private StreamersServices streamersServices;

    private List<Ponctuation> getAllPonctuationByPeriod(long start, long end) {
        long startDay = DateUtils.getEpochStartDayOf(start);
        long endDay = DateUtils.localDateTimeToEpoch(DateUtils.epochToLocalDateTime(DateUtils.getEpochStartDayOf(end)).plusHours(23).plusMinutes(59).plusSeconds(59));
        return repository.findByDateGreaterThanEqualAndDateLessThanEqualOrderByStreamers(startDay, endDay);
    }

    public Ponctuation newPonctuation(PonctuationDTORequest dtoRequest) {
        try {
            if (CanRegister(dtoRequest)) {
                return repository.save(Ponctuation.builder()
                        .id(ID.generate())
                        .idPublic(ID.generate())
                        .visible(true)
                        .deleted(false)
                        .dateCreate(DateUtils.localDateTimeToEpoch(LocalDateTime.now()))
                        .lastModificationDate(0l)
                        .date(DateUtils.localDateTimeToEpoch(LocalDateTime.now()))
                        .score(dtoRequest.getScore())
                        .streamers(getStreamer(dtoRequest.getStreamersDTORequest()))
                        .build());
            } else
                throw new BadRequestException("Não pode ser efetuado registro dessa pontuação", "Falha no registro de Pontuação");
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage(), "Falha critica ao efetuar pontuação");
        }

    }

    private Streamers getStreamer(StreamersDTORequest streamersDTORequest) {
        Optional<Streamers> optional = streamersServices.getByLogin(streamersDTORequest.getTwitchName());
        if (optional.isPresent()) {
            return optional.get();
        }
        throw new BadRequestException("Falhao ao efetuar pontuação: Streamer não foi encontrado.", "Falha no registro de Pontuação");
    }

    private boolean CanRegister(PonctuationDTORequest dtoRequest) {
        if (dtoRequest.getStreamersDTORequest() != null && checkPeriodPonctuaction(dtoRequest))
            return true;
        throw new BadRequestException("Não é possivel realizar pontuação sem um Streamer", "Falha no registro de Pontuação");
    }

    private boolean checkPeriodPonctuaction(PonctuationDTORequest dtoRequest) {
        Optional<Streamers> optional = streamersServices.getByLogin(dtoRequest.getStreamersDTORequest().getTwitchName());
        List<Ponctuation> ponctuationList = repository.findByStreamers(optional.get());
        if (!ponctuationList.isEmpty()) {
            LocalDateTime lastTime = DateUtils.epochToLocalDateTime(ponctuationList.get(ponctuationList.size() - 1).getDate());
            LocalDateTime currentTime = LocalDateTime.now();
            if (DateUtils.localDateTimeToEpoch(lastTime.plusMinutes(9)) > DateUtils.localDateTimeToEpoch(currentTime)) {
                throw new BadRequestException("Você já recolheu a sua pontuação. Por favor nao tente burlar o sistema", "Falha no registrar de Pontuação");
            }
        }
        return true;
    }

    public List<Ponctuation> getAllPonctuactionByPeriod(long start, long end){
        long startDay = DateUtils.getEpochStartDayOf(start);
        long endDay = DateUtils.getEpochEndDayOf(DateUtils.getEpochStartDayOf(DateUtils.getEpochEndDayOf(end)));
        return repository.findByDateGreaterThanEqualAndDateLessThanEqualOrderByStreamers(startDay,endDay);

    }

    public List<Ponctuation> getAllPonctuactionByPeriodStreamer(long start, long end, Streamers streamers){
        long startDay = DateUtils.getEpochStartDayOf(start);
        long endDay = DateUtils.getEpochEndDayOf(DateUtils.getEpochStartDayOf(DateUtils.getEpochEndDayOf(end)));
        return repository.findByDateGreaterThanEqualAndDateLessThanEqualAndStreamersOrderByDateCreate(startDay,endDay,streamers);

    }

    public List<Ponctuation> getAllByStreamer(Streamers str){
        return repository.findByStreamers(str);
    }

    public Ponctuation delete(UUID id){
        if(!Security.isADM()){
            throw new BadRequestException("Seu usuário não tem privilégios para esta operação", "Falha de segurança");
        }
        Optional<Ponctuation> optional = repository.findByIdPublic(id);
        if (optional.isEmpty()) {
            throw new BadRequestException("Streamer não registrado na base de dados.", "Falha ao deletar streamer");
        }
        Ponctuation p = optional.get();
        repository.delete(p);
        return p;
    }
}
