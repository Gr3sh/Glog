package io.github.gr3sh.service;

import io.github.gr3sh.entity.AlbumTags;
import io.github.gr3sh.repository.AlbumTagsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Optional;

/**
 * 标签服务类
 * 负责：
 *   - 标签的增查
 *   - 批量处理标签
 *   - 标签使用次数管理（增加/减少）
 */

@Service
public class AlbumTagsService {

    @Autowired
    private AlbumTagsRepository albumTagsRepository;

    /**
     * 根据标签名获取标签，如果不存在则创建新的
     */
    public AlbumTags getOrCreateTag(String tagName) {
        String trimmed = tagName.trim();
        Optional<AlbumTags> existingTag = albumTagsRepository.findByTagName(trimmed);
        if (existingTag.isPresent()) {
            AlbumTags tag = existingTag.get();
            tag.incrementCount(); 
            return albumTagsRepository.save(tag);
        } else {
            AlbumTags newTag = new AlbumTags(trimmed);
            newTag.setUsageCount(1);
            return albumTagsRepository.save(newTag);
        }
    }

    /**
     * 批量处理标签列表（比如前端传来多个标签）
     */
    public Set<AlbumTags> getOrCreateTags(List<String> tagNames) {
        Set<AlbumTags> tags = new HashSet<>();
        if (tagNames != null) {
            for (String tagName : tagNames) {
                if (tagName != null && !tagName.isBlank()) {
                    tags.add(getOrCreateTag(tagName));
                }
            }
        }
        return tags;
    }

    /**
     * 标签使用次数减少（可选，用于删除照片时）
     */
    public void decreaseUsage(Set<AlbumTags> tags) {
        for (AlbumTags tag : tags) {
            if (tag.getUsageCount() > 1) {
                tag.decrementCount();
                albumTagsRepository.save(tag);
            } else {
                albumTagsRepository.delete(tag);
            }
        }
    }

    public List<AlbumTags> getAllTagsSortedByUsage() {
        return albumTagsRepository.findAllByOrderByUsageCountDesc();
    }
}
