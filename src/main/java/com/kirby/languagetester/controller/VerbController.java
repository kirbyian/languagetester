package com.kirby.languagetester.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kirby.languagetester.dto.VerbLanguageDTO;
import com.kirby.languagetester.model.Language;
import com.kirby.languagetester.model.Verb;
import com.kirby.languagetester.repository.LanguageRepository;
import com.kirby.languagetester.repository.VerbRepository;

@RestController
@RequestMapping("api/verbs")
public class VerbController {

	private VerbRepository verbRepository;

	private LanguageRepository languageRepository;

	public VerbController(VerbRepository verbRepository, LanguageRepository languageRepository) {
		this.verbRepository = verbRepository;
		this.languageRepository = languageRepository;
	}

	@GetMapping()
	public List<Verb> getAllVerbsWithTenses(@RequestParam String language) {

		return verbRepository.findByLanguageOrderByVerb(language);
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

	@PostMapping()
	public Verb createVerb(@RequestHeader(value = "Authorization") String token, @RequestBody VerbLanguageDTO verbDTO) {

		Verb verb = new Verb();
		verb.setVerb(verbDTO.getVerb());
		Optional<Language> optional = languageRepository.findBycode(verbDTO.getLanguage());

		if (optional.isPresent()) {
			verb.setLanguage(optional.get());
			verb = verbRepository.save(verb);
		}

		return verb;

	}

}
