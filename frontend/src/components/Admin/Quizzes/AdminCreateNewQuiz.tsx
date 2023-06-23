import React, { useState } from "react";
import { Col, Container, Row } from "reactstrap";
import { Button, Form } from "react-bootstrap";
import { AdminQuizWizard } from "../AdminQuizWizard";
import { useOktaAuth } from "@okta/okta-react/";

export const AdminCreateNewQuiz = () => {
  const [showAddTodoForm, setShowAddTodoForm] = useState(false);
  const [isLoading, setIsLoading] = useState(true);
  const [httpError, setHttpError] = useState(null);
  const { oktaAuth, authState } = useOktaAuth();

  return (
    <Container className="justify-content-md-center create-new-quiz-container">
         <br></br>
      <Row className="d-flex justify-content-center">
        <Col className="d-flex justify-content-center">
          <Button
            onClick={() => setShowAddTodoForm(!showAddTodoForm)}
            className="btn btn-primary"
          >
            {showAddTodoForm ? "Close Form" : "Create New Quiz"}
          </Button>
        </Col>
      </Row>

      {showAddTodoForm && <AdminQuizWizard />}
    </Container>
  );
};
