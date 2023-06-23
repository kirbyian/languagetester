package com.kirby.languagetester.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "questions")
public class Question extends BaseEntity {

	@Column(name = "question")
	private String question;

	@OneToMany(cascade=CascadeType.ALL )
	@JoinColumn(name = "question_id")
	private List<Answer> answers = new ArrayList<>();
	
	@Column(name="quizID")
	private Long quizID;
	
	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public List<Answer> getAnswers() {
		return answers;
	}

	public void addAnswer(Answer answer) {
		getAnswers().add(answer);
	}

	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}

	public Long getQuizID() {
		return quizID;
	}

	public void setQuizID(Long quizID) {
		this.quizID = quizID;
	}
	
	
}
