package com.kirby.languagetester.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.kirby.languagetester.conjugation.ConjugationCreationException;
import com.kirby.languagetester.model.Conjugation;
import com.kirby.languagetester.model.Quiz;
import com.kirby.languagetester.model.QuizTypes;
import com.kirby.languagetester.model.Tense;
import com.kirby.languagetester.model.Verb;
import com.kirby.languagetester.repository.ConjugationRepository;
import com.kirby.languagetester.repository.QuizRepository;
import com.kirby.languagetester.repository.SubjectRepository;
import com.kirby.languagetester.repository.TenseRepository;
import com.kirby.languagetester.repository.VerbRepository;
import com.nimbusds.oauth2.sdk.util.StringUtils;

@Service
public class ConjugationService {

	private static final String UNABLE_TO_CREATE_CONJUGATION = "Unable to create Conjugation:";

	private ConjugationRepository conjugationRepository;

	private VerbRepository verbRepository;

	private TenseRepository tenseRepository;

	private SubjectRepository subjectRepository;

	private QuizRepository quizRepository;

	@Transactional
	public List<Conjugation> createConjugation(String verbID, String tenseID, String token,
			Map<Long, String> subjectConjugationMap) {

		validateConjugationCreation(verbID, tenseID);

		Tense tense = tenseRepository.getReferenceById(Long.parseLong(tenseID));
		Verb verb = verbRepository.getReferenceById(Long.parseLong(verbID));

		verb.getTenses().add(tense);
		verbRepository.save(verb);

		Quiz quiz = saveQuizForTenseAndVerbConjugation(tense, verb);

		List<Conjugation> conjugations = new ArrayList<Conjugation>();

		for (Long key : subjectConjugationMap.keySet()) {
			Conjugation conjugation = mapConjugationDetails(subjectConjugationMap, tense, verb, quiz, key);
			conjugationRepository.save(conjugation);
			conjugations.add(conjugation);
		}

		return conjugations;
	}

	private void validateConjugationCreation(String verbID, String tenseID) {
		// validate ids are numeric
		if (verbID.isEmpty() || !StringUtils.isNumeric(verbID) || verbID.isEmpty() || !StringUtils.isNumeric(tenseID)) {
			throw new ConjugationCreationException(buildMessage(verbID, tenseID, "Values provided are not valid"));
		}

		// validate conjugation doesn't exist for verb,tense and subject
		if (!conjugationRepository.findByVerbIdAndTenseId(Long.parseLong(verbID), Long.parseLong(tenseID)).isEmpty()) {
			throw new ConjugationCreationException(
					buildMessage(verbID, tenseID, "Conjugation already exists for verb and tesnse"));
		}
	}

	private String buildMessage(String verbId, String tenseId, String reason) {
		StringBuilder stringBuilder = new StringBuilder(UNABLE_TO_CREATE_CONJUGATION);
		stringBuilder.append(" For VerbID ").append(verbId);
		stringBuilder.append("For TenseID ").append(tenseId);
		stringBuilder.append("-").append(reason);
		return stringBuilder.toString();
	}

	private Conjugation mapConjugationDetails(Map<Long, String> subjectConjugationMap, Tense tense, Verb verb,
			Quiz quiz, Long key) {
		Conjugation conjugation = new Conjugation();
		conjugation.setTenseid(tense);
		conjugation.setVerb(verb);
		conjugation.setSubject(subjectRepository.getReferenceById(key));
		conjugation.setConjugation(subjectConjugationMap.get(key));
		conjugation.setQuiz(quiz);
		return conjugation;
	}

	private Quiz saveQuizForTenseAndVerbConjugation(Tense tense, Verb verb) {
		Quiz quiz = new Quiz();
		quiz.setQuizType(QuizTypes.CONJUGATION.name());
		quiz.setName(verb.getVerb() + ":" + tense.getTense());
		quizRepository.save(quiz);
		return quiz;
	}

}
