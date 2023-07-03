package com.languagetester.tests.quiz;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.kirby.languagetester.model.Answer;
import com.kirby.languagetester.model.Question;
import com.kirby.languagetester.model.Quiz;
import com.kirby.languagetester.quiz.exception.QuizCreationException;
import com.kirby.languagetester.repository.LanguageRepository;
import com.kirby.languagetester.repository.QuestionRepository;
import com.kirby.languagetester.repository.QuizRepository;
import com.kirby.languagetester.service.AnswerService;
import com.kirby.languagetester.service.QuizService;
import com.kirby.languagetester.utils.ExtractJWT;

class QuizServiceTest {

	@Mock
	private QuizRepository quizRepository;

	@Mock
	private QuestionRepository questionRepository;

	@Mock
	private AnswerService answerService;

	@Mock
	private ExtractJWT extractJWT;

	@Mock
	private QuizService quizService;

	@Mock
	private LanguageRepository languageRepository;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		quizService = new QuizService(quizRepository, questionRepository, answerService, languageRepository,
				extractJWT);
	}

	@Test
	void testCreateQuiz_ValidQuiz_ShouldSaveQuizAndQuestions() {
		// Arrange
		String token = "Bearer: dummyToken";
		Quiz quiz = new Quiz();
		quiz.setName("Test Quiz");
		quiz.setQuizType("General");

		List<Question> questions = new ArrayList<Question>();
		Question dummyQuestion = new Question();
		Answer dummyAnswer = new Answer();
		dummyAnswer.setAnswer("A");
		dummyQuestion.setQuestion("Q1");
		dummyQuestion.addAnswer(dummyAnswer);
		questions.add(dummyQuestion);
		quiz.setQuestions(questions);

		when(quizRepository.findByName(quiz.getName())).thenReturn(new ArrayList());

		when(extractJWT.payloadJWTExtraction(token, token)).thenReturn("dummy@email.com");

		// Act
		quizService.createQuiz(token, quiz);

		// Assert
		verify(quizRepository).save(any(Quiz.class));
		verify(questionRepository, times(quiz.getQuestions().size())).save(any(Question.class));
		verify(answerService, atLeastOnce()).saveQuestions(dummyQuestion);

		// Add more assertions if needed
	}

	@Test
	void testCreateQuiz_QuizWithoutName_ShouldThrowException() {
		// Arrange
		String token = "dummyToken";
		Quiz quiz = new Quiz();
		quiz.setName("");
		quiz.setQuizType("General");

		// Act & Assert
		assertThrows(QuizCreationException.class, () -> quizService.createQuiz(token, quiz));

		// Add more assertions if needed
	}

	@Test
	void testCreateQuiz_QuizWithExistingName_ShouldThrowException() {
		// Arrange
		String token = "dummyToken";
		Quiz quiz = new Quiz();
		quiz.setName("Existing Quiz");
		quiz.setQuizType("General");

		// when(quizRepository.findByName(quiz.getName())).thenReturn(singletonList(new
		// Quiz()));

		// Act & Assert
		assertThrows(QuizCreationException.class, () -> quizService.createQuiz(token, quiz));

	}

}
