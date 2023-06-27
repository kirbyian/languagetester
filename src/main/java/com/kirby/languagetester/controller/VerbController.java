package com.kirby.languagetester.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kirby.languagetester.model.Verb;
import com.kirby.languagetester.repository.VerbRepository;

@RestController
@RequestMapping("api/verbs")
public class VerbController  {

	private VerbRepository verbRepository;

	public VerbController(VerbRepository verbRepository) {
		this.verbRepository = verbRepository;
	}

	@GetMapping()
	public List<Verb> getAllVerbs() {

		List<Verb> verbs = verbRepository.findAll();

		return verbs;

	}

	@GetMapping("/{verbName}")
	public Verb getVerbByName(@PathVariable String verbName) {

		Optional<Verb> verb = verbRepository.findByVerbIgnoreCase(verbName);

		if (verb.isPresent()) {
			return verb.get();
		} else {
			return new Verb();
		}

	}

	@GetMapping("/exists/{verb}")
	public boolean doesVerbExist(@PathVariable String verb) {

		return verbRepository.findByVerbIgnoreCase(verb).isPresent();

	}

	@PostMapping("/{verb}")
	public Verb createVerb(@RequestHeader(value = "Authorization") String token, @PathVariable String verb) {

		Verb verbObject = new Verb();
		verbObject.setVerb(verb);
		return verbRepository.save(verbObject);

	}

}
