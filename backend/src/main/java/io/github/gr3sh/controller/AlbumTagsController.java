package io.github.gr3sh.controller;

import io.github.gr3sh.entity.AlbumTags;
import io.github.gr3sh.service.AlbumTagsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173") 
@RestController
@RequestMapping("/tags")
public class AlbumTagsController {

    @Autowired
    private AlbumTagsService albumTagsService;

    @GetMapping
    public List<AlbumTags> getAllTags() {
        return albumTagsService.getAllTagsSortedByUsage();
    }
}
