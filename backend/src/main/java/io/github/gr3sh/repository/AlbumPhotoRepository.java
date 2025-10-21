package io.github.gr3sh.repository;

import io.github.gr3sh.entity.AlbumPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlbumPhotoRepository extends JpaRepository<AlbumPhoto, Integer> {
    List<AlbumPhoto> findAllByOrderByPhotoTimeDesc();
}
