import React, { useRef, useState } from "react";
import { Col, Container, Row } from "reactstrap";
import { Button, Form } from "react-bootstrap";
import { redirect, useNavigate } from "react-router-dom";
import AnswerModel from "../models/AnswerModel";
import QuestionModel from "../models/QuestionModel";
import QuizModel from "../models/QuizModel";
import "./AdminHome.css"

export const AdminQuizWizard = () => {
  const [step, setStep] = useState(1);
  const [anotherQuestion, setAnotherQuestion] = useState(true);
  const [showAddQuestion, setShowAddQuestion] = useState(true);
  const [questions, setQuestions] = useState<QuestionModel[]>([]);
  const [answers, setAnswers] = useState<Array<AnswerModel>>([]);

  const refQuizName = useRef<HTMLInputElement>(null);
  const [quizName, setQuizName] = useState("");
  const refIsCorrect = useRef<HTMLInputElement>(null);
  const questionString = useRef<HTMLInputElement>(null);
  const [questionName, setQuestionName] = useState("");
  const refAnswer = useRef<HTMLInputElement>(null);
  const navigate = useNavigate();
  const base_url = `${process.env.REACT_APP_SPRING_BASE_URL}${process.env.REACT_APP_SPRING_PORT}`;

  const onNext = () => {
    if (step === 1) {
      if (!refQuizName.current?.value) {
        alert("Please enter a quiz name");
        return;
      }
      setQuizName(refQuizName.current?.value);
    }

    setStep(step + 1);
  };

  const onPrevious = () => {
    setStep(step - 1);
  };


  const addAnswer = () => {
    const answerString = refAnswer.current?.value;
    const isCorrect = refIsCorrect.current?.checked;

    if (!answerString) {
      alert("Please enter an answer");
      return;
    }

    if (answers.filter((answer) => answer.correct).length > 0 && isCorrect) {
      alert(
        "You have already entered a correct answer. Please uncheck the correct box to add another answer"
      );
      return;
    }

    if (answers.find((answer) => answer.answer === answerString)) {
      alert(
        "You have already entered this answer. Please enter a different answer"
      );
      return;
    }

    const newAnswer = new AnswerModel(
      answers.length,
      "",
      answerString,
      isCorrect ? true : false
    );
    const updatedAnswers = [...answers, newAnswer];
    setAnswers(updatedAnswers);

    // Clear the answer input
    refAnswer.current.value = "";
    setShowAddQuestion(false);
    if (questionString.current != null && questionString.current.value != "") {
      setQuestionName(questionString.current.value);
    }
  };

  const updateAnswerText = (answerIndex: number, value: string) => {
    const updatedAnswers = [...answers];
    const answer = answers[answerIndex];
    if (answer) {
      answers[answerIndex].answer = value;
    }
    updatedAnswers[answerIndex] = answer;
    setAnswers(updatedAnswers);
  };

  function updateAnswerIsCorrect(index: number, value: string): void {
    const updatedAnswers = [...answers];
    let updatedAnswer = updatedAnswers[index];
    updatedAnswer.correct = value === "true" ? true : false;
    setAnswers(updatedAnswers);
  }

  const removeAnswer = (index: number) => {
    let updatedAnswers = answers.filter(
      (answer, answerIndex) => index !== answerIndex
    );

    setAnswers(updatedAnswers);
  };

  const toggleCorrectAnswer = (id: number) => {
    const updatedAnswers = answers.map((answer) =>
      answer.id === id ? { ...answer, correct: !answer.correct } : answer
    );
    setAnswers(updatedAnswers);
  };

  const addQuestion = () => {
    if (!questionName || questionName === "") {
      alert("Please enter a question");
      return;
    }

    const newAnswers = answers.filter(
      (answer) => answer.answer !== "" && answer.correct === true
    );
    if (newAnswers.length === 0) {
      alert("Please enter at least one correct answer");
      return;
    }

    const answerModels = answers.map(
      (answer) => new AnswerModel(0, "", answer.answer, answer.correct)
    );
    const question = new QuestionModel(0, "", questionName, answerModels);
    setQuestions([...questions, question]);
    setAnswers([]); // Clear the answers for the next question
    setQuestionName("");
    setShowAddQuestion(true);
  };

  const FormWizardStep1 = (props: { onNext: () => void }) => {
    const { onNext } = props;

    return (
      <div>
        <Row>
          <Col className="d-flex justify-content-center ">
            <h3>Create Quiz Wizard</h3>
          </Col>
        </Row>
        <Row>
          <Col className="d-flex justify-content-center">
            <label>
              Quiz Name:
              <input type="text" ref={refQuizName} />
            </label>
          </Col>
        </Row>
        <Row>
          <Col className="d-flex justify-content-center">
            <Button onClick={onNext}>Next</Button>
          </Col>
        </Row>
      </div>
    );
  };

  const FormWizardStep2 = (props: {
    onNext: () => void;
    onPrevious: () => void;
  }) => {
    const { onNext, onPrevious } = props;

    return (
      <Container className="create-quiz-wizard">
        <Row>
          {/* // 1st column for adding questions and answers */}
          <Col xs lg="6">
            <div className="quiz-form-div">
              {anotherQuestion && (
                <Row className="justify-content-center">
                  <Row className="justify-content-center">
                    <Col xs lg="auto">
                      <h3 className="justify-content-center">{quizName}</h3>
                    </Col>
                  </Row>
                  <Row className="justify-content-center">
                    {showAddQuestion && (
                      <Col xs lg="auto">
                        <Form.Group className="mb-3" controlId="formGroupEmail">
                          <strong>
                            {" "}
                            <Form.Label id="questionIn">
                              Question
                            </Form.Label>{" "}
                          </strong>
                          <Form.Control
                            type="text"
                            ref={questionString}
                            placeholder="Enter Question"
                          />
                        </Form.Group>
                      </Col>
                    )}
                    {!showAddQuestion && (
                      <Col xs lg="auto">
                        <Form.Group className="mb-3" controlId="formGroupEmail">
                          <h4>{questionName} </h4>
                        </Form.Group>
                      </Col>
                    )}
                  </Row>

                  <br />
                  <Row className="justify-content-md-center">
                    <Col xs lg="auto">
                      <Form.Group className="mb-3" controlId="formGroupEmail">
                        <strong>
                          {" "}
                          <Form.Label id="questionIn">Answer</Form.Label>{" "}
                        </strong>
                        <Form.Control
                          type="text"
                          ref={refAnswer}
                          placeholder="Enter Answer"
                        />
                      </Form.Group>
                    </Col>
                  </Row>
                  <Row className="justify-content-md-center">
                    <Col xs lg="auto">
                      <Form.Check
                        type="checkbox"
                        id="correctCheckbox"
                        label="Is Correct"
                        ref={refIsCorrect}
                      />
                    </Col>
                  </Row>
                  <br />
                  <Row className="justify-content-md-center">
                    <Col xs lg="auto">
                      <Button
                        className="btn-add-answer"
                        onClick={() => addAnswer()}
                      >
                        Add Answer
                      </Button>
                    </Col>
                    <Col md="auto">
                      <Button
                        className="btn-add-question"
                        onClick={addQuestion}
                      >
                        Add Question
                      </Button>
                    </Col>
                  </Row>
                  <br />
                </Row>
              )}
            </div>
          </Col>

          {/* // 2nd column for adding questions and answers */}
          <Col xs lg="5">
            <div className="quiz-form-div-results">
              <Row className="justify-content-center">
                <h5>Current Answers</h5>

                {answers.map((answer, answerIndex) => (
                  <Row key={answerIndex} className="justify-content-center">
                    <Col>
                      <strong>Answer:</strong>
                    </Col>
                    <Col>
                      <input
                        type="text"
                        value={answer.answer}
                        onChange={(e) =>
                          updateAnswerText(answerIndex, e.target.value)
                        }
                      />
                    </Col>
                    <Row className="justify-content-center">
                      <Col>
                        <strong>Is Correct: </strong>
                      </Col>
                      <input
                        type="checkbox"
                        checked={answer.correct}
                        onChange={() => toggleCorrectAnswer(answerIndex)}
                      />
                    </Row>

                    <Row className="justify-content-center">
                      <Col>
                        <Button
                          className="btn-delete"
                          onClick={() => removeAnswer(answerIndex)}
                        >
                          Remove
                        </Button>
                      </Col>
                    </Row>
                  </Row>
                ))}
              </Row>
            </div>
          </Col>
        </Row>

        <br />

        <Row className="justify-content-md-center">
          <Col md="auto">
            <Row className="justify-content-md-center">
              <Col md="auto">
                <Button onClick={onPrevious}>Previous</Button>
              </Col>

              <Col md="auto">
                <Button onClick={onNext}>Current Questions</Button>
              </Col>
            </Row>
          </Col>
        </Row>
      </Container>
    );
  };

  const FormWizardStep3 = (props: {
    onPrevious: () => void;
  }) => {
    const { onPrevious } = props;

    const updateAnswerText = (
      answerIndex: number,
      value: string,
      questionIndex: number
    ) => {
      const updatedQuestions = [...questions];
      const question = questions[questionIndex];
      if (question) {
        question.answers[answerIndex].answer = value;
      }
      updatedQuestions[questionIndex] = question;
      setQuestions(updatedQuestions);
    };

    const removeAnswer = (answerIndex: number, questionIndex: number) => {
      let updatedQuestions = [...questions];
      const question = questions[questionIndex];
      if (question) {
        question.answers = questions[questionIndex].answers.filter(
          (answer, index) => index !== answerIndex
        );
      }
      if (question.answers.length === 0) {
        updatedQuestions = updatedQuestions.filter(
          (question, index) => index !== questionIndex
        );
        setShowAddQuestion(true);
      }

      setQuestions(updatedQuestions);
    };

    const createQuiz = async () => {
      const url = `${base_url}/api/quizzes/`;
      if (!quizName) {
        alert("Please enter a quiz name");
        return;
      }
      const newQuiz = new QuizModel(0, questions, "VOCABULARY", quizName);
      const authJson = localStorage.getItem("okta-token-storage");
      const authStateToken = JSON.parse(authJson !== null ? authJson : "");
      // let email = authStateToken.accessToken.claims.sub;
      // newQuiz.owner = email;

      const requestOptions = {
        method: "POST",
        headers: {
          Authorization: `Bearer ${authStateToken?.accessToken?.accessToken}`,
          "Content-Type": "application/json",
        },
        body: JSON.stringify(newQuiz),
      };

      const submitNewQuizResponse = await fetch(url, requestOptions);
      if (!submitNewQuizResponse.ok) {
        throw new Error("Something went wrong!");
      }

      alert("Quiz created successfully!");
      navigate("/home");
    };

    function toggleCorrectAnswerForQuestion(
      answerIndex: number,
      questionIndex: number
    ): void {
      const updatedQuestions = [...questions];
      let updatedQuestion = updatedQuestions[questionIndex];
      updatedQuestion.answers[answerIndex].correct =
        !updatedQuestion.answers[answerIndex].correct;
      setQuestions(updatedQuestions);
    }

    return (
      <Container className="justify-content-md-center">
        <Row className="justify-content-md-center">
          <Col xs lg="auto">
            <div className="quiz-form-div">
              <Row className="justify-content-center">
                <h5>Current Questions</h5>

                {questions.map((question, questionIndex) => (
                  <Col key={questionIndex}>
                    <div key={questionIndex}>
                      <Row className="justify-content-center">
                        <div className="d-flex justify-content-center mb-3">
                          <h5>
                            <strong>Question:</strong> {question.question}
                          </h5>
                        </div>
                      </Row>
                      {question.answers.map((answer, answerIndex) => (
                        <Col className="d-flex" key={answerIndex}>
                          <Row className="justify-content-center">
                            <Col>
                              <strong>Answer:</strong>
                            </Col>
                            <Col>
                              <input
                                type="text"
                                value={answer.answer}
                                onChange={(e) =>
                                  updateAnswerText(
                                    answerIndex,
                                    e.target.value,
                                    questionIndex
                                  )
                                }
                              />
                            </Col>
                          </Row>
                          <Row className="justify-content-center">
                            <Col>
                              <strong>Is Correct: </strong>
                            </Col>
                            <Col>
                              <input
                                type="checkbox"
                                checked={answer.correct}
                                onChange={() =>
                                  toggleCorrectAnswerForQuestion(
                                    answerIndex,
                                    questionIndex
                                  )
                                }
                              />
                            </Col>
                          </Row>

                          <Row className="justify-content-center">
                            <Col>
                              <Button
                                className="btn-delete"
                                onClick={() =>
                                  removeAnswer(answerIndex, questionIndex)
                                }
                              >
                                Remove
                              </Button>
                            </Col>
                          </Row>
                        </Col>
                      ))}
                    </div>
                  </Col>
                ))}
              </Row>
            </div>
          </Col>
        </Row>

        <Row className="justify-content-md-center">
          <Col md="auto">
            <Button onClick={onPrevious}>Previous</Button>
          </Col>

          <Col md="auto">
            <Button onClick={createQuiz}>Create Quiz</Button>
          </Col>
        </Row>
      </Container>
    );
  };

  const FormWizard = () => {
    const [step, setStep] = useState(1);
  };

  return (
    <Container className="justify-content-md-center">
      <br />

      {step === 1 && <FormWizardStep1 onNext={onNext} />}
      {step === 2 && (
        <FormWizardStep2 onNext={onNext} onPrevious={onPrevious} />
      )}
      {step === 3 && (
        <FormWizardStep3 onPrevious={onPrevious} />
      )}
    </Container>
  );
};
