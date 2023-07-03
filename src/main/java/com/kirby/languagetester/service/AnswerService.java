package com.kirby.languagetester.service;

import org.springframework.stereotype.Service;

import com.kirby.languagetester.model.Question;
import com.kirby.languagetester.repository.AnswerRepository;

@Service
public class AnswerService {

	private AnswerRepository answerRepository;

	public AnswerService(AnswerRepository answerRepository) {
		super();
		this.answerRepository = answerRepository;
	}

	public void saveQuestions(Question question) {

		answerRepository.saveAll(question.getAnswers());

	}

}
