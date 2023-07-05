package com.kirby.languagetester.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kirby.languagetester.controller.dto.LanguageCategoryDto;
import com.kirby.languagetester.model.Language;
import com.kirby.languagetester.model.VocabularyItem;
import com.kirby.languagetester.repository.LanguageRepository;
import com.kirby.languagetester.repository.VocabItemRepository;

@Controller
@RequestMapping("api/vocabulary")
public class VocabularyController {

	private VocabItemRepository vocabItemRepository;

	private LanguageRepository languageRepository;

	public VocabularyController(VocabItemRepository vocabItemRepository, LanguageRepository languageRepository) {
		this.vocabItemRepository = vocabItemRepository;
		this.languageRepository = languageRepository;
	}

	@GetMapping()
	public ResponseEntity<List<VocabularyItem>> getAllVerbsByCategory(@RequestParam String category,
			@RequestParam String language) {

		Optional<Language> optionalLanguage = languageRepository.findBycode(language);
		List<VocabularyItem> vocabularyItems = new ArrayList<>();

		if (optionalLanguage.isPresent()) {
			vocabularyItems = vocabItemRepository.findByCategoryIgnoreCaseAndLanguage(category, optionalLanguage.get());
		}

		return new ResponseEntity(vocabularyItems, HttpStatus.OK);

	}

	@GetMapping("/categories")
	public ResponseEntity<LanguageCategoryDto> getCategoriesByLanguage(@RequestParam String language) {

		Optional<Language> optionalLanguage = languageRepository.findBycode(language);
		List<String> distinctCategoriesByLanguage = new ArrayList<>();

		LanguageCategoryDto languageCategoryDTO = new LanguageCategoryDto();

		if (optionalLanguage.isPresent()) {
			languageCategoryDTO.setLanguage(optionalLanguage.get().getName());
			distinctCategoriesByLanguage = vocabItemRepository
					.findAllDistinctCategories(optionalLanguage.get().getId());
			languageCategoryDTO.setCategories(distinctCategoriesByLanguage);
		}

		return new ResponseEntity(languageCategoryDTO, HttpStatus.OK);

	}

}
