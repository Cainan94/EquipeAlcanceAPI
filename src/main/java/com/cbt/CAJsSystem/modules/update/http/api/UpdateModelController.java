package com.cbt.CAJsSystem.modules.update.http.api;

import com.cbt.CAJsSystem.modules.update.http.dto.UpdateModelDTORequest;
import com.cbt.CAJsSystem.modules.update.http.dto.UpdateModelDTOResponse;
import com.cbt.CAJsSystem.modules.update.model.UpdateModel;
import com.cbt.CAJsSystem.modules.update.service.UpdateModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/update")
public class UpdateModelController {

    @Autowired
    private UpdateModelService service;

    @GetMapping()
    @CrossOrigin("*")
    public ResponseEntity<UpdateModelDTOResponse> getVersion(){
        return ResponseEntity.ok(UpdateModelDTOResponse.toDTO(service.getCurrentVersion()));
    }

    @PostMapping()
    @CrossOrigin("*")
    public ResponseEntity<UpdateModelDTOResponse> setNewVersion( @RequestBody UpdateModelDTORequest request){
        return ResponseEntity.ok(UpdateModelDTOResponse.toDTO(service.insertVersion(request)));
    }
}
