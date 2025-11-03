package io.github.gr3sh.controller;

import io.github.gr3sh.entity.AlbumPhoto;
import io.github.gr3sh.service.AlbumPhotoService;

import java.io.IOException;
import java.util.ArrayList;

import java.util.List;
import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;


@CrossOrigin(origins = "http://localhost:5173") 
@RestController
@RequestMapping("/photos")
public class AlbumPhotoController {

    @Autowired
    private AlbumPhotoService albumPhotoService;

    // 上传照片接口
    @PostMapping("/upload")
    public List<AlbumPhoto> uploadPhotos(
            @RequestParam("photos") List<MultipartFile> files,
            @RequestParam("tags") String tagsJson,
            @RequestParam("date") String date
    ) throws IOException {
    
        // 解析标签
        List<String> tags = new ArrayList<>();
        if (tagsJson != null && !tagsJson.isEmpty()) {
            ObjectMapper mapper = new ObjectMapper();
            tags = mapper.readValue(tagsJson, new TypeReference<List<String>>() {});
        }

        // 保存图片
        List<AlbumPhoto> savedPhotos = new ArrayList<>();
        String uploadDir = "E:/Work/Glog/database/uploads/";
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        for (MultipartFile file : files) {
            File targetFile = new File(uploadDir + file.getOriginalFilename());
            file.transferTo(targetFile);

            AlbumPhoto photo = albumPhotoService.uploadPhoto(
                file.getOriginalFilename(),
                targetFile.getPath(),
                date,
                tags
            );
            savedPhotos.add(photo);
        }

        return savedPhotos;
    }

    // 获取所有照片列表，按日期从新到旧
    @GetMapping("/list")
    public List<AlbumPhoto> getAllPhotos() {
        return albumPhotoService.getPhotosOrderedByDate();
    }

    // 删除照片接口
    @DeleteMapping("/delete/{photoId}")
    public String deletePhoto(@PathVariable Integer photoId) {
        try {
            albumPhotoService.deletePhoto(photoId);
            return "删除成功";
        } catch (Exception e) {
            e.printStackTrace();
            return "删除失败: " + e.getMessage();
        }
    }
}
