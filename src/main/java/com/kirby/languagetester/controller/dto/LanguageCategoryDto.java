package com.kirby.languagetester.controller.dto;

import java.util.List;

public class LanguageCategoryDto {

	private String language;

	private List<String> categories;

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public List<String> getCategories() {
		return categories;
	}

	public void setCategories(List<String> categories) {
		this.categories = categories;
	}

}
