package com.kirby.languagetester.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kirby.languagetester.conjugation.ConjugationCreationException;
import com.kirby.languagetester.constants.ConjugationConstants;
import com.kirby.languagetester.constants.OktaConstants;
import com.kirby.languagetester.model.Conjugation;
import com.kirby.languagetester.model.Language;
import com.kirby.languagetester.model.Quiz;
import com.kirby.languagetester.model.QuizTypes;
import com.kirby.languagetester.model.Tense;
import com.kirby.languagetester.model.Verb;
import com.kirby.languagetester.repository.ConjugationRepository;
import com.kirby.languagetester.repository.LanguageRepository;
import com.kirby.languagetester.repository.QuizRepository;
import com.kirby.languagetester.repository.SubjectRepository;
import com.kirby.languagetester.repository.TenseRepository;
import com.kirby.languagetester.repository.VerbRepository;
import com.kirby.languagetester.utils.ExtractJWT;
import com.nimbusds.oauth2.sdk.util.StringUtils;

@Service
public class ConjugationService {

	private ConjugationRepository conjugationRepository;

	private VerbRepository verbRepository;

	private TenseRepository tenseRepository;

	private SubjectRepository subjectRepository;

	private QuizRepository quizRepository;

	private LanguageRepository languageRepository;

	private ExtractJWT extractJWT;

	public ConjugationService(ConjugationRepository conjugationRepository, VerbRepository verbRepository,
			TenseRepository tenseRepository, SubjectRepository subjectRepository, QuizRepository quizRepository,
			ExtractJWT extractJWT, LanguageRepository languageRepository) {
		this.conjugationRepository = conjugationRepository;
		this.verbRepository = verbRepository;
		this.tenseRepository = tenseRepository;
		this.subjectRepository = subjectRepository;
		this.quizRepository = quizRepository;
		this.extractJWT = extractJWT;
		this.languageRepository = languageRepository;
	}

	@Transactional
	public ResponseEntity<String> createConjugations(String verbID, String tenseID, String token,
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

		String message = "Resource created successfully.";
		return ResponseEntity.status(HttpStatus.CREATED).body(message); // 201 Created
	}

	@Transactional
	public ResponseEntity<String> editConjugations(String verbID, String tenseID, String token,
			Map<Long, String> subjectConjugationMap) {

		List<Conjugation> existingConjugations = conjugationRepository
				.findByVerbIdAndTenseIdOrderById(Long.parseLong(verbID), Long.parseLong(tenseID));

		if (existingConjugations.isEmpty()) {
			throw new ConjugationCreationException(
					buildMessage(verbID, tenseID, "Conjugations do not exists for verb and tense, Cannot Modify"));
		}

		for (Conjugation conjugation : existingConjugations) {

			String conjugationString = subjectConjugationMap.get(conjugation.getSubject().getId());
			if (!conjugation.getConjugation().equalsIgnoreCase(conjugationString)) {
				conjugation.setConjugation(conjugationString);
				conjugationRepository.save(conjugation);
			}

		}

		String message = "Resource created successfully.";
		return ResponseEntity.status(HttpStatus.OK).body(message);
	}

	@Transactional
	public ResponseEntity<String> deleteConjugations(String verbID, String tenseID, String token) {

		List<Conjugation> existingConjugations = new ArrayList<>();
		if (StringUtils.isNumeric(verbID) && StringUtils.isNumeric(tenseID)) {
			existingConjugations = conjugationRepository.findByVerbIdAndTenseIdOrderById(Long.parseLong(verbID),
					Long.parseLong(tenseID));
		}

		Tense tense = tenseRepository.getReferenceById(Long.parseLong(tenseID));
		Verb verb = verbRepository.getReferenceById(Long.parseLong(verbID));

		verb.getTenses().remove(tense);
		verbRepository.save(verb);

		long quizid = 0;
		for (Conjugation conjugation : existingConjugations) {

			quizid = conjugation.getQuiz().getId();
			conjugationRepository.delete(conjugation);
		}

		if (quizid != 0) {
			quizRepository.deleteById(quizid);
		}

		String message = "Resource deleted successfully.";
		return ResponseEntity.status(HttpStatus.OK).body(message);
	}

	private void validateConjugationCreation(String verbID, String tenseID) {
		// validate ids are numeric
		if (verbID.isEmpty() || !StringUtils.isNumeric(verbID) || verbID.isEmpty() || !StringUtils.isNumeric(tenseID)) {
			throw new ConjugationCreationException(buildMessage(verbID, tenseID, "Values provided are not valid"));
		}

		// validate conjugation doesn't exist for verb,tense and subject
		if (!conjugationRepository.findByVerbIdAndTenseIdOrderById(Long.parseLong(verbID), Long.parseLong(tenseID))
				.isEmpty()) {
			throw new ConjugationCreationException(
					buildMessage(verbID, tenseID, "Conjugation already exists for verb and tesnse"));
		}
	}

	private String buildMessage(String verbId, String tenseId, String reason) {
		StringBuilder stringBuilder = new StringBuilder(ConjugationConstants.UNABLE_TO_CREATE_CONJUGATION);
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

	public List<Conjugation> findByVerbIDandTenseId(Long verbid, Long tenseid) {
		return conjugationRepository.findByVerbIdAndTenseIdOrderById(verbid, tenseid);
	}

	public List<Conjugation> findConjugationsByOwner(String token) {
		String userEmail = extractJWT.payloadJWTExtraction(token, OktaConstants.SUB);
		return conjugationRepository.findByOwner(userEmail);
	}

	public List<Verb> findAllVerbsWithConjugations() {

		List<Verb> verbs = verbRepository.findAll();

		List<Verb> filteredList = verbs.stream().filter(verb -> !verb.getTenses().isEmpty())
				.collect(Collectors.toList());
		return filteredList;
	}

	public List<Verb> getVerbAndTensesByLanguage(String language) {
		Optional<Language> langaugeObject = languageRepository.findBycode(language);
		List<Verb> verbs = new ArrayList<Verb>();
		if (langaugeObject.isPresent()) {
			verbs = verbRepository.findByLanguage(langaugeObject.get());
		}

		List<Verb> filteredList = verbs.stream().filter(verb -> !verb.getTenses().isEmpty())
				.collect(Collectors.toList());
		return filteredList;
	}

}
