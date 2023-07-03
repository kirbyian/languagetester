package com.kirby.languagetester.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.kirby.languagetester.model.Language;
import com.kirby.languagetester.model.Subject;
import com.kirby.languagetester.repository.LanguageRepository;
import com.kirby.languagetester.repository.SubjectRepository;

@Service
public class SubjectService {

	private SubjectRepository subjectRepository;

	private LanguageRepository languageRepository;

	public SubjectService(SubjectRepository subjectRepository, LanguageRepository languageRepository) {
		this.subjectRepository = subjectRepository;
		this.languageRepository = languageRepository;
	}

	public List<Subject> getSubjectsByLanguageCode(String language) {

		Optional<Language> languageObject = languageRepository.findBycode(language);
		List<Subject> subjects = new ArrayList<Subject>();
		if (languageObject.isPresent()) {
			subjects = subjectRepository.findByLanguageOrderById(languageObject.get());
		}
		return subjects;
	}

}
