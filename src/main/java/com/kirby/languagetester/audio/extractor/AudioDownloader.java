package com.kirby.languagetester.audio.extractor;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kirby.languagetester.model.Language;
import com.kirby.languagetester.model.VocabularyItem;
import com.kirby.languagetester.repository.LanguageRepository;
import com.kirby.languagetester.repository.VocabItemRepository;

@Service
public class AudioDownloader {

	private String baseReactDir = "/Users/iankirby/react/language-app/languagetester-react/src/assets/audio";
	private String fileURL = "https://speechgen.io/index.php?r=api/text";

	private VocabItemRepository vocabItemRepository;

	private LanguageRepository languageRepository;

	public AudioDownloader(VocabItemRepository vocabItemRepository,
			LanguageRepository languageRepository) {
		this.vocabItemRepository = vocabItemRepository;
		this.languageRepository = languageRepository;
	}

	public void extractAudioFiles(Path path) throws JsonMappingException, JsonProcessingException {

		// get files from current directory

		// parse file

		// check if word exists for vocabulary item by word and language
		List<VocabularyItem> vocabularyItems = parseFileinCurrentDirectory(path);

		for (VocabularyItem vocabularyItem : vocabularyItems) {
			Optional<VocabularyItem> foundVocabItem = vocabItemRepository
					.findByLanguageAndWord(vocabularyItem.getLanguage(), vocabularyItem.getWord());

			if (!foundVocabItem.isPresent()) {
				// save
				vocabularyItem = vocabItemRepository.save(vocabularyItem);
			} else {
				vocabularyItem = foundVocabItem.get();
			}

			String reactUrl = baseReactDir + "/" + vocabularyItem.getLanguage().getName() + "/"
					+ vocabularyItem.getCategory()+"/"+vocabularyItem.getWord().trim();
			//postToTextToSpeech(fileURL, vocabularyItem.getWord(), vocabularyItem.getCategory(),reactUrl);
		}
		// sendRequest(fileURL, text, "furniture");
	}

	public List<VocabularyItem> parseFileinCurrentDirectory(Path file) {

		List<VocabularyItem> vocabularyItems = new ArrayList<>();
		try {
			if (file.getFileName().toString().endsWith(".csv")) {
				System.out.println("Reading file: " + file);
				List<String> lines;

				lines = Files.lines(file).collect(Collectors.toList());

				for (String line : lines) {

					String[] lineValues = line.split(",");
					VocabularyItem item = new VocabularyItem();

					item.setWord(lineValues[0]);
					item.setTranslation(lineValues[1]);
					item.setCategory(lineValues[2]);
					Optional<Language> languageObject = languageRepository.findBycode(lineValues[3]);
					System.out.println(lineValues[0]);
					item.setLanguage(languageObject.get());
					item.setAudioUrl(lineValues[0]+".mp3");
					vocabularyItems.add(item);
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return vocabularyItems;
	}

	public static void postToTextToSpeech(String url, String text, String category,String reactUrl)
			throws JsonMappingException, JsonProcessingException {
		RestTemplate restTemplate = new RestTemplate();

		// Create headers
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		// Create parameters
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("token", "f3d42938737ceab9ebd801cbc20ce532");
		map.add("email", "iankirby1991@hotmail.com");
		map.add("voice", "Yara");
		map.add("text", text);
		map.add("format", "mp3");

		// Build the request
		// Send the request as POST
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

		ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
		if (response.getStatusCode() == HttpStatus.OK) {

			String responseBody = response.getBody();
			ObjectMapper mapper = new ObjectMapper();
			JsonNode rootNode = mapper.readTree(responseBody);

			// Assuming the response body is a JSON object...
			String fileUrl = rootNode.get("file").asText();

			downloadFile(fileUrl, text.trim());
			

		}
	}

	public static void downloadFile(String url, String fileName) {
		RestTemplate restTemplate = new RestTemplate();

		RequestCallback requestCallback = restTemplate.httpEntityCallback(null, null);

		ResponseExtractor<Void> responseExtractor = new ResponseExtractor<Void>() {
			@Override
			public Void extractData(ClientHttpResponse response) throws IOException {
				FileOutputStream fileOutputStream = new FileOutputStream(fileName + ".mp3"); // Output file path
				StreamUtils.copy(response.getBody(), fileOutputStream);
				fileOutputStream.close();
				return null;
			}
		};
		restTemplate.execute(url, HttpMethod.GET, requestCallback, responseExtractor);
	}

}
