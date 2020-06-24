package com.amsidh.mvc.ui.controllers;

import com.amsidh.mvc.data.AlbumEntity;
import com.amsidh.mvc.service.AlbumsService;
import com.amsidh.mvc.ui.model.AlbumResponseModel;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

@RestController
@RequestMapping("/users/{id}/albums")
public class AlbumsController {
    
    @Autowired
    AlbumsService albumsService;
    Logger logger = getLogger(this.getClass());
    @GetMapping( 
            produces = { 
                APPLICATION_JSON_VALUE,
                APPLICATION_XML_VALUE,
            })
    public List<AlbumResponseModel> userAlbums(@PathVariable String id) {

        List<AlbumResponseModel> returnValue = new ArrayList<>();
        
        List<AlbumEntity> albumsEntities = albumsService.getAlbums(id);
        
        if(albumsEntities == null || albumsEntities.isEmpty())
        {
            return returnValue;
        }
        
        Type listType = new TypeToken<List<AlbumResponseModel>>(){}.getType();
 
        returnValue = new ModelMapper().map(albumsEntities, listType);
        logger.info("Returning " + returnValue.size() + " albums");
        return returnValue;
    }
}
