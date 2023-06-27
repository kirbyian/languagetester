package com.kirby.languagetester.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kirby.languagetester.model.Subject;
import com.kirby.languagetester.repository.SubjectRepository;

@RestController
@RequestMapping("api/subjects")
public class SubjectController  {

	private SubjectRepository subjectRepository;

	public SubjectController(SubjectRepository subjectRepository) {
		this.subjectRepository = subjectRepository;
	}

	@GetMapping
	public List<Subject> getAllSubjects() {

		return subjectRepository.findAll();
	}

}
