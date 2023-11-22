package com.kirby.languagetester.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kirby.languagetester.model.Language;
import com.kirby.languagetester.model.Tense;
import com.kirby.languagetester.repository.LanguageRepository;
import com.kirby.languagetester.repository.TenseRepository;

@RestController
@RequestMapping("api/tenses")
public class TenseController {
	//test2

	private TenseRepository tenseRepository;

	@Autowired
	private LanguageRepository languageRepository;

	public TenseController(TenseRepository tenseRepository) {
		this.tenseRepository = tenseRepository;
	}

	@GetMapping
	public List<Tense> getAllTenses(@RequestParam String language) {

		return getAllTensesFromCache(language);
	}

	@Cacheable("tenses")
	public List<Tense> getAllTensesFromCache(String language) {

		Optional<Language> langaugeObject = languageRepository.findBycode(language);
		List<Tense> tenses = new ArrayList<Tense>();
		if (langaugeObject.isPresent()) {
			tenses = tenseRepository.findByLanguage(langaugeObject.get());
		}
		return tenses;
	}

}
