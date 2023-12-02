package com.kirby.languagetester.repository;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.kirby.languagetester.model.Quiz;

public interface QuizRepository extends JpaRepository<Quiz, Long> {

    String QUIZZES_BY_TYPE_CACHE = "quizzesByType";
    String QUIZZES_BY_LANG_AND_TYPE_CACHE = "quizzesByLanguageAndQuizType";

    @CacheEvict(QUIZZES_BY_TYPE_CACHE)
    List<Quiz> findByQuizTypeIgnoreCase(String quizType);

    List<Quiz> findByOwner(String owner);

    @CacheEvict(QUIZZES_BY_LANG_AND_TYPE_CACHE)
    @Query("SELECT q FROM Quiz q JOIN q.language l WHERE l.code = ?1 AND q.quizType <> ?2 ORDER BY q.name")
    List<Quiz> findByLanguageAndQuizTypeNotOrderByName(String languageCode, String quizType);
    
    @Query("SELECT q FROM Quiz q JOIN q.language l WHERE LOWER(l.code) = LOWER(?1) AND q.quizType <> ?2 AND LOWER(q.name) LIKE LOWER(CONCAT('%', ?3, '%')) ORDER BY q.name")
    List<Quiz> findByLanguageAndQuizTypeNotOrderByNameBySearch(String languageCode, String quizType, String textSearch);

    List<Quiz> findByName(String name);
}
