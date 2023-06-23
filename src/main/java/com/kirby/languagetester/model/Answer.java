package com.kirby.languagetester.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "answers")
public class Answer extends BaseEntity {

	@Column(name = "answer")
	private String answer;

	@Column(name = "is_correct")
	private boolean isCorrect;
	
	
	@Column(name = "question_id")
	private long question_id;

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public boolean isCorrect() {
		return isCorrect;
	}

	public void setCorrect(boolean isCorrect) {
		this.isCorrect = isCorrect;
	}

	public long getQuestion_id() {
		return question_id;
	}

	public void setQuestion_id(long question_id) {
		this.question_id = question_id;
	}
	
	

	
}
