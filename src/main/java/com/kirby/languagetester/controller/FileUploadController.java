package com.kirby.languagetester.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kirby.languagetester.audio.extractor.AudioDownloader;

@RestController()
@RequestMapping("api/vocabularyitem")
public class FileUploadController {

	private AudioDownloader audioDownloader;

	public FileUploadController(AudioDownloader audioDownloader) {
		this.audioDownloader = audioDownloader;
	}

	@PostMapping("/upload")
	public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
		try {
			byte[] bytes = file.getBytes();
			Path path = Paths.get("../uploads" + file.getOriginalFilename());
			Files.write(path, bytes);
			audioDownloader.extractAudioFiles(path);

			return new ResponseEntity<>("File uploaded successfully", HttpStatus.OK);
		} catch (IOException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
