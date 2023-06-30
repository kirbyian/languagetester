package com.kirby.languagetester.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kirby.languagetester.model.Language;
import com.kirby.languagetester.model.Subject;
import com.kirby.languagetester.model.Tense;
import com.kirby.languagetester.repository.LanguageRepository;
import com.kirby.languagetester.repository.SubjectRepository;

@RestController
@RequestMapping("api/subjects")
public class SubjectController {

	private SubjectRepository subjectRepository;

	@Autowired
	private LanguageRepository languageRepository;

	public SubjectController(SubjectRepository subjectRepository) {
		this.subjectRepository = subjectRepository;
	}

	@GetMapping
	public List<Subject> getAllSubjects(@RequestParam String language) {

		Optional<Language> langaugeObject = languageRepository.findBycode(language);
		List<Subject> subjects = new ArrayList<Subject>();
		if (langaugeObject.isPresent()) {
			subjects = subjectRepository.findByLanguageOrderById(langaugeObject.get());
		}

		return subjects;
	}

}
