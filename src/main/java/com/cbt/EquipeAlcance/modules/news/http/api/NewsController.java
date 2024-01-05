package com.cbt.EquipeAlcance.modules.news.http.api;

import com.cbt.EquipeAlcance.modules.news.http.adapters.NewsDTORequest;
import com.cbt.EquipeAlcance.modules.news.http.adapters.NewsDTOResponse;
import com.cbt.EquipeAlcance.modules.news.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/news")
public class NewsController {

    @Autowired
    private NewsService service;

    @PostMapping(value = "/registerNews")
    @CrossOrigin("*")
    public ResponseEntity<NewsDTOResponse> registerNews(@RequestBody NewsDTORequest request){
        return ResponseEntity.ok(NewsDTOResponse.toDto(service.doRegister(request)));
    }
    @PostMapping(value = "/updateNews")
    @CrossOrigin("*")
    public ResponseEntity<NewsDTOResponse> updateNews(@RequestBody NewsDTORequest request){
        return ResponseEntity.ok(NewsDTOResponse.toDto(service.doUpdate(request)));
    }
    @PostMapping(value = "/deleteNews")
    @CrossOrigin("*")
    public ResponseEntity<NewsDTOResponse> deleteNews(@RequestBody NewsDTORequest request){
        return ResponseEntity.ok(NewsDTOResponse.toDto(service.doDelete(request)));
    }
    @GetMapping(value = "/getAllNews")
    @CrossOrigin("*")
    public ResponseEntity<List<NewsDTOResponse>> getAllNews(){
        return ResponseEntity.ok(service.getAllNews().stream().map(NewsDTOResponse::toDto).collect(Collectors.toList()));
    }
    @PostMapping(value = "/getByTitle")
    @CrossOrigin("*")
    public ResponseEntity<NewsDTOResponse> getByTitle(@RequestBody NewsDTORequest request){
        return ResponseEntity.ok(NewsDTOResponse.toDto(service.getByTitle(request.getTitle())));
    }

}
