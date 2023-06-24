import React, { FormEvent, useEffect, useState } from "react";
import { Alert, Col, Container, Form, Row } from "react-bootstrap";
import { Button, Input } from "reactstrap";
import Question from "../models/QuestionModel";
import Answer from "../models/AnswerModel";
import QuizModel from "../models/QuizModel";
import { Link, useParams } from "react-router-dom";
import "./QuizPage.css";
import { set } from "react-hook-form";

export const QuizPage = () => {
  const [result, setResult] = useState(0);
  const [showResult, setShowResult] = useState(false);
  const [quiz, setQuiz] = useState("");
  const [questions, setQuestions] = useState<Question[]>([]);
  const [answers, setAnswers] = useState(new Map<number, Answer>());
  const [currentQuestionIndex, setCurrentQuestionIndex] = useState(0);
  const [selectedAnswer, setSelectedAnswer] = useState<number | null>(null);
  const [isLoading, setIsLoading] = useState(false);
  const [httpError, setHttpError] = useState(null);
  const [userResponseQuestionMap, setUserResponseQuestionMap] = useState(
    new Map<number, number>()
  );
  const [isLastQuestion, setIsLastQuestion] = useState(false);
  const [incorrectAnswers, setIncorrectAnswers] = useState<Answer[]>([]);
  const [isFirstQuestion, setIsFirstQuestion] = useState(true);
  
  
  const params = useParams();
 

  useEffect(() => {
    const fetchQuiz = async () => {
      setIsLoading(true);
      try {
        const response = await fetch(
          `http://localhost:8080/api/quizzes/${params.id}`
        );
        if (!response.ok) {
          throw new Error("Something went wrong!");
        }
        const responseData = await response.json();
        const loadedQuiz: QuizModel = {
          id: responseData.id,
          name: responseData.name,
          questions: responseData.questions,
          quizType: responseData.quizType,
        };
        setQuiz(loadedQuiz.name);
        setQuestions(loadedQuiz.questions ?? []);
        setIsLoading(false);
      } catch (error:any) {
        setIsLoading(false);
        setHttpError(error.message);
      }
    };

    fetchQuiz();
  }, [params.id]);

  const handleSelectAnswer = (answerId: number) => {
    setSelectedAnswer(answerId);
  };

  const handleNextQuestion = () => {
    if (selectedAnswer !== null) {
      setUserResponseQuestionMap((prevState) => {
        const updatedResponseMap = new Map(prevState);
        updatedResponseMap.set(
          questions[currentQuestionIndex].id,
          selectedAnswer
        );
        return updatedResponseMap;
      });

      setSelectedAnswer(null);

      if (currentQuestionIndex + 1 < questions.length) {
        setCurrentQuestionIndex((prevIndex) => prevIndex + 1);
      }

      if (currentQuestionIndex  === questions.length) {
        calculateResult();
      }

      if (currentQuestionIndex + 1 === questions.length) {
        setIsLastQuestion(true);
      }

      if (currentQuestionIndex > 0) {
        setIsFirstQuestion(false);
      }
      
      
    }
  };

  const handleQuizSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    if (selectedAnswer !== null) {
      setUserResponseQuestionMap((prevState) => {
        const updatedResponseMap = new Map(prevState);
        updatedResponseMap.set(
          questions[currentQuestionIndex].id,
          selectedAnswer
        );
        return updatedResponseMap;
      });
    }

    calculateResult();
  };

  const handleBack = () => {
    if (currentQuestionIndex === 0) {
      // Handle back button action when on the first question
      // For example, redirect to a different page or show a confirmation message
      console.log("This is the first question!");
      setIsFirstQuestion(true);
    } else {
      setCurrentQuestionIndex((prevIndex) => prevIndex - 1);
      setShowResult(false);
      setSelectedAnswer(null);
      setIsLastQuestion(false);
    }
  };

  const calculateResult = () => {
    let total = 0;
    let correctAnswers = 0;
    const incorrectAnswers: Answer[] = [];

    userResponseQuestionMap.forEach((answerId, questionId) => {
      const question = questions.find((q) => q.id === questionId);
      if (question) {
        const answer = question.answers.find((a) => a.id === answerId);
        if (answer && answer.correct) {
          correctAnswers++;
        }else {
          if(answer){
          incorrectAnswers.push(answer);
          }
        }
      }
    });

    total = Math.round((correctAnswers / questions.length) * 100);
    setIncorrectAnswers(incorrectAnswers);
    setResult(total);
    setShowResult(true);
  };

  if (isLoading) {
    return <h1>Loading</h1>;
  }

  if (questions.length === 0) {
    return <h1>No questions found</h1>;
  }

  

  return (
    <Container className="container justify-content-center">
      <h2>{quiz}</h2>
      {showResult ? (
        <Row  className="justify-content-center">
          <Col  lg="4">
          <Alert variant="info">Result: {result}%</Alert>
          {incorrectAnswers.length > 0 && (
            <div className="incorrect-answers">
              <h5>Incorrect Answers:</h5>
              <ul>
                {incorrectAnswers.map((answer) => (
                  <li key={answer.id}>{answer.answer}</li>
                ))}
              </ul>
            </div>
          )}
          </Col>
          <Button variant="success">
          <Link  to="/quizhome">
               Quizzes
             </Link>
             </Button>
        </Row>
      ) : (
        <div>
          <h4>{questions[currentQuestionIndex].question}</h4>
          <Form onSubmit={handleQuizSubmit}>
            {questions[currentQuestionIndex].answers.map((answer) => (
              <div key={answer.id}>
                <Col md="auto" sm={10}>
                  <Button
                    className={`answer-button ${
                      selectedAnswer === answer.id ? "selected" : ""
                    }`}
                    onClick={() => handleSelectAnswer(answer.id)}
                  >
                    {answer.answer}
                  </Button>
                </Col>
                
            
              </div>
       
            ))}
            
           <br></br>
            {isLastQuestion ? (
              <Button variant="success" type="submit">
                Submit
              </Button>
            ) : (
              <Button variant="primary" onClick={handleNextQuestion}>
                Next
              </Button>
            )}
          </Form>
        </div>
      )}
    </Container>
  );
};

