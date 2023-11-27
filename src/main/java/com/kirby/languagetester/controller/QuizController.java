package com.kirby.languagetester.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kirby.languagetester.model.Quiz;
import com.kirby.languagetester.service.QuizService;

@RequestMapping("api/quizzes")
@RestController
public class QuizController {

	@Autowired
	private QuizService quizService;

	public QuizController(QuizService quizService) {
		this.quizService = quizService;
	}

	@GetMapping("/{id}")
	public Quiz getQuestionsByQuizID(@PathVariable String id) {

		return quizService.getQuestionsByQuizId(id);

	}

	@DeleteMapping("/{id}")
	public void deleteQuizByID(@PathVariable String id) {

		quizService.deleteByQuizID(id);

	}

	@GetMapping
	public List<Quiz> getQuizzesByQuizType(@RequestParam String quizType) {

		return quizService.findQuizzesByType(quizType);

	}

	@GetMapping("/user")
	public List<Quiz> getQuizzesByOwner(@RequestHeader(value = "Authorization") String token) {

		return quizService.findQuizzesByOwner(token);

	}

	@GetMapping("/all")
	@CacheEvict("quizzes")
	//@Scheduled(fixedRateString = "${caching.spring.generalTTL}")
	public List<Quiz> getAllQuizzes(@RequestParam String language) {

		return quizService.findNonConjugationTypeQuizzes(language);

	}

	@PostMapping()
	public void createQuiz(@RequestHeader(value = "Authorization") String token, @RequestBody Quiz quiz) {

		quizService.createQuiz(token, quiz);

	}

}
