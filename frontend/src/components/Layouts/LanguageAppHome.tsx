import React from "react";
import { Button, Col, Container, Row } from "react-bootstrap";
import { Link } from "react-router-dom";
import backgroundImg from "../../assets/images/language-portuguese.jpeg";
import { Jumbotron } from "reactstrap";

export const LanguageAppHome = () => {
  return (
    <div
      className="home-page app-home"
      style={{ backgroundImage: `url(${backgroundImg})` }}
    >
      <Container className="app-home-container">
        <Row className="justify-content-center app-home">
          <Col xs={12} md={6} className="text-center ">
            <h1>Welcome to the Portuguese Quiz App</h1>
            <p>
              Test your knowledge of different languages with our fun quizzes!
            </p>
            <Link to="/quizhome">
              <Button variant="primary" size="lg">
                Find a Quiz
              </Button>
            </Link>
          </Col>
        </Row>
        <Row className="justify-content-center app-home">
          <Col xs={12} md={6} className="text-center">
            <Link to="/conjugations">
              <Button variant="secondary" size="lg">
                Test Conjugations
              </Button>
            </Link>
          </Col>
        </Row>
      </Container>
    </div>
  );
};
