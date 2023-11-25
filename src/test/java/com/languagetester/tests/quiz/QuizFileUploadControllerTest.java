package com.languagetester.tests.quiz;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.kirby.languagetester.controller.QuizFileUploadController;
import com.kirby.languagetester.service.QuizService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class QuizFileUploadControllerTest {

    @Mock
    private QuizService quizService;

    @InjectMocks
    private QuizFileUploadController quizFileUploadController;

    @Test
    void testUploadFile() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(quizFileUploadController).build();

        Path testFilePath = Paths.get("src/test/resources/test.csv");
        MockMultipartFile multipartFile = new MockMultipartFile("file", "test.csv", "text/csv",
                Files.readAllBytes(testFilePath));

        mockMvc.perform(multipart("/api/quiz/upload")
                .file(multipartFile)
                .header("Authorization", "your-token"))
                .andExpect(status().isOk());

        verify(quizService, times(1)).createQuiz(any(), any());
    }
}
