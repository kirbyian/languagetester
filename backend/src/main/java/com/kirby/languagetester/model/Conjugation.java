package com.kirby.languagetester.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "conjugations")
public class Conjugation extends BaseEntity {

	@Column(name = "conjugation")
	private String conjugation;

	@OneToOne()
	@JoinColumn(name = "tenseid")
	private Tense tense;

	@OneToOne()
	@JoinColumn(name = "subjectid")
	private Subject subject;

	@OneToOne()
	@JoinColumn(name = "verbid")
	private Verb verb;

	@OneToOne()
	@JoinColumn(name = "quizid")
	private Quiz quiz;

	@Column(name = "owner")
	private String owner;

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getConjugation() {
		return conjugation;
	}

	public void setConjugation(String conjugation) {
		this.conjugation = conjugation;
	}

	public Tense getTense() {
		return tense;
	}

	public void setTenseid(Tense tense) {
		this.tense = tense;
	}

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public Verb getVerb() {
		return verb;
	}

	public void setVerb(Verb verb) {
		this.verb = verb;
	}

	public Quiz getQuiz() {
		return quiz;
	}

	public void setQuiz(Quiz quiz) {
		this.quiz = quiz;
	}

}
