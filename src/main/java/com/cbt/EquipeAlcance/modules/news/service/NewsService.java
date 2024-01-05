package com.cbt.EquipeAlcance.modules.news.service;

import com.cbt.EquipeAlcance.infra.errors.exceptions.BadRequestException;
import com.cbt.EquipeAlcance.modules.news.http.adapters.NewsDTORequest;
import com.cbt.EquipeAlcance.modules.news.model.NewsModel;
import com.cbt.EquipeAlcance.modules.news.repository.NewsRepository;
import com.cbt.EquipeAlcance.utils.DateUtils;
import com.cbt.EquipeAlcance.utils.ID;
import com.cbt.EquipeAlcance.utils.Security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class NewsService {
    @Autowired
    private NewsRepository repository;

    public NewsModel doRegister(NewsDTORequest request) {
        if (!Security.isADM()) {
            throw new BadRequestException("Seu usuário não tem privilégios para esta operação", "Falha de segurança");
        }
        if (canRegister(request)) {
            return repository.save(NewsModel.builder()
                    .id(ID.generate())
                    .idPublic(ID.generate())
                    .visible(true)
                    .deleted(false)
                    .dateCreate(DateUtils.localDateTimeToEpoch(LocalDateTime.now()))
                    .lastModificationDate(0l)
                    .title(request.getTitle())
                    .description(request.getDesc())
                    .link(request.getLink())
                    .image(request.getImage())
                    .build());
        }
        throw new BadRequestException("Falha ao publicar a noticia", "Falha critica");
    }

    public NewsModel doUpdate(NewsDTORequest request) {
        if (!Security.isADM()) {
            throw new BadRequestException("Seu usuário não tem privilégios para esta operação", "Falha de segurança");
        }
        if(canUpdate(request)){
            NewsModel newsModel = repository.findByIdPublic(ID.toUUID(request.getId())).get();
            return repository.save(NewsModel.builder()
                    .id(newsModel.getId())
                    .idPublic(newsModel.getIdPublic())
                    .visible(true)
                    .deleted(false)
                    .dateCreate(newsModel.getDateCreate())
                    .lastModificationDate(DateUtils.localDateTimeToEpoch(LocalDateTime.now()))
                    .title(request.getTitle())
                    .description(request.getDesc())
                    .link(request.getLink())
                    .image(request.getImage())
                    .build());

        }
        throw new BadRequestException("Falha ao atualizar a noticia", "Falha critica");
    }

    public NewsModel doDelete(NewsDTORequest request) {
        if (!Security.isADM()) {
            throw new BadRequestException("Seu usuário não tem privilégios para esta operação", "Falha de segurança");
        }
        if (canDelete(request)) {
            NewsModel newsModel = repository.findByIdPublic(ID.toUUID(request.getId())).get();
            repository.delete(newsModel);
            return newsModel;
        }
        throw new BadRequestException("Falha ao deletar a noticia", "Falha critica");
    }

    public List<NewsModel> getAllNews(){
            List<NewsModel> newsModels = repository.findAll();
        Collections.sort(newsModels, Comparator.comparingLong(NewsModel::getDateCreate));
        return newsModels;

    }

    public NewsModel getByTitle(String title){
        Optional<NewsModel> optional =  repository.findByTitle(title);
        if(optional.isEmpty()){
            throw new BadRequestException("Não foi possivel encontrar noticia pelo titulo","Falha ao consutar noticia");
        }
        return optional.get();
    }

    private boolean canDelete(NewsDTORequest request) {
        if(request.getId().trim().isEmpty()){
            throw new BadRequestException("Imporssivel encontrar noticia sem identificador", "Falha ao deletar noticia");
        }
        Optional<NewsModel> optional = repository.findByIdPublic(ID.toUUID(request.getId()));
        if (optional.isEmpty()){
            throw new BadRequestException("Noticia não encontrada na base de dados", "Falha ao deletar noticia");
        }
        if(!request.getTitle().equals(optional.get().getTitle())){
            throw new BadRequestException("Noticia informada difere da noticia registrada", "Falha ao deletar noticia");
        }
        return true;
    }

    private boolean canUpdate(NewsDTORequest request) {
        if(request.getId().trim().isEmpty()){
            throw new BadRequestException("Imporssivel encontrar noticia sem identificador", "Falha ao atualizar noticia");
        }
        if (request.getDesc().trim().isEmpty() || request.getTitle().trim().isEmpty()) {
            throw new BadRequestException("O titulo e  a descrição da noticia deve ser informado.", "Falha ao atualizar noticia");
        }
        Optional<NewsModel> optional = repository.findByIdPublic(ID.toUUID(request.getId()));
        if (optional.isEmpty()){
            throw new BadRequestException("Noticia não encontrada na base de dados", "Falha ao atualizar noticia");
        }
        return true;
    }

    private boolean canRegister(NewsDTORequest request) {
        if (request.getDesc().trim().isEmpty() || request.getTitle().trim().isEmpty()) {
            throw new BadRequestException("O titulo e  a descrição da noticia deve ser informado.", "Falha ao publicar noticia");
        }
        return true;
    }


}
