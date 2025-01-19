package com.UssicConMuSSiCCon.service;

import java.util.List;

import com.UssicConMuSSiCCon.entity.Song;
import com.UssicConMuSSiCCon.entity.User;

public interface UserService {
    User findByEmail(String email);

    User save(User user);

    void purchaseSong(Long userId, Long songId);

    List<Song> getPurchasedSong(Long userId);
}
