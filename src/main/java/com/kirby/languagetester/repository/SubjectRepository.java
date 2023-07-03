package com.kirby.languagetester.repository;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kirby.languagetester.model.Language;
import com.kirby.languagetester.model.Subject;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {

	@CacheEvict("subjectsByLanguage")
	List<Subject> findByLanguageOrderById(Language language);

}
