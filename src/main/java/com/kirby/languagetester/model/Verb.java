package com.kirby.languagetester.model;

import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "verbs")
public class Verb extends BaseEntity {

	@Column(name = "verb")
	private String verb;
	
	@OneToOne()
	@JoinColumn(name = "languageid")
	private Language language;
	
	public String getVerb() {
		return verb;
	}
	
	@ManyToMany(targetEntity = Tense.class, cascade = { CascadeType.ALL })
	@JoinTable(name = "tense_verbs", joinColumns = { @JoinColumn(name = "verb_id") }, inverseJoinColumns = {
			@JoinColumn(name = "tense_id") })
	private Set<Tense> tenses;

	public Set<Tense> getTenses() {
		return tenses;
	}

	public void setTenses(Set<Tense> tenses) {
		this.tenses = tenses;
	}

	public void setVerb(String verb) {
		this.verb = verb;
	}
	
	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Verb other = (Verb) obj;
		return Objects.equals(verb, other.verb);
	}

	
	
}
