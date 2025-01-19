package com.UssicConMuSSiCCon.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.UssicConMuSSiCCon.entity.Song;
import com.UssicConMuSSiCCon.entity.User;
import com.UssicConMuSSiCCon.service.SongService;
import com.UssicConMuSSiCCon.service.UserService;

import java.util.List;

@Controller
@Log4j2
public class HomeController {

	@Autowired
	SongService songService;

	@Autowired
	UserService userService;

	@Secured("ROLE_USER")
	@GetMapping({ "/", "/home" })
	public String retrieveFormLoginInfo(Model model, @AuthenticationPrincipal User user) {
		
		System.out.println("---------Home Controller 1----------");
		
		
		// In form-based login flow you get UserDetails as principal while in Oauth
		// based flow you get Oauth2User
		log.info("user.getUsername() => " + user.getUsername());
		log.info("user.getEmail() => " + user.getEmail());
		log.info("user.getAuthProvider() => " + user.getAuthProvider());
		log.info("user.getProviderId() => " + user.getProviderId());

		List<Song> songs = songService.getAll();
		
		System.err.println("song list: "+songs);
		
		List<Song> purchasedSongs = userService.getPurchasedSong(user.getId());
		
		model.addAttribute("purchasedSongs", purchasedSongs);
		model.addAttribute("songs", songs);
		model.addAttribute("name", user.getName());
		System.out.println("---------Home Controller 2----------");
		return "home";
	}
}
