package com.kirby.languagetester.repository;

import java.util.Optional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.jpa.repository.JpaRepository;

import com.kirby.languagetester.model.Language;

public interface LanguageRepository extends JpaRepository<Language, Long> {
	
	@CacheEvict("languages")
	Optional<Language> findBycode(String code);
}
