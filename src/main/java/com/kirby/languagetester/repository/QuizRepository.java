package com.kirby.languagetester.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kirby.languagetester.model.Language;
import com.kirby.languagetester.model.Quiz;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {

	List<Quiz> findByQuizType(String quizType);
	
	List<Quiz> findByOwner(String owner);

	List<Quiz> findByLanguageAndQuizTypeNot(Language language,String quizType);
	
	List<Quiz> findByName(String name);
}
