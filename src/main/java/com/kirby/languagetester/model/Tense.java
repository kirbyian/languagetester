package com.kirby.languagetester.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tenses")
public class Tense extends BaseEntity {

	@Column(name = "tense")
	private String tense;

	@OneToOne()
	@JoinColumn(name = "languageid")
	private Language language;

	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	public String getTense() {
		return tense;
	}

	public void setTense(String tense) {
		this.tense = tense;
	}

}
