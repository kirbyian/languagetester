package com.kirby.languagetester.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kirby.languagetester.model.Language;
import com.kirby.languagetester.model.VocabularyItem;

@Repository
public interface VocabItemRepository extends JpaRepository<VocabularyItem, Long> {

	List<VocabularyItem> findByCategoryIgnoreCaseAndLanguage(String category,Language language);

}
