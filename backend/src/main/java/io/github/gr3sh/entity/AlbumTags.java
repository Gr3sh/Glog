package io.github.gr3sh.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 对应 AlbumTags 表 - 标签实体类
 */
@Entity
@Table(name = "AlbumTags",
       indexes = {
           @Index(name = "idx_tag_name", columnList = "tag_name"),
           @Index(name = "idx_usage_count", columnList = "usage_count")
       })
public class AlbumTags {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Integer tagId;

    @Column(name = "tag_name", nullable = false, length = 50, unique = true)
    private String tagName;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "usage_count", nullable = false)
    private Integer usageCount = 0;

    @ManyToMany(mappedBy = "tags", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<AlbumPhoto> photos = new HashSet<>();


    public AlbumTags() {
    }

    public AlbumTags(String tagName) {
        this.tagName = tagName;
    }

    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getUsageCount() {
        return usageCount;
    }

    public void setUsageCount(Integer usageCount) {
        this.usageCount = usageCount;
    }
    
    public Set<AlbumPhoto> getPhotos() {
        return photos;
    }

    public void decrementCount() {
        this.usageCount--;
    }

    public void incrementCount() {
        this.usageCount++;
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (usageCount == null) {
            usageCount = 1;
        }
    }
}
