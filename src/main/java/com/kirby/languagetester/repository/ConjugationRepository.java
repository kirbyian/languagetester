package com.kirby.languagetester.repository;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kirby.languagetester.model.Conjugation;

@Repository
public interface ConjugationRepository extends JpaRepository<Conjugation, Integer> {

	@CacheEvict("conjugationsByVerbAndTense")
	List<Conjugation> findByVerbIdAndTenseIdOrderById(Long verbId, Long tenseId);

	@CacheEvict("conjugationByQuizID")
	List<Conjugation> findByQuizId(Integer quizid);

	@CacheEvict("conjugationByVerbID")
	List<Conjugation> findByVerbId(Integer verbid);

	@CacheEvict("conjugationByOwner")
	List<Conjugation> findByOwner(String owner);

}
