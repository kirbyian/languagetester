import React, { useEffect, useState } from "react";
import { Col, Container, Form, FormControl, Row } from "react-bootstrap";
import Button from "react-bootstrap/Button";
import { useParams } from "react-router-dom";
import ConjugationModel from "../models/ConjugationModel";
import { useForm, SubmitHandler } from "react-hook-form";
import "./Conjugation.css";

export const ConjugationPage = () => {
  const [result, setResult] = useState(0);
  const [verb, setVerb] = useState("");
  const [errors, setErrors] = useState("");
  const [tense, setTense] = useState("");
  const [showResult, setShowResult] = useState(false);
  const [conjugations, setConjugations] = useState<ConjugationModel[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [httpError, setHttpError] = useState(null);
  const [userResponseQuestionMap, setUserResponseQuestionMap] = useState(
    new Map<Number, String>()
  );

  const params = useParams();

  const handleChange = (event: any) => {
    let currentConjugation = conjugations.find(
      (conjugation) => conjugation.id == event.target.id
    );

    if (currentConjugation != undefined) {
      userResponseQuestionMap.set(currentConjugation.id, event.target.value);
    }

    setErrors("");
    setShowResult(false);
  };

  const handleSubmit = (e: any) => {
    setErrors("");
    if (conjugations.length !== userResponseQuestionMap.size) {
      setErrors("Please answer all questions");
      return;
    }

    setShowResult(true);
    let total = 0;
    let numberOfQuestions = 0;
    let correctAnswers = 0;

    userResponseQuestionMap.forEach((answer, conjugationID) => {
      numberOfQuestions++;

      if (
        conjugations
          .find((conjugation) => conjugation.id == conjugationID)
          ?.conjugation.toUpperCase() === answer.toUpperCase()
      ) {
        correctAnswers++;
      }
    }, []);
    setResult(Math.round((correctAnswers / numberOfQuestions) * 100));
  };

  useEffect(() => {
    const fetchTodos = async () => {
      const url: string = `http://localhost:8080/api/conjugations?verbid=${params.verbid}&tenseid=${params.tenseid}`;

      const response = await fetch(url);

      if (response != null && !response.ok) {
        throw new Error("Something went wrong!");
      }

      const responseJson = await response.json();

      const responseData = responseJson;
      const loadedConjugations: ConjugationModel[] = [];

      for (const key in responseData) {
        loadedConjugations.push({
          id: responseJson[key].id,
          conjugation: responseJson[key].conjugation,
          verb: responseJson[key].verb.verb,
          subject: responseJson[key].subject.subject,
          quiz: responseJson[key].quiz.quiz,
          tense: responseJson[key].tense.tense,
        });
      }

      setVerb(loadedConjugations[0].verb);
      setTense(loadedConjugations[0].tense);

      setConjugations(loadedConjugations);
      setIsLoading(false);
    };
    fetchTodos().catch((error: any) => {
      setIsLoading(false);
      setHttpError(error.message);
    });
  }, []);

  if (isLoading) {
    return <h1>Loading</h1>;
  }

  if (httpError) {
    return (
      <div className="container m-5">
        <p>{httpError}</p>
      </div>
    );
  }

  function handleClear(e: any) {
    setErrors("");
    setShowResult(false);
  }

  return (
    <Container>
      <Form id="conjugation-form" className="justify-content-center">
        <h4 className="justify-text-center">
          {verb}: {tense}
        </h4>
        {conjugations.map((conjugation) => (
          <Row className="mb-3 justify-content-center " key={conjugation.id}>
            <Col lg="2">
              <Form.Label
              className="conjugation-form-label"
                column="lg"
                lg="3"
                htmlFor={conjugation.id.toString()}
                key={conjugation.id.toString()}
              >
                {conjugation.subject}
              </Form.Label>
            </Col>
            <Col lg="4">
              <FormControl
                aria-label="Medium"
                aria-describedby="inputGroup-sizing-lg"
                required
                key={conjugation.id.toString()}
                id={conjugation.id.toString()}
                onChange={handleChange}
                type="text"
                name={conjugation.id.toString()}
                className="conjugation-form-input"
              ></FormControl>
            </Col>

            {showResult &&
              userResponseQuestionMap.get(conjugation.id) !== undefined &&
              userResponseQuestionMap.get(conjugation.id)?.toUpperCase() ===
                conjugation.conjugation.toUpperCase() && (
                <Col column="sm" lg="3" className="correct-answer" text>
                  <h4 className="correct-answer">{conjugation.conjugation} </h4>
                </Col>
              )}
            {showResult &&
              userResponseQuestionMap.get(conjugation.id) !== undefined &&
              userResponseQuestionMap.get(conjugation.id)?.toUpperCase() !==
                conjugation.conjugation.toUpperCase() && (
                <Col column="sm" lg="3" className="incorrect-answer">
                  <h4  className="incorrect-answer">{conjugation.conjugation} </h4>
                </Col>
              )}
          </Row>
        ))}

        <Row className="justify-content-center">
          <Col md="auto">
            <Button onClick={handleSubmit} variant="primary">
              Submit
            </Button>
          </Col>
            <br></br>
          <Col md="auto">
            <Button onClick={handleClear} variant="secondary">
              Clear
            </Button>
          </Col>
        </Row>

        {showResult && <h2>Result: {result}%</h2>}
        <div className="text-danger">
          <h2>{errors}</h2>
        </div>
      </Form>
    </Container>
  );
};
