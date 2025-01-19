package com.UssicConMuSSiCCon.service;

import java.util.List;

import com.UssicConMuSSiCCon.entity.Song;

public interface SongService {

    Song getById(Long id);

    List<Song> getAll();

    void addSong(Song song);

    List<Song> searchSong(String param);

    void deleteSong(Long songId);
}
