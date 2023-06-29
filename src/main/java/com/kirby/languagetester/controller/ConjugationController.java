package com.kirby.languagetester.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.kirby.languagetester.constants.OktaConstants;
import com.kirby.languagetester.model.Conjugation;
import com.kirby.languagetester.model.Language;
import com.kirby.languagetester.model.Verb;
import com.kirby.languagetester.repository.ConjugationRepository;
import com.kirby.languagetester.repository.LanguageRepository;
import com.kirby.languagetester.repository.VerbRepository;
import com.kirby.languagetester.service.ConjugationService;
import com.kirby.languagetester.utils.ExtractJWT;

@RequestMapping("api/conjugations")
@RestController()
public class ConjugationController {

	private ConjugationRepository conjugationRepository;

	private VerbRepository verbRepository;

	@Autowired
	private ConjugationService conjugationService;
	
	@Autowired
	private LanguageRepository languageRepository;

	public ConjugationController(ConjugationRepository conjugationRepository, VerbRepository verbRepository) {
		this.conjugationRepository = conjugationRepository;
		this.verbRepository = verbRepository;
	}

	@GetMapping("/verbs")
	public List<Verb> getVerbsAndDistinctTenses(@RequestParam String language) {

		Optional<Language> langaugeObject = languageRepository.findBycode(language);
		List<Verb> verbs = new ArrayList<Verb>();
		if(langaugeObject.isPresent()) {
			verbs = verbRepository.findByLanguage(langaugeObject.get());
		}
		
		List<Verb> filteredList = verbs.stream().filter(verb -> !verb.getTenses().isEmpty())
				.collect(Collectors.toList());

		return filteredList;

	}

	/**
	 * Went want a list of verbs,
	 * 
	 * @param quizid
	 * @return
	 */
	@GetMapping()
	public List<Conjugation> getConjugations(@RequestParam Long verbid, @RequestParam Long tenseid) {

		List<Conjugation> conjugations = conjugationRepository.findByVerbIdAndTenseId(verbid, tenseid);

		return conjugations;

	}

	@GetMapping("/user")
	public List<Conjugation> getConjugationsByOwner(@RequestHeader(value = "Authorization") String token) {

		String userEmail = ExtractJWT.payloadJWTExtraction(token, OktaConstants.SUB);
		List<Conjugation> conjugations = conjugationRepository.findByOwner(userEmail);

		return conjugations;

	}

	@PostMapping
	public ResponseEntity<String> createConjugations(@RequestParam String verbID, @RequestParam String tenseID,
			@RequestHeader(value = "Authorization") String token,
			@RequestBody Map<Long, String> subjectConjugationMap) {

		return conjugationService.createConjugation(verbID, tenseID, token, subjectConjugationMap);
	}

}
