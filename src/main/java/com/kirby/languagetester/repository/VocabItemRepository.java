package com.kirby.languagetester.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.kirby.languagetester.model.Language;
import com.kirby.languagetester.model.VocabularyItem;

public interface VocabItemRepository extends JpaRepository<VocabularyItem, Long> {

	List<VocabularyItem> findByCategoryIgnoreCaseAndLanguage(String category, Language language);

	@Query("SELECT DISTINCT vi.category FROM VocabularyItem vi WHERE vi.language.id = ?1 ORDER BY vi.category")
	List<String> findAllDistinctCategoriesByLanguage(Long languageId);
	
	@Query("SELECT DISTINCT vi.category FROM VocabularyItem vi JOIN vi.language l WHERE LOWER(l.code) = LOWER(?1) AND LOWER(vi.category) LIKE LOWER(CONCAT('%', ?2, '%'))  ORDER BY vi.category")
	List<String> findAllDistinctCategoriesByLanguageAndSearchText(String langauge, String searchText);
	
	Optional<VocabularyItem> findByLanguageAndWord(Language language, String word);

}
