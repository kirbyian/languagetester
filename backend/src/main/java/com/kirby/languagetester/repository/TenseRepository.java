package com.kirby.languagetester.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kirby.languagetester.model.Tense;

@Repository
public interface TenseRepository extends JpaRepository<Tense, Long> {
	
	Tense findByTenseContainingIgnoreCase(String tense);
		

}
