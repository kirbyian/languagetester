package com.kirby.languagetester.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kirby.languagetester.model.Verb;

@Repository
public interface VerbRepository extends JpaRepository<Verb, Long> {
	
	Optional<Verb> findByVerbContainingIgnoreCase(String verb);
		

	Optional<Verb> findByVerbIgnoreCase(String verb);
}
