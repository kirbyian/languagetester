package com.kirby.languagetester.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kirby.languagetester.model.Subject;
import com.kirby.languagetester.service.SubjectService;

@RestController
@RequestMapping("api/subjects")
public class SubjectController {

	private SubjectService subjectService;

	public SubjectController(SubjectService subjectService) {
		this.subjectService = subjectService;
	}

	@GetMapping
	public List<Subject> getAllSubjects(@RequestParam String language) {

		return subjectService.getSubjectsByLanguageCode(language);
	}

}
