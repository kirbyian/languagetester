package com.kirby.languagetester.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kirby.languagetester.model.Conjugation;

@Repository
public interface ConjugationRepository extends JpaRepository<Conjugation, Integer> {
	
	List<Conjugation> findByVerbIdAndTenseId(Long verbId,Long tenseId);
	
	List<Conjugation> findByQuizId(Integer quizid);
	
	List<Conjugation> findByVerbId(Integer verbid);
	
	List<Conjugation> findByOwner(String owner);

}
