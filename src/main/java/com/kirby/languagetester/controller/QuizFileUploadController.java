package com.kirby.languagetester.controller;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.kirby.languagetester.model.Answer;
import com.kirby.languagetester.model.Question;
import com.kirby.languagetester.model.Quiz;
import com.kirby.languagetester.model.QuizTypes;
import com.kirby.languagetester.service.QuizService;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

@RestController()
@RequestMapping("api/quiz")
public class QuizFileUploadController {

	private Logger logger = LoggerFactory.getLogger(QuizFileUploadController.class);

	private QuizService quizService;

	public QuizFileUploadController(QuizService quizService) {
		this.quizService = quizService;
	}

	@PostMapping(value = "/upload")
	public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file,
			@RequestHeader(value = "Authorization") String token) throws Exception {
		try {
			byte[] bytes = file.getBytes();
			String directoryPath = "/tmp/uploads/";
			Path directory = Paths.get(directoryPath);

			// Create the directory if it doesn't exist
			if (!Files.exists(directory)) {
				Files.createDirectories(directory);
			}
			Path path = Paths.get(directoryPath + file.getOriginalFilename());
			logger.info("Writing file to path:" + path.toString());
			Files.write(path, bytes);
			createQuizByUpload(path, token);

			return new ResponseEntity<>("File uploaded successfully", HttpStatus.OK);
		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Transactional
	public void createQuizByUpload(Path path, String token) throws CsvValidationException {

		// check if word exists for vocabulary item by word and language
		Quiz quiz = parseFileinCurrentDirectory(path);
		quizService.createQuiz(token, quiz);

	}

	public Quiz parseFileinCurrentDirectory(Path file) throws CsvValidationException {
		Quiz quiz = new Quiz();
		List<Question> questions = new ArrayList<>();
		List<String[]> lines = new ArrayList<>();

		quiz.setQuizType(QuizTypes.VOCABULARY.name());

		try (CSVReader reader = new CSVReader(new FileReader(file.toString()))) {
			String[] nextLine;
			// Read and store all lines
			addLinesToList(lines, reader);

			// Set Name and Language String from first line
			setQuizNameAndLanguage(quiz, lines);

			// Process lines
			for (int i = 0; i < lines.size(); i++) {

				if (lines.get(i).length > 0 && lines.get(i)[0].equalsIgnoreCase("q")) {
					i = addQuestionToList(questions, lines, i);
				}
			}

			// Set questions for the quiz
			quiz.setQuestions(questions);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return quiz;
	}

	private int addQuestionToList(List<Question> questions, List<String[]> lines, int i) {
		Question question = new Question();
		question.setQuestion(lines.get(i)[1]);

		// Process answers
		for (int j = i + 1; j < lines.size(); j++) {

			if (lines.get(j).length > 0 && lines.get(j)[0].equalsIgnoreCase("a")) {
				setAnswer(lines, question, j);
			} else {
				// If the line doesn't start with "a," it's the next question
				i = j - 1; // Move the outer loop index back to the last processed line
				break;
			}
		}

		// Set the question only if there are answers
		if (!question.getAnswers().isEmpty()) {
			questions.add(question);
		}
		return i;
	}

	private void addLinesToList(List<String[]> lines, CSVReader reader) throws IOException, CsvValidationException {
		String[] nextLine;
		while ((nextLine = reader.readNext()) != null) {
			lines.add(nextLine);
		}
	}

	private void setQuizNameAndLanguage(Quiz quiz, List<String[]> lines) {
		if (!lines.isEmpty()) {
			quiz.setName(lines.get(0)[0]);
			quiz.setLanguageString(lines.get(0)[1]);
		}
	}

	private void setAnswer(List<String[]> lines, Question question, int j) {
		Answer answer = new Answer();
		answer.setAnswer(lines.get(j)[1]);
		answer.setCorrect(lines.get(j).length > 2 && "y".equalsIgnoreCase(lines.get(j)[2].trim()));
		question.addAnswer(answer);
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
