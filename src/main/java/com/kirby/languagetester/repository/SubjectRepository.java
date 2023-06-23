package com.kirby.languagetester.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kirby.languagetester.model.Subject;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {

}
