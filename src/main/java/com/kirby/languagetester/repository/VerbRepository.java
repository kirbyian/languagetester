package com.kirby.languagetester.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.kirby.languagetester.model.Verb;

public interface VerbRepository extends JpaRepository<Verb, Long> {

	public static final String VERBS_BY_LANGUAGE_WITH_TENSES = "SELECT DISTINCT v from Verb v JOIN v.tenses JOIN v.language l where l.code =?1 order by v.verb ";

	Optional<Verb> findByVerbContainingIgnoreCaseOrderByVerb(String verb);

	Optional<Verb> findByVerbIgnoreCaseOrderByVerb(String verb);

	@CacheEvict("VerbsByLanguage")
	@Query(VERBS_BY_LANGUAGE_WITH_TENSES)
	List<Verb> findByLanguageOrderByVerb(String language);
}
