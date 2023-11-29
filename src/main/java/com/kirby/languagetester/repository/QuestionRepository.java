package com.kirby.languagetester.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kirby.languagetester.model.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
