package com.kirby.languagetester.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tenses")
public class Tense extends BaseEntity {

	@Column(name = "tense")
	private String tense;

	public String getTense() {
		return tense;
	}

	public void setTense(String tense) {
		this.tense = tense;
	}

}
