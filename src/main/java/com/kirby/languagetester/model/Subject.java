package com.kirby.languagetester.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "subjects")
public class Subject extends BaseEntity {

	@Column(name = "subject")
	private String subject;
	
	@OneToOne()
	@JoinColumn(name = "languageid")
	private Language language;
	
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	
	

	
}
