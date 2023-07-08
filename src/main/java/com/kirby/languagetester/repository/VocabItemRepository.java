package com.kirby.languagetester.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.kirby.languagetester.model.Language;
import com.kirby.languagetester.model.VocabularyItem;

@Repository
public interface VocabItemRepository extends JpaRepository<VocabularyItem, Long> {

	List<VocabularyItem> findByCategoryIgnoreCaseAndLanguage(String category, Language language);

	@Query("SELECT DISTINCT vi.category FROM VocabularyItem vi WHERE vi.language.id = ?1")
	List<String> findAllDistinctCategories(Long languageId);

	Optional<VocabularyItem> findByLanguageAndWord(Language language, String word);

}
