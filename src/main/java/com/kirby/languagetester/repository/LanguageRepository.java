package com.kirby.languagetester.repository;

import java.util.Optional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kirby.languagetester.model.Language;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Long> {
	
	@CacheEvict("languages")
	Optional<Language> findBycode(String code);
}
