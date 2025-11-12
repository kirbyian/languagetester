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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import com.amazonaws.services.s3.model.PutObjectResult;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kirby.languagetester.model.Language;
import com.kirby.languagetester.model.VocabularyItem;
import com.kirby.languagetester.repository.LanguageRepository;
import com.kirby.languagetester.repository.VocabItemRepository;
import com.kirby.languagetester.service.S3Service;

@Service
public class AudioDownloader {

    @Value("${speechgen.api.url:https://speechgen.io/index.php?r=api/text}")
    private String speechGenURL;

    @Value("${speechgen.api.token}")
    private String speechGenToken;

    @Value("${speechgen.api.email}")
    private String speechGenEmail;

    @Value("${aws.s3.audio.base-url}")
    private String awsBaseURL;

    private final VocabItemRepository vocabItemRepository;
    private final LanguageRepository languageRepository;
    private final S3Service s3Service;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public AudioDownloader(VocabItemRepository vocabItemRepository,
                           LanguageRepository languageRepository,
                           S3Service s3Service) {
        this.vocabItemRepository = vocabItemRepository;
        this.languageRepository = languageRepository;
        this.s3Service = s3Service;
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    @Transactional
    public void processVocabularyItems(Path path) throws Exception {
        List<VocabularyItem> vocabularyItems = parseFile(path);

        for (VocabularyItem vocabularyItem : vocabularyItems) {

            validateItem(vocabularyItem);

            Optional<VocabularyItem> existingItem =
                    vocabItemRepository.findByLanguageAndWord(
                            vocabularyItem.getLanguage(),
                            vocabularyItem.getWord());

            String vocabPath = String.format("audio/%s/%s/%s.mp3",
                    vocabularyItem.getLanguage().getName().toLowerCase(),
                    vocabularyItem.getCategory(),
                    vocabularyItem.getWord().trim());

            if (existingItem.isPresent()) {
                vocabularyItem = existingItem.get();
            }

            boolean exists = s3Service.getAWSFileURL(vocabPath);
            if (!exists) {
                String localFile = postToTextToSpeech(vocabularyItem);
                PutObjectResult result = s3Service.uploadFile(localFile, vocabPath);
                if (result != null) {
                    vocabularyItem.setAudioUrl(awsBaseURL + vocabPath);
                }
            } else {
                vocabularyItem.setAudioUrl(awsBaseURL + vocabPath);
            }

            vocabItemRepository.save(vocabularyItem);
        }
    }

    private void validateItem(VocabularyItem vocabularyItem) throws Exception {
        // TODO: add validation logic (non-null fields, supported language codes, etc.)
    }

    public List<VocabularyItem> parseFile(Path file) {
        List<VocabularyItem> vocabularyItems = new ArrayList<>();
        try {
            if (file.getFileName().toString().endsWith(".csv")) {
                List<String> lines = Files.lines(file).collect(Collectors.toList());
                for (String line : lines) {
                    String[] values = line.split(",");
                    if (values.length < 4) continue;

                    VocabularyItem item = new VocabularyItem();
                    item.setWord(values[0]);
                    item.setTranslation(values[1]);
                    item.setCategory(values[2]);
                    languageRepository.findBycode(values[3])
                            .ifPresent(item::setLanguage);
                    vocabularyItems.add(item);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return vocabularyItems;
    }

    public String postToTextToSpeech(VocabularyItem vocab) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("token", speechGenToken);
        form.add("email", speechGenEmail);

        String voice = switch (vocab.getLanguage().getCode()) {
            case "pt" -> "Yara";
            case "fr" -> "Lea";
            default -> "Brian";
        };

        form.add("voice", voice);
        form.add("text", vocab.getWord());
        form.add("format", "mp3");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(form, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(speechGenURL, request, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            JsonNode root = objectMapper.readTree(response.getBody());
            String fileURL = root.get("file").asText();
            String localPath = "./audio/" + vocab.getWord().trim() + ".mp3";
            downloadFile(fileURL, localPath);
            return localPath;
        }

        throw new IOException("Failed to generate speech for: " + vocab.getWord());
    }

    private void downloadFile(String url, String fileName) throws IOException {
        RequestCallback requestCallback = restTemplate.httpEntityCallback(null, null);
        ResponseExtractor<Void> responseExtractor = (ClientHttpResponse response) -> {
            try (FileOutputStream out = new FileOutputStream(fileName)) {
                StreamUtils.copy(response.getBody(), out);
            }
            return null;
        };
        restTemplate.execute(url, HttpMethod.GET, requestCallback, responseExtractor);
    }
}
