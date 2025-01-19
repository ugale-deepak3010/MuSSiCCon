package com.UssicCon.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import com.UssicCon.entity.Song;
import com.UssicCon.repo.SongRepository;
import com.UssicCon.service.SongService;
import com.UssicCon.utils.FileUtils;

import java.util.List;

@Service
public class SongServiceImpl implements SongService {

    @Autowired
    SongRepository songRepository;

    @Override
    public void addSong(Song song) {
        songRepository.save(song);
    }

    @Override
    public Song getById(Long id) {
        return songRepository.findById(id).
                orElseThrow(() -> new NotFoundException("Song not found!"));
    }

    @Override
    public List<Song> getAll() {
    	
    	System.err.println("Getting songs from repo");
    	try {
    		songRepository.findAll();
		} catch (Exception e) {
			System.out.println("Here is the error: ");
		}
    	
        return songRepository.findAll();
    }

    @Override
    public List<Song> searchSong(String param) {
        return songRepository.searchSong(param);
    }

    @Override
    public void deleteSong(Long songId) {
        Song song = songRepository.findById(songId).
                orElseThrow(() -> new NotFoundException("Song not found!"));
        String songLink = song.getSongLink();
        String imgLink = song.getImgLink();
        FileUtils.deleteFile(songLink);
        FileUtils.deleteFile(imgLink);
        songRepository.delete(song);
    }
}
