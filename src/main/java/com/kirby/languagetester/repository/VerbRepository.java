package com.kirby.languagetester.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kirby.languagetester.model.Language;
import com.kirby.languagetester.model.Verb;

public interface VerbRepository extends JpaRepository<Verb, Long> {
	
	Optional<Verb> findByVerbContainingIgnoreCaseOrderByVerb(String verb);
		
	Optional<Verb> findByVerbIgnoreCaseOrderByVerb(String verb);
	
	List<Verb> findByLanguageOrderByVerb(Language language);
}
