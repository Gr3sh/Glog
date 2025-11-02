package io.github.gr3sh.service;

import io.github.gr3sh.entity.AlbumPhoto;
import io.github.gr3sh.entity.AlbumTags;
import io.github.gr3sh.repository.AlbumPhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Set;
import java.time.LocalDateTime;
import java.util.List;
import java.io.File;

/**
 * 照片服务类
 * 负责：
 *   - 照片上传/删除
 *   - 照片-标签关联
 *   - 获取照片列表/单张照片
 */

@Service
public class AlbumPhotoService {

    @Autowired
    private AlbumPhotoRepository albumPhotoRepository;

    @Autowired
    private AlbumTagsService albumTagsService; 
    
    /**
     * 上传照片并建立标签关联
     */
    public AlbumPhoto uploadPhoto(String originalName, String storagePath, String date, List<String> tags) {
        AlbumPhoto photo = new AlbumPhoto();
        photo.setOriginalName(originalName);
        photo.setStoragePath(storagePath);

        LocalDateTime photoDate;
        try {
            photoDate = LocalDate.parse(date).atStartOfDay();
        } catch (Exception e) {
            photoDate = LocalDateTime.now();
        }
        photo.setPhotoTime(photoDate);
        photo.setUploadTime(LocalDateTime.now());

        AlbumPhoto savedPhoto = albumPhotoRepository.save(photo);

        if (tags != null && !tags.isEmpty()) {
            Set<AlbumTags> tagSet = albumTagsService.getOrCreateTags(tags);
            savedPhoto.setTags(tagSet);
            savedPhoto = albumPhotoRepository.save(savedPhoto); 
        }

        return savedPhoto;
    }

    /**
     * 获取所有照片，按照片时间降序
     */
    public List<AlbumPhoto> getPhotosOrderedByDate() {
        return albumPhotoRepository.findAllByOrderByPhotoTimeDesc();
    }

    /**
     * 获取单张照片
     */
    public AlbumPhoto getPhotoById(Integer photoId) {
        return albumPhotoRepository.findById(photoId).orElse(null);
    }

    /**
     * 删除照片
     * - 删除本地文件
     * - 更新标签使用次数
     * - 删除数据库记录
     */
    public void deletePhoto(Integer photoId) throws Exception {
        AlbumPhoto photo = getPhotoById(photoId);
        if (photo == null) {
            throw new Exception("照片不存在");
        }

        // 删除本地文件
        File file = new File(photo.getStoragePath());
        if (file.exists() && !file.delete()) {
            throw new Exception("文件删除失败: " + photo.getStoragePath());
        }
        
        // 处理标签：照片关联的每个标签引用数减1，如果为0就删除标签
        if (photo.getTags() != null && !photo.getTags().isEmpty()) {
            albumTagsService.decreaseUsage(photo.getTags());
        }

        // 删除数据库记录
        albumPhotoRepository.deleteById(photoId);
    }
}
