package com.UssicConMuSSiCCon.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.UssicConMuSSiCCon.entity.Song;
import com.UssicConMuSSiCCon.service.SongService;
import com.UssicConMuSSiCCon.utils.FileUtils;

import lombok.extern.log4j.Log4j2;

@Secured("ROLE_ADMIN")
@Controller
@RequestMapping("/admin")
@Log4j2
public class DashboardController {

	@Autowired
	SongService songService;

	@GetMapping("/")
	public String adminLogin(Model model) {

		System.out.println("Showing dashboard");
		// show table mode
		model.addAttribute("dashboard", 1);

		model.addAttribute("songs", songService.getAll());

		for (Song song : songService.getAll()) {

			System.out.println("Song is : " + song.toString());
		}

		System.out.println("Providing data to dashoard");

		return "dashboard";
	}

	@RequestMapping("/search")
	public String search(@RequestParam("param") String param, Model model) {
		List<Song> songs = songService.searchSong(param);
		model.addAttribute("dashboard", 1);
		model.addAttribute("songs", songs);
		return "dashboard";
	}

	@GetMapping("/add-new-song")
	public String openAddForm(Model model) {
		// show add form mode
		model.addAttribute("addForm", 1);
		return "dashboard";
	}

	@PostMapping("/add-new-song")
	public RedirectView addNewSong(@RequestParam String name, @RequestParam String artist, @RequestParam int isPremium,
			@RequestParam MultipartFile audio, @RequestParam MultipartFile avatar,
			RedirectAttributes redirectAttributes) throws IOException {

		Song newSong = new Song();
		newSong.setName(name);
		newSong.setArtist(artist);
		newSong.setIsPremium(isPremium);

		String audioFileName = StringUtils.cleanPath(audio.getOriginalFilename());
		String avatarFileName = StringUtils.cleanPath(avatar.getOriginalFilename());

		String uploadDir = "MuSSiCConData/" + newSong.getName();

		String audioLink = FileUtils.saveFile(uploadDir, audioFileName, audio);
		String avatarLink = FileUtils.saveFile(uploadDir, avatarFileName, avatar);

		newSong.setSongLink(audioLink);
		newSong.setImgLink(avatarLink);
		log.debug("Song: " + newSong.getName() + " => AudioLink vs AvatarLink: " + newSong.getSongLink() + "||"
				+ newSong.getImgLink());

		songService.addSong(newSong);
		redirectAttributes.addFlashAttribute("uploadMsg", "Add new song successfully!");

		return new RedirectView("/admin/add-new-song");
	}

	@GetMapping("/delete")
	public String deleteSong(@RequestParam("songId") Long songId, Model model) {
		songService.deleteSong(songId);
		return "redirect:/admin/";
	}
}
