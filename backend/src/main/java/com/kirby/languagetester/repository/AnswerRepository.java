package com.kirby.languagetester.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kirby.languagetester.model.Answer;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
	

}
