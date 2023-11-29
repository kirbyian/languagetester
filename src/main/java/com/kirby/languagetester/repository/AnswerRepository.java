package com.kirby.languagetester.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kirby.languagetester.model.Answer;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
	

}
