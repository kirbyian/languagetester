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
@Table(name = "quizzes")
public class Quiz extends BaseEntity {
	
	@OneToMany(cascade=CascadeType.ALL, orphanRemoval = true )
	@JoinColumn(name = "quizID" )
	private List<Question> questions = new ArrayList<>();
	
	@Column(name = "quiz_type")
	private String quizType;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "owner")
	private String owner;
	
	
	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getQuizType() {
		return quizType;
	}

	public void setQuizType(String quizType) {
		this.quizType = quizType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

	
	
}
