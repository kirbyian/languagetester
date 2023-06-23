import React from 'react'
import { Col, Row, Table } from 'react-bootstrap'
import { Link } from 'react-router-dom'
import Quiz from '../../models/QuizModel'

export const AdminQuizzesTable = (props: { currentRecords: Quiz[] } ) => {
  return (
    <div className="mt-5 container text-center">
    <ul className="navbar-nav ms-auto"></ul>
    <Table striped bordered hover>
      <thead>
        <tr>
          <th>Quizzes</th>
        </tr>
      </thead>
      <tbody>
        {props.currentRecords.map((quiz:Quiz) => (
          <tr key={quiz.id}>
            <td>
              <ul className="navbar-nav me-auto mb-2 mb-lg-0">
                <li key={quiz.id} className="nav-item">
                  <Row>
                    <Col>
                      <span>{quiz.name} </span>
                    </Col>

                    <Col>
                      <Link
                        key={quiz.id}
                        aria-current="page"
                        to={`/quizzes/${quiz.id}`}
                      >
                        {" "}
                        View{" "}
                      </Link>
                    </Col>

                    <Col>
                      <Link
                        key={quiz.id}
                        aria-current="page"
                        to={`/quizzes/edit/${quiz.id}`}
                      >
                        Edit
                      </Link>
                    </Col>
                  </Row>
                </li>
              </ul>
            </td>
          </tr>
        ))}
      </tbody>
    </Table>
    </div>
  )
}
