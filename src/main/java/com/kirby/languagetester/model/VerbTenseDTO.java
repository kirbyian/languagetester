package com.kirby.languagetester.model;

import java.util.Objects;
import java.util.Set;

public class VerbTenseDTO {

	Set<Tense> tenses;

	private String verbName;

	private Integer verbId;
	
	private String language;
	
	public VerbTenseDTO(Integer verbId, String verbName) {
		this.verbId=verbId;
		this.verbName=verbName;
	}

	public Set<Tense> getTenses() {
		return tenses;
	}

	public void setTenses(Set<Tense> tenses) {
		this.tenses = tenses;
	}

	public String getVerbName() {
		return verbName;
	}

	public void setVerbName(String verbName) {
		this.verbName = verbName;
	}

	public Integer getVerbId() {
		return verbId;
	}

	public void setVerbId(Integer verbId) {
		this.verbId = verbId;
	}
	

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	@Override
	public int hashCode() {
		return Objects.hash(verbId, verbName, tenses);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VerbTenseDTO other = (VerbTenseDTO) obj;
		return Objects.equals(verbId, other.verbId);
	}
	
	

}
