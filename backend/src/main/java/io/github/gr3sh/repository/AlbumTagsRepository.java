package io.github.gr3sh.repository;

import io.github.gr3sh.entity.AlbumTags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface AlbumTagsRepository extends JpaRepository<AlbumTags, Integer> {
    Optional<AlbumTags> findByTagName(String tagName);
    List<AlbumTags> findAllByOrderByUsageCountDesc();
}
