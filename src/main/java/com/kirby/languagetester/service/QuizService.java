package com.kirby.languagetester.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.kirby.languagetester.constants.OktaConstants;
import com.kirby.languagetester.constants.QuizConstants;
import com.kirby.languagetester.model.Language;
import com.kirby.languagetester.model.Question;
import com.kirby.languagetester.model.Quiz;
import com.kirby.languagetester.quiz.exception.QuizCreationException;
import com.kirby.languagetester.repository.LanguageRepository;
import com.kirby.languagetester.repository.QuestionRepository;
import com.kirby.languagetester.repository.QuizRepository;
import com.kirby.languagetester.utils.ExtractJWT;
import com.nimbusds.oauth2.sdk.util.StringUtils;

@Service
public class QuizService {

	private QuizRepository quizRepository;

	private QuestionRepository questionRepository;

	private AnswerService answerService;

	private LanguageRepository languageRepository;

	private ExtractJWT extractJWT;

	public QuizService(QuizRepository quizRepository, QuestionRepository questionRepository,
			AnswerService answerService, LanguageRepository languageRepository, ExtractJWT extractJWT) {
		super();
		this.quizRepository = quizRepository;
		this.questionRepository = questionRepository;
		this.answerService = answerService;
		this.languageRepository = languageRepository;
		this.extractJWT = extractJWT;
	}

	@Transactional
	public void createQuiz(String token, Quiz quiz) {

		setQuizLanguage(quiz);

		validateQuiz(quiz);

		Quiz newQuiz = saveQuiz(token, quiz);

		for (Question question : quiz.getQuestions()) {
			createQuestionsAndAnswers(newQuiz, question);
		}

	}

	private void setQuizLanguage(Quiz quiz) {
		Optional<Language> langaugeObject = languageRepository.findBycode(quiz.getLanguageString());
		if (langaugeObject.isPresent()) {
			quiz.setLanguage(langaugeObject.get());
		}
	}

	private void createQuestionsAndAnswers(Quiz newQuiz, Question question) {
		Question newQuestion = createQuestionForQuiz(newQuiz, question);
		createAnswersForQuestion(question, newQuestion);
	}

	private void createAnswersForQuestion(Question question, Question newQuestion) {
		question.getAnswers().forEach(answer -> answer.setQuestion_id(newQuestion.getId()));

		answerService.saveQuestions(question);
	}

	private Question createQuestionForQuiz(Quiz newQuiz, Question question) {
		Question newQuestion = mapQuestionDetails(newQuiz, question);
		questionRepository.save(newQuestion);
		return newQuestion;
	}

	private Question mapQuestionDetails(Quiz newQuiz, Question question) {
		Question newQuestion = new Question();
		newQuestion.setQuestion(question.getQuestion());
		newQuestion.setQuizID(newQuiz.getId());
		newQuestion.setVersion(0);
		return newQuestion;
	}

	private Quiz saveQuiz(String token, Quiz quiz) {
		String userEmail = extractJWT.payloadJWTExtraction(token, OktaConstants.SUB);
		Quiz newQuiz = new Quiz();
		mapNewQuizDetails(quiz, userEmail, newQuiz);
		quizRepository.save(newQuiz);
		return newQuiz;
	}

	private void mapNewQuizDetails(Quiz quiz, String userEmail, Quiz newQuiz) {
		newQuiz.setName(quiz.getName());
		newQuiz.setOwner(userEmail);
		newQuiz.setVersion(0);
		newQuiz.setLanguage(quiz.getLanguage());
		newQuiz.setQuizType(quiz.getQuizType());
	}

	private void validateQuiz(Quiz quiz) {
		validateNameIsProvided(quiz);
		validateQuizDoesNotExist(quiz);
		validateQuestionsAndAnswersAreProvided(quiz);
	}

	private void validateQuestionsAndAnswersAreProvided(Quiz quiz) {
		if (quiz.getQuestions().isEmpty() || questionsDoNotContainAnAnswer(quiz.getQuestions()))
			throw new QuizCreationException("Unable to create Quiz,no questions or answers were provided");
	}

	private void validateQuizDoesNotExist(Quiz quiz) {
		if (!quizRepository.findByName(quiz.getName()).isEmpty()) {
			throw new QuizCreationException(
					"Unable to create Quiz,name provided already exist, please provide a new one");
		}
	}

	private void validateNameIsProvided(Quiz quiz) {
		if (quiz.getName().isEmpty()) {
			throw new QuizCreationException("Unable to create Quiz, no name was provided");
		}
	}

	private boolean questionsDoNotContainAnAnswer(List<Question> questions) {

		for (Question question : questions) {
			if (question.getAnswers().isEmpty()) {
				return true;
			}
		}
		return false;
	}

	public List<Quiz> findQuizzesByOwner(String token) {
		String userEmail = extractJWT.payloadJWTExtraction(token, OktaConstants.SUB);
		return quizRepository.findByOwner(userEmail);
	}

	public Quiz getQuestionsByQuizId(String id) {

		if (StringUtils.isNumeric(id)) {
			return quizRepository.findById(Long.parseLong(id)).get();
		}
		return null;
	}

	public void deleteByQuizID(String id) {
		if (StringUtils.isNumeric(id)) {
			quizRepository.deleteById(Long.parseLong(id));
		}
	}

	public List<Quiz> findQuizzesByType(String quizType) {
		return quizRepository.findByQuizTypeIgnoreCase(quizType.toUpperCase());
	}

	public List<Quiz> findNonConjugationTypeQuizzes(String language) {
		return quizRepository.findByLanguageAndQuizTypeNotOrderByName(language, QuizConstants.CONJUGATION);
	}

}
