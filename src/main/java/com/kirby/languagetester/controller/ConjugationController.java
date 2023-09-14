package com.kirby.languagetester.controller;

import java.util.List;
import java.util.Map;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kirby.languagetester.model.Conjugation;
import com.kirby.languagetester.model.Verb;
import com.kirby.languagetester.service.ConjugationService;

@RequestMapping("api/conjugations")
@RestController()
public class ConjugationController {

	private ConjugationService conjugationService;

	public ConjugationController(ConjugationService conjugationService) {
		this.conjugationService = conjugationService;
	}

	@GetMapping("/verbs")
	public List<Verb> getVerbsAndDistinctTenses(@RequestParam String language) {

		return conjugationService.getVerbAndTensesByLanguage(language);

	}

	@GetMapping("/admin/verbs")
	@Cacheable("conjugations")
	public List<Verb> getVerbsAndDistinctTensesForAdmin() {

		return conjugationService.findAllVerbsWithConjugations();

	}

	/**
	 * Went want a list of verbs,
	 * 
	 * @param quizid
	 * @return
	 */
	@GetMapping()
	public ResponseEntity<List<Conjugation>> getConjugations(@RequestParam Long verbid, @RequestParam Long tenseid) {

		return new ResponseEntity<List<Conjugation>>(conjugationService.findByVerbIDandTenseId(verbid, tenseid),
				HttpStatus.OK);

	}

	@GetMapping("/user")
	public ResponseEntity<List<Conjugation>> getConjugationsByOwner(
			@RequestHeader(value = "Authorization") String token) {

		return new ResponseEntity<List<Conjugation>>(conjugationService.findConjugationsByOwner(token), HttpStatus.OK);

	}

	@PostMapping
	public ResponseEntity<String> createConjugations(@RequestParam String verbID, @RequestParam String tenseID,
			@RequestHeader(value = "Authorization") String token,
			@RequestBody Map<Long, String> subjectConjugationMap) {

		return conjugationService.createConjugations(verbID, tenseID, token, subjectConjugationMap);
	}

	@PutMapping
	public ResponseEntity<String> editConjugations(@RequestParam String verbID, @RequestParam String tenseID,
			@RequestHeader(value = "Authorization") String token,
			@RequestBody Map<Long, String> subjectConjugationMap) {

		return conjugationService.editConjugations(verbID, tenseID, token, subjectConjugationMap);
	}

	@DeleteMapping
	public ResponseEntity<String> deleteConjugations(@RequestParam String verbID, @RequestParam String tenseID,
			@RequestHeader(value = "Authorization") String token) {

		return conjugationService.deleteConjugations(verbID, tenseID, token);
	}

}
