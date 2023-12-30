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
                throw new BadRequestException("Você já recolheu a sua pontuação. Por faver nao tente burlar o sistema", "Falha no registrar de Pontuação");
            }
        }
        return true;
    }

    public Set<PonctuationTable> getListPonctuationUser(long start, long end,String id) {
        Optional<Streamers> streamersOptional = streamersServices.getByIdPublic(id);
        if(streamersOptional.isEmpty()){
            throw new BadRequestException("Usuário não registrado no sistema","Falha ao consultar pontuação");
        }

        long startDay = DateUtils.getEpochStartDayOf(start);
        long endDay = DateUtils.localDateTimeToEpoch(DateUtils.epochToLocalDateTime(DateUtils.getEpochStartDayOf(end)).plusHours(23).plusMinutes(59).plusSeconds(59));
        List<Ponctuation> ponctuationList = new ArrayList<>();
        ponctuationList.addAll(repository.findByDateGreaterThanEqualAndDateLessThanEqualAndStreamersOrderByDateCreate(startDay,endDay,streamersOptional.get()));
        if(ponctuationList.isEmpty()){
            return new HashSet<>();
        }
        Set<PonctuationTable> result = new HashSet<>();
        Set<PonctuationDTOResponse> ponctuationDTOResponses = new HashSet<>();
        PonctuationTable ponctuationTable = new PonctuationTable();

        long date = ponctuationList.get(0).getDate();
        Ponctuation p = ponctuationList.get(0);
        ponctuationTable.setDate(DateUtils.localDateToString(DateUtils.epochToLocalDate(date)));
        float total = 0f;

        for (Ponctuation ponctuation : ponctuationList) {
            if(DateUtils.getEpochStartDayOf(date)==DateUtils.getEpochStartDayOf(ponctuation.getDate())){

                if(p.getStreamers().getTwitchName().equals(ponctuation.getStreamers().getTwitchName())){

                    total+=ponctuation.getScore();

                }else{

                    ponctuationDTOResponses.add(PonctuationDTOResponse.builder()
                            .score(total)
                            .streamer(StreamersDTOResponse.toDTO(p.getStreamers()))
                            .build());
                    p = ponctuation;
                    total = 0f;
                    total+=ponctuation.getScore();
                }

            }else{
                ponctuationDTOResponses.add(PonctuationDTOResponse.builder()
                        .score(total)
                        .streamer(StreamersDTOResponse.toDTO(p.getStreamers()))
                        .build());
                total = 0f;
                ponctuationTable.getPontuacaoes().addAll(ponctuationDTOResponses);
                ponctuationDTOResponses = new HashSet<>();
                result.add(ponctuationTable);
                ponctuationTable = new PonctuationTable();
                date = ponctuation.getDate();
                ponctuationTable.setDate(DateUtils.localDateToString(DateUtils.epochToLocalDate(date)));

                if(p.getStreamers().getTwitchName().equals(ponctuation.getStreamers().getTwitchName())){

                    total+=ponctuation.getScore();

                }else{

                    ponctuationDTOResponses.add(PonctuationDTOResponse.builder()
                            .score(total)
                            .streamer(StreamersDTOResponse.toDTO(p.getStreamers()))
                            .build());
                    p = ponctuation;
                    total = 0f;
                    total+=ponctuation.getScore();
                }
            }
        }
        ponctuationDTOResponses.add(PonctuationDTOResponse.builder()
                .score(total)
                .streamer(StreamersDTOResponse.toDTO(p.getStreamers()))
                .build());
        total = 0f;
        ponctuationTable.getPontuacaoes().addAll(ponctuationDTOResponses);
        result.add(ponctuationTable);
        return result;
    }
    public Set<PonctuationTable> getListPonctuation(long start, long end) {
        long startDay = DateUtils.getEpochStartDayOf(start);
        long endDay = DateUtils.localDateTimeToEpoch(DateUtils.epochToLocalDateTime(DateUtils.getEpochStartDayOf(end)).plusHours(23).plusMinutes(59).plusSeconds(59));

        List<Ponctuation> ponctuationList = new ArrayList<>();
        for (Streamers streamers : streamersServices.getAllActive()) {
            ponctuationList.addAll(repository.findByDateGreaterThanEqualAndDateLessThanEqualAndStreamers(startDay,endDay,streamers));
        }
        if(ponctuationList.isEmpty()){
            return new HashSet<>();
        }
        Set<PonctuationTable> result = new HashSet<>();
        Set<PonctuationDTOResponse> ponctuationDTOResponses = new HashSet<>();
        PonctuationTable ponctuationTable = new PonctuationTable();

        long date = ponctuationList.get(0).getDate();
        Ponctuation p = ponctuationList.get(0);
        ponctuationTable.setDate(DateUtils.localDateToString(DateUtils.epochToLocalDate(date)));
        float total = 0f;

        for (Ponctuation ponctuation : ponctuationList) {
            if(DateUtils.getEpochStartDayOf(date)==DateUtils.getEpochStartDayOf(ponctuation.getDate())){

                if(p.getStreamers().getTwitchName().equals(ponctuation.getStreamers().getTwitchName())){

                    total+=ponctuation.getScore();

                }else{

                    ponctuationDTOResponses.add(PonctuationDTOResponse.builder()
                            .score(total)
                            .streamer(StreamersDTOResponse.toDTO(p.getStreamers()))
                            .build());
                    p = ponctuation;
                    total = 0f;
                    total+=ponctuation.getScore();
                }

            }else{
                ponctuationDTOResponses.add(PonctuationDTOResponse.builder()
                        .score(total)
                        .streamer(StreamersDTOResponse.toDTO(p.getStreamers()))
                        .build());
                total = 0f;
                ponctuationTable.getPontuacaoes().addAll(ponctuationDTOResponses);
                ponctuationDTOResponses = new HashSet<>();
                result.add(ponctuationTable);
                ponctuationTable = new PonctuationTable();
                date = ponctuation.getDate();
                ponctuationTable.setDate(DateUtils.localDateToString(DateUtils.epochToLocalDate(date)));

                if(p.getStreamers().getTwitchName().equals(ponctuation.getStreamers().getTwitchName())){

                    total+=ponctuation.getScore();

                }else{

                    ponctuationDTOResponses.add(PonctuationDTOResponse.builder()
                            .score(total)
                            .streamer(StreamersDTOResponse.toDTO(p.getStreamers()))
                            .build());
                    p = ponctuation;
                    total = 0f;
                    total+=ponctuation.getScore();
                }
            }
        }
        ponctuationDTOResponses.add(PonctuationDTOResponse.builder()
                .score(total)
                .streamer(StreamersDTOResponse.toDTO(p.getStreamers()))
                .build());
        total = 0f;
        ponctuationTable.getPontuacaoes().addAll(ponctuationDTOResponses);
        result.add(ponctuationTable);
        return result;
    }
}
