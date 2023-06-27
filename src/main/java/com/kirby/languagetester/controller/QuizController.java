package com.kirby.languagetester.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kirby.languagetester.constants.OktaConstants;
import com.kirby.languagetester.model.Quiz;
import com.kirby.languagetester.repository.QuizRepository;
import com.kirby.languagetester.service.QuizService;
import com.kirby.languagetester.utils.ExtractJWT;
import com.nimbusds.oauth2.sdk.util.StringUtils;

@RequestMapping("api/quizzes")
@RestController
public class QuizController {

	@Autowired
	private QuizRepository quizRepository;

	@Autowired
	private QuizService quizService;

	public QuizController(QuizRepository quizRepository) {
		this.quizRepository = quizRepository;
	}

	@GetMapping("/{id}")
	public Quiz getQuestionsByQuizID(@PathVariable String id) {

		if (StringUtils.isNumeric(id)) {
			return quizRepository.findById(Long.parseLong(id)).get();
		}
		return null;

	}

	@GetMapping
	public List<Quiz> getQuizzesByQuizType(@RequestParam String quizType) {

		List<Quiz> quizzes = quizRepository.findByQuizType(quizType.toUpperCase());

		return quizzes;

	}

	@GetMapping("/user")
	public List<Quiz> getQuizzesByOwner(@RequestHeader(value = "Authorization") String token) {

		String userEmail = ExtractJWT.payloadJWTExtraction(token, OktaConstants.SUB);
		List<Quiz> quizzes = quizRepository.findByOwner(userEmail);

		return quizzes;

	}

	@GetMapping("/all")
	public List<Quiz> getAllQuizzes() {

		List<Quiz> quizzes = quizRepository.findByQuizTypeNot("CONJUGATION");

		return quizzes;

	}

	@PostMapping()
	public void createQuiz(@RequestHeader(value = "Authorization") String token, @RequestBody Quiz quiz) {

		quizService.createQuiz(token, quiz);

	}

}
