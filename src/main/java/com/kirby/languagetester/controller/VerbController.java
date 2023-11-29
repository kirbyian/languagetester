package com.kirby.languagetester.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kirby.languagetester.model.Language;
import com.kirby.languagetester.model.Verb;
import com.kirby.languagetester.repository.LanguageRepository;
import com.kirby.languagetester.repository.VerbRepository;

@RestController
@RequestMapping("api/verbs")
public class VerbController  {

	private VerbRepository verbRepository;
	
	@Autowired
	private LanguageRepository languageRepository;

	public VerbController(VerbRepository verbRepository) {
		this.verbRepository = verbRepository;
	}

	@GetMapping()
	public List<Verb> getAllVerbs(@RequestParam String language) {

		Optional<Language> langaugeObject = languageRepository.findBycode(language);
		List<Verb> verbs = new ArrayList<>();
		if (langaugeObject.isPresent()) {
			verbs = verbRepository.findByLanguageOrderByVerb(langaugeObject.get());
		}

		return verbs;

	}

	@GetMapping("/{verbName}")
	public Verb getVerbByName(@PathVariable String verbName) {

		Optional<Verb> verb = verbRepository.findByVerbIgnoreCaseOrderByVerb(verbName);

		if (verb.isPresent()) {
			return verb.get();
		} else {
			return new Verb();
		}

	}

	@GetMapping("/exists/{verb}")
	public boolean doesVerbExist(@PathVariable String verb) {

		return verbRepository.findByVerbIgnoreCaseOrderByVerb(verb).isPresent();

	}

	@PostMapping("/{verb}")
	public Verb createVerb(@RequestHeader(value = "Authorization") String token, @PathVariable String verb) {

		Verb verbObject = new Verb();
		verbObject.setVerb(verb);
		return verbRepository.save(verbObject);

	}

}
