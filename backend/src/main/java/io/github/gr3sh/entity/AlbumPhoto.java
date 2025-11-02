package io.github.gr3sh.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * 对应 album_photos 表 - 照片主表实体
 */
@Entity
@Table(name = "album_photos", 
       indexes = @Index(name = "idx_upload_time", columnList = "upload_time"))
public class AlbumPhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "photo_id")
    private Integer photoId;

    @Column(name = "original_name", nullable = false, length = 255)
    private String originalName;

    @Column(name = "storage_path", nullable = false, length = 512)
    private String storagePath;

    @Column(name = "photo_time", nullable = false, updatable = false)
    private LocalDateTime photoTime;

    @Column(name = "upload_time", nullable = false, updatable = false)
    private LocalDateTime uploadTime;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "album_photo_tags",
        joinColumns = @JoinColumn(name = "photo_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<AlbumTags> tags = new HashSet<>();

    public AlbumPhoto() {
    }

    public Integer getPhotoId() {
        return photoId;
    }

    public void setPhotoId(Integer photoId) {
        this.photoId = photoId;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getStoragePath() {
        return storagePath;
    }

    public void setStoragePath(String storagePath) {
        this.storagePath = storagePath;
    }

    public LocalDateTime getPhotoTime() {
        return photoTime;
    }

    public void setPhotoTime(LocalDateTime photoTime) {
        this.photoTime = photoTime;
    }

    public LocalDateTime getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(LocalDateTime uploadTime) {
        this.uploadTime = uploadTime;
    }

    public Set<AlbumTags> getTags() {
        return tags;
    }

    public void setTags(Set<AlbumTags> tags) {
        this.tags = tags;
    }

    @PrePersist
    protected void onCreate() {
        if (uploadTime == null) {
            uploadTime = LocalDateTime.now();
        }
    }
}
