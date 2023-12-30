package com.cbt.CAJsSystem.modules.ponctuation.http.api;

import com.cbt.CAJsSystem.modules.ponctuation.http.adapters.PonctuationTable;
import com.cbt.CAJsSystem.modules.ponctuation.http.dto.PonctuationDTORequest;
import com.cbt.CAJsSystem.modules.ponctuation.http.dto.PonctuationDTOResponse;
import com.cbt.CAJsSystem.modules.ponctuation.model.Ponctuation;
import com.cbt.CAJsSystem.modules.ponctuation.service.PonctuationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/ponctuation")
public class PonctuationController {

    @Autowired
    private PonctuationService ponctuationService;

    @PostMapping("/registerPonctuation")
    public ResponseEntity<PonctuationDTOResponse> setPonctuation(@RequestBody PonctuationDTORequest dtoRequest) {
        return ResponseEntity.ok(PonctuationDTOResponse.toDTO(ponctuationService.newPonctuation(dtoRequest)));
    }

    @GetMapping("/getAllPonctuationByPeriod/{start}/{end}")
    @CrossOrigin("*")
    public ResponseEntity<Set<PonctuationTable>> getAllPonctuationByPeriod(@PathVariable long start, @PathVariable long end) {
        return ResponseEntity.ok(ponctuationService.getListPonctuation(start, end));
    }

    @GetMapping("/getAllPonctuationByPeriodAndUser/{start}/{end}/{id}")
    @CrossOrigin("*")
    public ResponseEntity<Set<PonctuationTable>> getAllPonctuationByPeriodAndId(@PathVariable long start, @PathVariable long end, @PathVariable String id) {
        return ResponseEntity.ok(ponctuationService.getListPonctuationUser(start, end,id));
    }

}
