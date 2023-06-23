package com.kirby.languagetester.controller;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kirby.languagetester.model.Tense;
import com.kirby.languagetester.repository.TenseRepository;

@RestController
@CrossOrigin("http://localhost:3000")
@RequestMapping("api/tenses")
public class TenseController{

	private TenseRepository tenseRepository;

	public TenseController(TenseRepository tenseRepository) {
		this.tenseRepository = tenseRepository;
	}

	@GetMapping
	
	public List<Tense> getAllTenses() {

		return getAllTensesFromCache();
	}
	
	@Cacheable("tenses")
	public  List<Tense> getAllTensesFromCache(){
		return tenseRepository.findAll();
	}

}
