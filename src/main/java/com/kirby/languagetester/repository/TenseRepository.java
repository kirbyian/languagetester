package com.kirby.languagetester.repository;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kirby.languagetester.model.Language;
import com.kirby.languagetester.model.Tense;

@Repository
public interface TenseRepository extends JpaRepository<Tense, Long> {
	
	Tense findByTenseContainingIgnoreCase(String tense);
		
	@CacheEvict("tensesByLanguage")
	List<Tense> findByLanguage(Language language);

}
