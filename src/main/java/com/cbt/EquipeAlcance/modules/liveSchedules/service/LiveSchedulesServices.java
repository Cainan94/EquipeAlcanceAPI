package com.cbt.EquipeAlcance.modules.liveSchedules.service;

import com.cbt.EquipeAlcance.infra.errors.exceptions.BadRequestException;
import com.cbt.EquipeAlcance.modules.liveSchedules.adapters.AvailableHours;
import com.cbt.EquipeAlcance.modules.liveSchedules.adapters.LiveScheduleTable;
import com.cbt.EquipeAlcance.modules.liveSchedules.adapters.StreamersAgendamentoTable;
import com.cbt.EquipeAlcance.modules.liveSchedules.http.dto.LiveSchedulesDTORequest;
import com.cbt.EquipeAlcance.modules.liveSchedules.model.LiveSchedule;
import com.cbt.EquipeAlcance.modules.liveSchedules.repopsitory.LiveSchedulesRepository;
import com.cbt.EquipeAlcance.modules.ponctuation.http.adapters.PonctuationTable;
import com.cbt.EquipeAlcance.modules.ponctuation.service.PonctuationService;
import com.cbt.EquipeAlcance.modules.streamers.model.Streamers;
import com.cbt.EquipeAlcance.modules.streamers.service.StreamersServices;
import com.cbt.EquipeAlcance.utils.DateUtils;
import com.cbt.EquipeAlcance.utils.ID;
import com.cbt.EquipeAlcance.utils.Security;
import com.cbt.EquipeAlcance.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class LiveSchedulesServices {

    @Autowired
    private LiveSchedulesRepository repository;

    private static final boolean userCamMark = true;

    @Autowired
    private StreamersServices streamersServices;

    @Autowired
    private PonctuationService ponctuationService;

    public LiveSchedule doRegisterByUser(LiveSchedulesDTORequest dtoRequest){
        try{
            if(userCamMark){
                return doRegister(dtoRequest);
            }else{
                throw new BadRequestException("Oops, essa função não estã sendo permitida mais", "Falha ao registrar agendamento");
            }
        }catch (Exception e) {
            throw new BadRequestException(e.getMessage(), "Falha critica ao registrar agendamento");
        }
    }

    public LiveSchedule doRegister(LiveSchedulesDTORequest dtoRequest) {
        try {
            if(!Security.isADM() && !userCamMark){
                throw new BadRequestException("Seu usuário não tem privilégios para esta operação", "Falha de segurança");
            }
            if (!canRegister(dtoRequest)) {
                throw new BadRequestException("Não pode ser efetuado registro desse agendamento", "Falha ao registrar agendamento");
            }

            Optional<Streamers> optionalStreamers = streamersServices.getByLogin(dtoRequest.getStreamersDTORequest().getTwitchName());
            if (optionalStreamers.isEmpty()) {
                throw new BadRequestException("não é possivel realizar agendamento de streamer nao registrado.", "Falha ao registrar agendamento");
            }

            return repository.save(LiveSchedule.builder()
                    .id(ID.generate())
                    .idPublic(ID.generate())
                    .visible(true)
                    .deleted(false)
                    .dateCreate(DateUtils.localDateToEpoch(LocalDate.now()))
                    .lastModificationDate(0l)
                    .startTime(dtoRequest.getStartTime())
                    .endTime(dtoRequest.getEndTime())
                    .streamer(optionalStreamers.get())
                    .build());
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage(), "Falha critica ao registrar agendamento");
        }
    }

    public LiveSchedule doUpdate(LiveSchedulesDTORequest dtoRequest) {
        try {
            if(!Security.isADM()){
                throw new BadRequestException("Seu usuário não tem privilégios para esta operação", "Falha de segurança");
            }

            Optional<LiveSchedule> optionalLiveSchedule = repository.findByIdPublic(ID.toUUID(dtoRequest.getId()));
            Optional<Streamers> optionalStreamers = streamersServices.getByLogin(dtoRequest.getStreamersDTORequest().getTwitchName());

            if (optionalLiveSchedule.isEmpty()) {
                throw new BadRequestException("agendamento não registrado na base de dados.", "Falha ao atualizar agendamento");
            }
            if (!canUpdate(optionalLiveSchedule.get(), dtoRequest)) {
                throw new BadRequestException("Não pode ser efetuado atualização desse agendamento", "Falha ao atualizar agendamento");
            }
            if (optionalStreamers.isEmpty()) {
                throw new BadRequestException("não é possivel realizar agendamento de streamer nao registrado.", "Falha ao registrar agendamento");
            }

            return repository.save(LiveSchedule.builder()
                    .id(optionalLiveSchedule.get().getId())
                    .idPublic(optionalLiveSchedule.get().getIdPublic())
                    .visible(dtoRequest.isVisible())
                    .deleted(dtoRequest.isDeleted())
                    .lastModificationDate(DateUtils.localDateTimeToEpoch(LocalDateTime.now()))
                    .dateCreate(optionalLiveSchedule.get().getDateCreate())
                    .startTime(dtoRequest.getStartTime())
                    .endTime(dtoRequest.getEndTime())
                    .streamer(optionalStreamers.get())
                    .build());
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage(), "Falha critica ao atualizar agendamento");
        }
    }

    public List<Streamers> getAllStreamerCanMark(long daySchedule) {
        LocalDateTime start = DateUtils.epochToLocalDateTime(DateUtils.getEpochStartDayOf(daySchedule));
        List<Streamers> cantMark = new ArrayList<>();
        List<Streamers> streamersList = streamersServices.getAllActive();
        cantMark.addAll(getAllStreamersSchedules(DateUtils.localDateTimeToEpoch(start)));
        cantMark.addAll(getAllStreamersSchedules(DateUtils.localDateTimeToEpoch(start.minusHours(24))));
        cantMark.forEach(streamersList::remove);
        return streamersList;
    }

    private List<LiveSchedule> getLiveScheduleByDay(long day, boolean isActive) {
        Long start = DateUtils.getEpochStartDayOf(day);
        long end = DateUtils.localDateTimeToEpoch(DateUtils.epochToLocalDateTime(start).plusHours(23).plusMinutes(59));
        return repository.findByStartTimeGreaterThanEqualAndStartTimeLessThanEqualAndVisibleOrderByStartTime(start, end, isActive);
    }


    public List<Streamers> getAllStreamersSchedules(long daySchedule) {
        List<LiveSchedule> scheduleList = this.getLiveScheduleByDay(daySchedule, true);
        List<Streamers> streamersList = new ArrayList<>();
        streamersServices.getAllActive().forEach(streamer -> {
            List<LiveSchedule> filter = scheduleList.stream().filter(filterStreamer -> filterStreamer.getStreamer().getId().equals(streamer.getId())).toList();
            filter.forEach(item -> streamersList.add(item.getStreamer()));
        });
        return streamersList;
    }

    public LiveSchedule delete(String id) {
        try {
            if(!Security.isADM()){
                throw new BadRequestException("Seu usuário não tem privilégios para esta operação", "Falha de segurança");
            }
            Optional<LiveSchedule> optionalLiveSchedule = repository.findByIdPublic(ID.toUUID(id));
            if (optionalLiveSchedule.isEmpty()) {
                throw new BadRequestException("agendamento não registrado na base de dados.", "Falha ao deletar agendamento");
            }
            repository.delete(optionalLiveSchedule.get());
            return optionalLiveSchedule.get();
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage(), "Falha critica ao deletar agendamento");
        }
    }

    public List<LiveSchedule> getScehduleOfDay(long dayShcedule) {
        return getLiveScheduleByDay(dayShcedule, true);
    }

    public List<LiveScheduleTable> getAllAgendamentos(long start, long end, boolean isActive) {
        List<LiveSchedule> scheduleList = new ArrayList<>();
        List<LiveSchedule> schedules = new ArrayList<>();
        for (long i = start; i < end; i = DateUtils.localDateTimeToEpoch(DateUtils.epochToLocalDateTime(i).plusHours(24).plusMinutes(59).plusSeconds(59))) {
            scheduleList.addAll(this.getLiveScheduleByDay(i, isActive));
        }
        if (scheduleList.isEmpty()) {
            return Collections.emptyList();
        }
        scheduleList.forEach(schedule -> {
            if (schedule.getStartTime() >= start && schedule.getStartTime() < end) {
                schedules.add(schedule);
            }
        });
        if (schedules.isEmpty()) {
            return Collections.emptyList();
        }
        LiveScheduleTable liveScheduleTable = new LiveScheduleTable();
        List<LiveScheduleTable> result = new ArrayList<>();

        long dateSchedule = schedules.get(0).getStartTime();

        liveScheduleTable.setDataAgendamento(DateUtils.localDateToString(DateUtils.epochToLocalDate(dateSchedule)));
        List<StreamersAgendamentoTable> streamersAgendamentoTables = new ArrayList<>();

        for (int i = 0; i < schedules.size(); i++) {
            var item = schedules.get(i);
            if (DateUtils.getEpochStartDayOf(dateSchedule) == DateUtils.getEpochStartDayOf(item.getStartTime())) {
                streamersAgendamentoTables.add(StreamersAgendamentoTable.builder().id(item.getStreamer().getIdPublic().toString()).nome(item.getStreamer().getTwitchName()).horaInicio(DateUtils.epochToLocalDateTime(item.getStartTime()).getHour() + ":" + DateUtils.epochToLocalDateTime(item.getStartTime()).getMinute()).horaFim(DateUtils.epochToLocalDateTime(item.getEndTime()).getHour() + ":" + DateUtils.epochToLocalDateTime(item.getEndTime()).getMinute()).link("https://www.twitch.tv/" + item.getStreamer().getTwitchName()).build());
            } else {
                liveScheduleTable.getStreamers().addAll(streamersAgendamentoTables);
                result.add(liveScheduleTable);
                liveScheduleTable = new LiveScheduleTable();
                dateSchedule = item.getStartTime();
                liveScheduleTable.setDataAgendamento(DateUtils.localDateToString(DateUtils.epochToLocalDate(dateSchedule)));
                streamersAgendamentoTables = new ArrayList<>();
                streamersAgendamentoTables.add(StreamersAgendamentoTable.builder().id(item.getStreamer().getIdPublic().toString()).nome(item.getStreamer().getTwitchName()).horaInicio(DateUtils.epochToLocalDateTime(item.getStartTime()).getHour() + ":" + DateUtils.epochToLocalDateTime(item.getStartTime()).getMinute()).horaFim(DateUtils.epochToLocalDateTime(item.getEndTime()).getHour() + ":" + DateUtils.epochToLocalDateTime(item.getEndTime()).getMinute()).link("https://www.twitch.tv/" + item.getStreamer().getTwitchName()).build());
            }
        }
        liveScheduleTable.getStreamers().addAll(streamersAgendamentoTables);
        result.add(liveScheduleTable);
        return result;
    }

    public List<LiveSchedule> getAllInPeriodVisible(long start, long end, boolean isActive) {
        try {
            List<LiveSchedule> listLives = repository.findByStartTimeGreaterThanEqualAndStartTimeLessThanEqualAndVisibleOrderByStartTime(start, end, isActive);
            List<LiveSchedule> schedules = new ArrayList<>();
            listLives.forEach(item -> {
                if ((item.getStartTime() >= DateUtils.localDateTimeToEpoch(DateUtils.epochToLocalDateTime(start).minusHours(1).minusMinutes(30)) && item.getEndTime() <= DateUtils.localDateTimeToEpoch(DateUtils.epochToLocalDateTime(end).plusHours(1).plusMinutes(30))) && item.isVisible() == isActive) {
                    schedules.add(item);
                }
            });
            return schedules;
        } catch (NumberFormatException e) {
            throw new BadRequestException(e.getMessage(), "Falha critica no servidor: Falha na conversão");
        }
    }

    private boolean canUpdate(LiveSchedule liveSchedule, LiveSchedulesDTORequest dtoRequest) {
        //realizar validações de regra de negocio
        /*  -horario livre?
            -streamer liberado (em caso de banimento temporario)
            -streamer póde agendar? (frequencia de agendamento)
         */
        return true;
    }

    private boolean canRegister(LiveSchedulesDTORequest dtoRequest) {
        if(StringUtils.isNullOrEmpty(dtoRequest.getStreamersDTORequest().getTwitchName())){
            throw new BadRequestException("Para realizar o agendamento é necessario informar o Streamer.", "Falha ao registrar Agendamento");
        }
        Optional<Streamers> streamers = streamersServices.getByLogin(dtoRequest.getStreamersDTORequest().getTwitchName());
        if(streamers.isEmpty()){
            throw new BadRequestException("Streamer não registrado.", "Falha ao consultar Agendamento");
        }
        if (dtoRequest.getStartTime() < 1 || dtoRequest.getEndTime() < 1)
            throw new BadRequestException("Para realizar o agendamento é necessario informar horaio de inicio e fim.", "Falha ao registrar Agendamento");
        if(repository.findByStartTime(dtoRequest.getStartTime()).isPresent()){
            throw new BadRequestException("Horário já está ocupado.", "Falha ao registrar Agendamento");
        }
        if(!hasPonctuaction(streamers.get().getIdPublic().toString(),dtoRequest.getStartTime()) && !Security.isADM()){
            throw new BadRequestException("Streamer não tem pontuação suficiente.", "Falha ao realizar Agendamento");
        }
        if(getAllStreamerCanMark(dtoRequest.getStartTime()).stream().filter(filter->filter.getTwitchName().equals(dtoRequest.getStreamersDTORequest().getTwitchName())).findFirst().isEmpty()){
            throw new BadRequestException("Streamer não pode agendar devido as politicas do grupo", "Falha ao registrar Agendamento");
        }
        //realizar validações de regra de negocio
        /*  -horario livre?
            -streamer liberado (em caso de banimento temporario)
            -streamer póde agendar? (frequencia de agendamento)
         */
        return true;
    }

    private boolean hasPonctuaction(String id, long startTime) {
        long start = DateUtils.getEpochStartDayOf(DateUtils.localDateTimeToEpoch(DateUtils.epochToLocalDateTime(startTime).minusDays(1)));
        long end = DateUtils.getEpochEndDayOf(start);
        Set<PonctuationTable> ponctuactions = ponctuationService.getListPonctuationUser(start,end,id);
        if(ponctuactions.isEmpty()){
            return false;
        }
        int userponctuaction = (int) ponctuactions.stream().toList().get(0).getPontuacaoes().get(0).getScore();
        long totalPonctuaction = repository.countByStartTimeGreaterThanEqualAndStartTimeLessThanEqualAndVisible(start,end,true) * 90;

        if(totalPonctuaction == 0){
            return true;
        }

        long porcent = (userponctuaction * 100L)/ totalPonctuaction;

        if(porcent < 35){
            return false;
        }

        return true;
    }

    public LiveSchedule getLastScheduleUser(String id){
        Optional<Streamers> streamer =  streamersServices.getByIdPublic(id);
        if(streamer.isEmpty()){
            throw new BadRequestException("Streamer não registrado.", "Falha ao consultar Agendamento");
        }
        return repository.findFirstByStreamerOrderByStartTimeDesc(streamer.get()).get();
    }

    public List<AvailableHours> getAvailableHours(long day) {
        List<AvailableHours> allList = new ArrayList<>();
        int id = 0;

        for (int i = 0; i < 24; i++) {
            for (int j = 0; j < 60; j = j + 30) {
                if (i < 10) {
                    if (j < 10) {
                        allList.add(AvailableHours.builder().id(id).value("0" + i + ":" + "0" + j).build());
                    } else {
                        allList.add(AvailableHours.builder().id(id).value("0" + i + ":" + j).build());
                    }
                } else {
                    if (j < 10) {
                        allList.add(AvailableHours.builder().id(id).value(i + ":" + "0" + j).build());
                    } else {
                        allList.add(AvailableHours.builder().id(id).value(i + ":" + j).build());
                    }
                }
                id++;
            }
            id++;
        }

        Long start = DateUtils.localDateTimeToEpoch(DateUtils.epochToLocalDateTime(DateUtils.getEpochStartDayOf(day)).minusMinutes(60));
        long end = DateUtils.localDateTimeToEpoch(DateUtils.epochToLocalDateTime(start).plusHours(23).plusMinutes(59));

        allList.forEach(time -> {
            int hours = Integer.parseInt(time.getValue().split(":")[0]);
            int minutes = Integer.parseInt(time.getValue().split(":")[1]);

        });
        List<AvailableHours> resultFilter = new ArrayList<>();
        repository.findByStartTimeGreaterThanEqualAndStartTimeLessThanEqualAndVisibleOrderByStartTime(start, end, true).forEach(schedule -> {
            LocalDateTime scheduleDateStart = DateUtils.epochToLocalDateTime(schedule.getStartTime()).minusMinutes(90);
            LocalDateTime scheduleDatEnd = DateUtils.epochToLocalDateTime(schedule.getStartTime()).plusMinutes(90);
            allList.forEach(time -> {
                int hora = Integer.parseInt(time.getValue().split(":")[0]);
                int minuto = Integer.parseInt(time.getValue().split(":")[1]);

                if (hora == scheduleDateStart.getHour()) {
                    if (minuto != scheduleDatEnd.getMinute()) resultFilter.add(time);
                }
                if (hora > scheduleDateStart.getHour()){
                    if(DateUtils.localDateTimeToEpoch(scheduleDatEnd) > DateUtils.getEpochEndDayOf(day)){
                        resultFilter.add(time);
                    }else{
                        if(hora < scheduleDatEnd.getHour()) resultFilter.add(time);
                    }
                }
                if(hora == scheduleDatEnd.getHour()){
                    if(minuto != scheduleDatEnd.getMinute()) resultFilter.add(time);
                }


            });
        });
        if (resultFilter.isEmpty()) {
            return allList;
        }
        resultFilter.forEach(allList::remove);
        return allList;
    }
}
