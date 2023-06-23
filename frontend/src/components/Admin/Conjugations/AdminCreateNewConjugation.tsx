import { useOktaAuth } from '@okta/okta-react';
import React, { useState } from 'react'
import { Container, Row, Col, Button } from 'reactstrap';
import { AdminConjugationWizard } from './AdminConjugationWizard';

export const AdminCreateNewConjugation = () => {
  const [showAddConjugationForm, setShowAddConjugationForm] = useState(false);
  const { oktaAuth, authState } = useOktaAuth();

  return (
    <Container className="justify-content-md-center create-new-quiz-container">
      <br />
      <Row>
        <Col className="d-flex justify-content-center">
          <Button
            onClick={() => setShowAddConjugationForm(!showAddConjugationForm)}
            className="btn btn-primary"
          >
            {showAddConjugationForm ? "Close Form" : "Create New Conjugation"}
          </Button>
        </Col>
      </Row>

      {showAddConjugationForm && <AdminConjugationWizard />}
    </Container>
  );
};
