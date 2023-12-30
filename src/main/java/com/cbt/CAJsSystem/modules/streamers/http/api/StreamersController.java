package com.cbt.CAJsSystem.modules.streamers.http.api;

import com.cbt.CAJsSystem.modules.liveSchedules.service.LiveSchedulesServices;
import com.cbt.CAJsSystem.modules.streamers.http.dto.StreamersDTOResponse;
import com.cbt.CAJsSystem.modules.streamers.service.StreamersServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.stream.Location;
import java.net.ResponseCache;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/streamers")
public class StreamersController {

    @Autowired
    private StreamersServices services;

    @GetMapping(value = "/getAllActive")
    @CrossOrigin("*")
    public ResponseEntity<List<StreamersDTOResponse>> getAllStreamersAtivos(){

        var list = services.getAllActive();
        List<StreamersDTOResponse> result = new ArrayList<>();
            list.forEach(item->{
            result.add(StreamersDTOResponse.toDTO(item));
        });
        return ResponseEntity.ok(result);
    }
}
