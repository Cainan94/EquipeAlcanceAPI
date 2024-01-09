package com.cbt.EquipeAlcance.modules.liveSchedules.http.api;

import com.cbt.EquipeAlcance.modules.liveSchedules.adapters.AvailableHours;
import com.cbt.EquipeAlcance.modules.liveSchedules.adapters.LiveScheduleTable;
import com.cbt.EquipeAlcance.modules.liveSchedules.http.dto.LiveSchedulesDTORequest;
import com.cbt.EquipeAlcance.modules.liveSchedules.http.dto.LiveSchedulesDTOResponse;
import com.cbt.EquipeAlcance.modules.liveSchedules.model.LiveSchedule;
import com.cbt.EquipeAlcance.modules.liveSchedules.service.LiveSchedulesServices;
import com.cbt.EquipeAlcance.modules.streamers.http.dto.StreamersDTOResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/liveSchedules")
public class LiveSchedulesController {

    @Autowired
    private LiveSchedulesServices services;

    @PostMapping(value = "/register")
    @CrossOrigin("*")
    public ResponseEntity<LiveSchedulesDTOResponse> registerLiveSchedule(@RequestBody LiveSchedulesDTORequest request) {
        return ResponseEntity.ok(LiveSchedulesDTOResponse.toDTO(services.doRegister(request)));
    }

    @PostMapping(value = "/update")
    @CrossOrigin("*")
    public ResponseEntity<LiveSchedulesDTOResponse> updateLiveSchedule(@RequestBody LiveSchedulesDTORequest request) {
        return ResponseEntity.ok(LiveSchedulesDTOResponse.toDTO(services.doUpdate(request)));
    }

    @GetMapping(value = "/delete/{id}")
    @CrossOrigin("*")
    public ResponseEntity<LiveSchedulesDTOResponse> deleteSchedule(@PathVariable String id) {
        return ResponseEntity.ok(LiveSchedulesDTOResponse.toDTO(services.delete(id)));
    }

    @GetMapping(value = "/AllPeriodActive/{startTime}/{endTime}")
    @CrossOrigin("*")
    public ResponseEntity<List<LiveSchedulesDTOResponse>> getAllByPeriodActive(@PathVariable long startTime, @PathVariable long endTime) {
        return ResponseEntity.ok(services.getAllInPeriodVisible(startTime, endTime, true).stream().map(LiveSchedulesDTOResponse::toDTO).collect(Collectors.toList()));
    }

    @GetMapping(value = "/AllPeriodInactive/{startTime}/{endTime}")
    @CrossOrigin("*")
    public ResponseEntity<List<LiveSchedulesDTOResponse>> getAllByPeriodInactive(@PathVariable long startTime, @PathVariable long endTime) {
        return ResponseEntity.ok(services.getAllInPeriodVisible(startTime, endTime, false).stream().map(LiveSchedulesDTOResponse::toDTO).collect(Collectors.toList()));
    }
    @GetMapping(value = "/AllAgendamentosActive/{startTime}/{endTime}")
    @CrossOrigin("*")
    public ResponseEntity<List<LiveScheduleTable>> getAllAgendamentosPeriodActive(@PathVariable long startTime, @PathVariable long endTime) {
        return ResponseEntity.ok(services.getAllAgendamentos(startTime, endTime, true));
    }

    @GetMapping(value = "/AllAgendamentosInactive/{startTime}/{endTime}")
    @CrossOrigin("*")
    public ResponseEntity<List<LiveScheduleTable>> getAllAgendamentosPeriodInactive(@PathVariable long startTime, @PathVariable long endTime) {
        return ResponseEntity.ok(services.getAllAgendamentos(startTime, endTime, false));
    }

    @GetMapping(value = "/AllScheduleOfDay/{daySchedule}")
    @CrossOrigin("*")
    public ResponseEntity<List<LiveSchedulesDTOResponse>>getAllSchdeuleOfDay(@PathVariable long daySchedule){
        List<LiveSchedule> list = services.getScehduleOfDay(daySchedule);
        if(list.size() > 0){
            return ResponseEntity.ok(list.stream().map(LiveSchedulesDTOResponse::toDTO).collect(Collectors.toList()));
        }
        return ResponseEntity.ok(Collections.emptyList());
    }

    @GetMapping(value = "/getStreamersCamDoLive/{daySchedule}")
    @CrossOrigin("*")
    public ResponseEntity<List<StreamersDTOResponse>> getStreamersCamDoLive(@PathVariable Long daySchedule){
        return ResponseEntity.ok(services.getAllStreamerCanMark(daySchedule).stream().map(StreamersDTOResponse::toDTO).collect(Collectors.toList()));
    }

    @GetMapping(value = "/getAllStreamersSchedules/{daySchedule}")
    @CrossOrigin("*")
    public ResponseEntity<List<StreamersDTOResponse>> getAllStreamersSchedules(@PathVariable Long daySchedule){
        return ResponseEntity.ok(services.getAllStreamersSchedules(daySchedule).stream().map(StreamersDTOResponse::toDTO).collect(Collectors.toList()));
    }

    @GetMapping(value = "/getAvailableHours/{daySchedule}")
    @CrossOrigin("*")
    public ResponseEntity<List<AvailableHours>> getAvailableHours(@PathVariable Long daySchedule){
        return ResponseEntity.ok(services.getAvailableHours(daySchedule));
    }

    @PostMapping(value = "/registerByUser")
    @CrossOrigin("*")
    public ResponseEntity<LiveSchedulesDTOResponse> registerLiveScheduleByUser(@RequestBody LiveSchedulesDTORequest request) {
        return ResponseEntity.ok(LiveSchedulesDTOResponse.toDTO(services.doRegisterByUser(request)));
    }

    @GetMapping(value = "/getLastScheduleUser/{id}")
    @CrossOrigin("*")
    public ResponseEntity<LiveSchedulesDTOResponse> getLastScheduleUser(@PathVariable String id){
        return ResponseEntity.ok(LiveSchedulesDTOResponse.toDTO(services.getLastScheduleUser(id)));
    }


}
