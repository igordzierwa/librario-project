package com.agh.librario.Picture;

import com.agh.librario.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@CrossOrigin
@RestController
@Slf4j

public class PictureController {

    @Autowired
    private PictureService pictureService;

    @Autowired
    private final PictureRepository repo;

    PictureController(PictureRepository repo, PictureService ps){
        this.repo = repo;
        this.pictureService = ps;
    }

    @GetMapping("/rest/picture")
    List<Picture> all(){
        return repo.findAll();
    }


    @PostMapping("/rest/picture")
    public  HashMap<String, String>  handleFileUpload(@RequestParam("file") MultipartFile file) {
        String name = pictureService.store(file, "picture");
        Path fullPath = pictureService.load(name);
        log.info("successfully uploaded " + file.getOriginalFilename() + " as: " + name + "!");

        HashMap<String, String> map = new HashMap<>();
        map.put("code", "200");
        map.put("name", name);
        map.put("message", "Added successfully");
        return map;
    }

}
