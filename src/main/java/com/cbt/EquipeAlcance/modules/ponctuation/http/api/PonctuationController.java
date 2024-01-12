package com.cbt.EquipeAlcance.modules.ponctuation.http.api;

import com.cbt.EquipeAlcance.modules.ponctuation.http.adapters.PonctuationTable;
import com.cbt.EquipeAlcance.modules.ponctuation.http.dto.PonctuationDTORequest;
import com.cbt.EquipeAlcance.modules.ponctuation.http.dto.PonctuationDTOResponse;
import com.cbt.EquipeAlcance.modules.ponctuation.service.PonctuationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("api/v1/ponctuation")
public class PonctuationController {

    @Autowired
    private PonctuationService ponctuationService;

    @PostMapping("/registerPonctuation")
        public ResponseEntity<PonctuationDTOResponse> setPonctuation(@RequestBody PonctuationDTORequest dtoRequest) {
        return ResponseEntity.ok(PonctuationDTOResponse.toDTO(ponctuationService.newPonctuation(dtoRequest)));
    }
}
