package com.kirby.languagetester.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kirby.languagetester.constants.OktaConstants;
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
import com.kirby.languagetester.utils.ExtractJWT;

@RequestMapping("api/conjugations")
@RestController()
@CrossOrigin("https://languagetester-react-5effb2069628.herokuapp.com")
public class ConjugationController {

	private ConjugationRepository conjugationRepository;

	private VerbRepository verbRepository;

	private TenseRepository tenseRepository;

	private SubjectRepository subjectRepository;
	
	private QuizRepository quizRepository;
	

	public ConjugationController(ConjugationRepository conjugationRepository, VerbRepository verbRepository,
			TenseRepository tenseRepository, SubjectRepository subjectRepository, QuizRepository quizRepository) {
		this.conjugationRepository = conjugationRepository;
		this.verbRepository = verbRepository;
		this.tenseRepository = tenseRepository;
		this.subjectRepository = subjectRepository;
		this.quizRepository = quizRepository;
	}

	@GetMapping("/verbs")
	public List<Verb> getVerbsAndDistinctTenses() {

		List<Verb> verbs = verbRepository.findAll();

		List<Verb> filteredList = verbs.stream().filter(verb -> !verb.getTenses().isEmpty())
				.collect(Collectors.toList());

		return filteredList;

	}

	/**
	 * Went want a list of verbs,
	 * 
	 * @param quizid
	 * @return
	 */
	@GetMapping()
	public List<Conjugation> getConjugations(@RequestParam Long verbid, @RequestParam Long tenseid) {

		List<Conjugation> conjugations = conjugationRepository.findByVerbIdAndTenseId(verbid, tenseid);

		return conjugations;

	}

	@GetMapping("/user")
	public List<Conjugation> getConjugationsByOwner(@RequestHeader(value = "Authorization") String token) {

		String userEmail = ExtractJWT.payloadJWTExtraction(token, OktaConstants.SUB);
		List<Conjugation> conjugations = conjugationRepository.findByOwner(userEmail);

		return conjugations;

	}

	@PostMapping
	public List<Conjugation> createConjugations(@RequestParam String verbID, @RequestParam String tenseID,
			@RequestHeader(value = "Authorization") String token,
			@RequestBody Map<Long,String> subjectConjugationMap) {

		Tense tense = tenseRepository.getReferenceById(Long.parseLong(tenseID));
		Verb verb=verbRepository.getReferenceById(Long.parseLong(verbID));
		verb.getTenses().add(tense);
		verbRepository.save(verb);
		
		Quiz quiz = saveQuizForTenseAndVerbConjugation(tense, verb);

		List<Conjugation> conjugations=new ArrayList<Conjugation>();
		 
		for (Long key : subjectConjugationMap.keySet()) {
			Conjugation conjugation = mapConjugationDetails(subjectConjugationMap, tense, verb, quiz, key);
			conjugationRepository.save(conjugation);
			conjugations.add(conjugation);
		}

		return conjugations;
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
		Quiz quiz=new Quiz();
		quiz.setQuizType(QuizTypes.CONJUGATION.name());
		quiz.setName(verb.getVerb()+":"+tense.getTense());
		quizRepository.save(quiz);
		return quiz;
	}

}
