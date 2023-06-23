package com.kirby.languagetester.dto;

import java.util.ArrayList;
import java.util.List;

import com.kirby.languagetester.model.Question;

public class AnswersSubmissionDto {

	private List<Question> questions = new ArrayList<>();

	private List<AnswerDTO> answers = new ArrayList<>();

	public List<Question> getQuestions() {
		return questions;
	}
	
	public void SetQuestions(List<Question> questions) {
		this.questions=questions;
	}

	public void addQuestion(Question question) {
		getQuestions().add(question);
	}

	public List<AnswerDTO> getAnswers() {
		return answers;
	}

	public void addAnswer(AnswerDTO answer) {
		getAnswers().add(answer);
	}

}
