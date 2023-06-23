package com.kirby.languagetester.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kirby.languagetester.model.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
}
