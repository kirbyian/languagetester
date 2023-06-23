package com.kirby.languagetester.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kirby.languagetester.constants.OktaConstants;
import com.kirby.languagetester.model.Question;
import com.kirby.languagetester.model.Quiz;
import com.kirby.languagetester.repository.AnswerRepository;
import com.kirby.languagetester.repository.QuestionRepository;
import com.kirby.languagetester.repository.QuizRepository;
import com.kirby.languagetester.utils.ExtractJWT;
import com.nimbusds.oauth2.sdk.util.StringUtils;

@RequestMapping("api/quizzes")
@RestController
@CrossOrigin("http://localhost:3000")
public class QuizController {

	@Autowired
	private QuizRepository quizRepository;

	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private AnswerRepository answerRepository;

	public QuizController(QuizRepository quizRepository, QuestionRepository questionRepository,
			AnswerRepository answerRepository) {
		this.quizRepository = quizRepository;
		this.questionRepository = questionRepository;
		this.answerRepository = answerRepository;
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

		String userEmail = ExtractJWT.payloadJWTExtraction(token, OktaConstants.SUB);
		// save new quiz
		Quiz newQuiz = new Quiz();
		newQuiz.setName(quiz.getName());
		newQuiz.setOwner(userEmail);
		newQuiz.setVersion(0);
		newQuiz.setQuizType(quiz.getQuizType());
		quizRepository.save(newQuiz);

		for (Question question : quiz.getQuestions()) {
			Question newQuestion = new Question();
			newQuestion.setQuestion(question.getQuestion());
			newQuestion.setQuizID(newQuiz.getId());
			newQuestion.setVersion(0);
			questionRepository.save(newQuestion);
			question.getAnswers().forEach(answer -> answer.setQuestion_id(newQuestion.getId()));
			answerRepository.saveAll(question.getAnswers());
		}

	}

}
