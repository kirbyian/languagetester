package com.kirby.languagetester.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kirby.languagetester.constants.OktaConstants;
import com.kirby.languagetester.model.Question;
import com.kirby.languagetester.model.Quiz;
import com.kirby.languagetester.quiz.exception.QuizCreationException;
import com.kirby.languagetester.repository.AnswerRepository;
import com.kirby.languagetester.repository.QuestionRepository;
import com.kirby.languagetester.repository.QuizRepository;
import com.kirby.languagetester.utils.ExtractJWT;

@Service
public class QuizService {

	@Autowired
	private QuizRepository quizRepository;

	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private AnswerRepository answerRepository;

	public QuizService(QuizRepository quizRepository, QuestionRepository questionRepository,
			AnswerRepository answerRepository) {
		this.quizRepository = quizRepository;
		this.questionRepository = questionRepository;
		this.answerRepository = answerRepository;
	}

	@Transactional
	public void createQuiz(String token, Quiz quiz) {

		validateQuiz(quiz);

		Quiz newQuiz = saveQuiz(token, quiz);

		for (Question question : quiz.getQuestions()) {
			createQuestionsAndAnswers(newQuiz, question);
		}

	}

	private void createQuestionsAndAnswers(Quiz newQuiz, Question question) {
		Question newQuestion = createQuestionForQuiz(newQuiz, question);
		createAnswersForQuestion(question, newQuestion);
	}

	private void createAnswersForQuestion(Question question, Question newQuestion) {
		question.getAnswers().forEach(answer -> answer.setQuestion_id(newQuestion.getId()));
		answerRepository.saveAll(question.getAnswers());
	}

	private Question createQuestionForQuiz(Quiz newQuiz, Question question) {
		Question newQuestion = new Question();
		newQuestion.setQuestion(question.getQuestion());
		newQuestion.setQuizID(newQuiz.getId());
		newQuestion.setVersion(0);
		questionRepository.save(newQuestion);
		return newQuestion;
	}

	private Quiz saveQuiz(String token, Quiz quiz) {
		String userEmail = ExtractJWT.payloadJWTExtraction(token, OktaConstants.SUB);
		// save new quiz
		Quiz newQuiz = new Quiz();
		newQuiz.setName(quiz.getName());
		newQuiz.setOwner(userEmail);
		newQuiz.setVersion(0);
		newQuiz.setQuizType(quiz.getQuizType());
		quizRepository.save(newQuiz);
		return newQuiz;
	}

	private void validateQuiz(Quiz quiz) {
		// validate name is provided
		if (quiz.getName().isEmpty()) {
			throw new QuizCreationException("Unable to create Quiz, no name was provided");
		}
		// validate if quizName already exists
		if (!quizRepository.findByName(quiz.getName()).isEmpty()) {
			throw new QuizCreationException(
					"Unable to create Quiz,name provided already exist, please provide a new one");
		}
		// validate if there are questions and Answers
		if (quiz.getQuestions().isEmpty()
				|| quiz.getQuestions().stream().filter(question -> question.getAnswers().isEmpty()).count() < 1) {
			throw new QuizCreationException("Unable to create Quiz,no questions or answers were provided");
		}
	}

}
