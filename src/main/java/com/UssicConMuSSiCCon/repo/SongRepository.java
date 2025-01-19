package com.UssicConMuSSiCCon.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.UssicConMuSSiCCon.entity.Song;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {

    @Query("SELECT s FROM Song s WHERE CONCAT(s.name, s.artist) LIKE %?1%")
    List<Song> searchSong(String param);
}
