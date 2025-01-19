package com.UssicCon.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import com.UssicCon.entity.Song;
import com.UssicCon.entity.User;
import com.UssicCon.repo.SongRepository;
import com.UssicCon.repo.UserRepository;
import com.UssicCon.service.UserService;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    private SongRepository songRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void purchaseSong(Long userId, Long songId) {
        User u = userRepository.findById(userId).
                orElseThrow(() -> new NotFoundException("User not found!"));
        u.getPurchasedSongs().add(songRepository.getOne(songId));
        userRepository.save(u);
    }

    @Override
    public List<Song> getPurchasedSong(Long userId) {
        return userRepository.findById(userId).
                orElseThrow(() -> new NotFoundException("User not found!")).
                getPurchasedSongs();
    }
}
