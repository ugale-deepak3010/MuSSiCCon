package com.UssicCon.service;

import java.util.List;

import com.UssicCon.entity.Song;
import com.UssicCon.entity.User;

public interface UserService {
    User findByEmail(String email);

    User save(User user);

    void purchaseSong(Long userId, Long songId);

    List<Song> getPurchasedSong(Long userId);
}
