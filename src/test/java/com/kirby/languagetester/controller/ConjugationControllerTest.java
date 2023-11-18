package com.kirby.languagetester.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.kirby.languagetester.model.Conjugation;
import com.kirby.languagetester.model.Language;
import com.kirby.languagetester.model.Verb;
import com.kirby.languagetester.service.ConjugationService;

class ConjugationControllerTest {

	@Mock
	private ConjugationService conjugationService;

	@InjectMocks
	private ConjugationController conjugationController;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void getVerbsAndDistinctTenses() {
		// Mocking
		String language = "Portuguese";
		List<Verb> mockVerbs = new ArrayList<>(); // Add some sample data here
		Verb falarVerb = new Verb();
		Language portuguese = new Language();
		portuguese.setName(language);
		falarVerb.setLanguage(portuguese);
		mockVerbs.add(falarVerb);
		when(conjugationService.getVerbAndTensesByLanguage(language)).thenReturn(mockVerbs);

		// Test
		List<Verb> result = conjugationController.getVerbsAndDistinctTenses(language);

		// Verify
		assertEquals(mockVerbs, result);
		verify(conjugationService, times(1)).getVerbAndTensesByLanguage(language);
	}

	@Test
	void getConjugations() {
		// Mocking
		Long verbId = 1L;
		Long tenseId = 1L;
		List<Conjugation> mockConjugations = new ArrayList<>(); // Add some sample data here
		when(conjugationService.findByVerbIDandTenseId(verbId, tenseId)).thenReturn(mockConjugations);

		// Test
		ResponseEntity<List<Conjugation>> result = conjugationController.getConjugations(verbId, tenseId);

		// Verify
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertEquals(mockConjugations, result.getBody());
		verify(conjugationService, times(1)).findByVerbIDandTenseId(verbId, tenseId);
	}

	@Test
	void deleteConjugations() {
		// Mocking
		String verbID = "someVerbId";
		String tenseID = "someTenseId";
		String token = "someToken";

		String message = "Resource deleted successfully.";

		when(conjugationService.deleteConjugations(verbID, tenseID, token))
				.thenReturn(ResponseEntity.status(HttpStatus.OK).body(message)); // or any expected response

		// Test
		ResponseEntity<String> result = conjugationController.deleteConjugations(verbID, tenseID, token);

		// Verify
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertEquals(message, result.getBody());
		verify(conjugationService, times(1)).deleteConjugations(verbID, tenseID, token);
	}

}
