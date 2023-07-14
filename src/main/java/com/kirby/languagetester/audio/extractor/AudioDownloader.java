package com.kirby.languagetester.audio.extractor;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

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

import com.amazonaws.services.s3.model.PutObjectResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kirby.languagetester.model.Language;
import com.kirby.languagetester.model.VocabularyItem;
import com.kirby.languagetester.repository.LanguageRepository;
import com.kirby.languagetester.repository.VocabItemRepository;
import com.kirby.languagetester.service.S3Service;

@Service
public class AudioDownloader {

	private String speechGenURL = "https://speechgen.io/index.php?r=api/text";

	private String awsBaseURL = "https://kirbylanguageapp.s3.eu-west-1.amazonaws.com/";

	private VocabItemRepository vocabItemRepository;

	private LanguageRepository languageRepository;

	private S3Service s3Service;

	public AudioDownloader(VocabItemRepository vocabItemRepository, LanguageRepository languageRepository,
			S3Service s3Service) {
		this.vocabItemRepository = vocabItemRepository;
		this.languageRepository = languageRepository;
		this.s3Service = s3Service;
	}

	@Transactional
	public void processVocabularyItems(Path path) throws Exception {

		// check if word exists for vocabulary item by word and language
		List<VocabularyItem> vocabularyItems = parseFileinCurrentDirectory(path);

		for (VocabularyItem vocabularyItem : vocabularyItems) {

			validateItem(vocabularyItem);
			Optional<VocabularyItem> foundVocabItem = vocabItemRepository
					.findByLanguageAndWord(vocabularyItem.getLanguage(), vocabularyItem.getWord());
			String vocabularyItemPath = "audio/" + vocabularyItem.getLanguage().getName().toLowerCase() + "/"
					+ vocabularyItem.getCategory() + "/" + vocabularyItem.getWord().trim()+".mp3";

			if (foundVocabItem.isPresent()) {
				vocabularyItem = foundVocabItem.get();
			}

			boolean exists = s3Service.getAWSFileURL(vocabularyItemPath);
			if (!exists) {
				String fileToUpload = postToTextToSpeech(vocabularyItem);
				PutObjectResult uploadedFile = s3Service.uploadFile(fileToUpload, vocabularyItemPath);
				if (uploadedFile != null) {
					vocabularyItem.setAudioUrl(awsBaseURL + vocabularyItemPath);
				}

			}else {
				vocabularyItem.setAudioUrl(awsBaseURL+vocabularyItemPath);
			}
			// upload to AWS

			vocabularyItem = vocabItemRepository.save(vocabularyItem);

		}
	}

	private void validateItem(VocabularyItem vocabularyItem) throws Exception {

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
					item.setLanguage(languageObject.get());
					vocabularyItems.add(item);
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return vocabularyItems;
	}

	public String postToTextToSpeech(VocabularyItem vocabularyItem) throws JsonMappingException, JsonProcessingException {
		RestTemplate restTemplate = new RestTemplate();

		// Create headers
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		String audioFilePath = "";

		// Create parameters
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("token", "f3d42938737ceab9ebd801cbc20ce532");
		map.add("email", "iankirby1991@hotmail.com");
		if(vocabularyItem.getLanguage().getCode().equals("pt")){
			map.add("voice", "Yara");
		}else if(vocabularyItem.getLanguage().getCode().equals("fr")) {
			map.add("voice", "Lea");
		}
		
		map.add("text", vocabularyItem.getWord());
		map.add("format", "mp3");

		// Build the request
		// Send the request as POST
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

		ResponseEntity<String> response = restTemplate.postForEntity(speechGenURL, request, String.class);
		if (response.getStatusCode() == HttpStatus.OK) {

			String responseBody = response.getBody();
			ObjectMapper mapper = new ObjectMapper();
			JsonNode rootNode = mapper.readTree(responseBody);

			// Assuming the response body is a JSON object...
			String speechGenFileURL = rootNode.get("file").asText();
			audioFilePath = "./audio" + vocabularyItem.getWord().trim() + ".mp3";

			downloadFile(speechGenFileURL, audioFilePath);
		}
		return audioFilePath;
	}

	public void downloadFile(String url, String fileName) {
		RestTemplate restTemplate = new RestTemplate();

		RequestCallback requestCallback = restTemplate.httpEntityCallback(null, null);

		ResponseExtractor<Void> responseExtractor = new ResponseExtractor<Void>() {
			@Override
			public Void extractData(ClientHttpResponse response) throws IOException {
				FileOutputStream fileOutputStream = new FileOutputStream(fileName); // Output file path
				StreamUtils.copy(response.getBody(), fileOutputStream);
				fileOutputStream.close();
				return null;
			}
		};
		restTemplate.execute(url, HttpMethod.GET, requestCallback, responseExtractor);
	}

}
