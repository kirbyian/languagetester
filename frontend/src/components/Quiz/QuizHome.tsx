import React, {useEffect, useState } from "react";
import { Container, Table } from "react-bootstrap";
import Quiz from "../models/QuizModel";
import { Link } from "react-router-dom";
import Pagination from "../Layouts/Pagination";
import "./QuizHome.css"; 

export const QuizHome = () => {
  const [quizzes, setQuizzes] = useState<Quiz[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [httpError, setHttpError] = useState(null);


  // To hold the actual data
  const [currentPage, setCurrentPage] = useState(1);
  const [loading, setLoading] = useState(true);
  const [recordsPerPage] = useState(5);

  const indexOfLastRecord = currentPage * recordsPerPage;
  const indexOfFirstRecord = indexOfLastRecord - recordsPerPage;
  const currentRecords = quizzes.slice(indexOfFirstRecord, indexOfLastRecord);
  const nPages = Math.ceil(quizzes.length / recordsPerPage)

  const base_url = `${process.env.REACT_APP_SPRING_BASE_URL}${process.env.REACT_APP_SPRING_PORT}`;

  useEffect(() => {
    const fetchTodos = async () => {
      const url: string = `${base_url}/api/quizzes/all`;

      const response = await fetch(url);

      if (response != null && !response.ok) {
        throw new Error("Something went wrong!");
      }

      const responseJson = await response.json();

      const responseData = responseJson;

      const loadedQuizzes: Quiz[] = [];

      for (const key in responseData) {
        loadedQuizzes.push({
          id: responseData[key].id,
          lastUpdated: responseData[key].lastUpdated,
          version: responseData[key].version,
          questions: responseData[key].questions,
          name: responseData[key].name,
          quizType: responseData[key].quizType,
        });
      }

      setQuizzes(loadedQuizzes);
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

  return (
      <Container className="text-center">
      <h4>Quizzes</h4>
        <Table striped bordered hover>
          <thead>
            <tr>
              <th>Quiz Name</th>
            </tr>
          </thead>
          <tbody>
            {currentRecords.map((quiz) => (
              <tr key={quiz.id}>
                <td>
                  <ul className="navbar-nav me-auto mb-2 mb-lg-0">
                    <li key={quiz.id} className="nav-item">
                      <Link
                        key={quiz.id}
                        className="nav-link nav-link-custom"
                        aria-current="page"
                        to={`/quizzes/${quiz.id}`}
                      >
                        {quiz.name}{" "}
                      </Link>
                    </li>
                  </ul>
                </td>
              </tr>
            ))}
          </tbody>
        </Table>
        <Pagination
        nPages={nPages}
        currentPage={currentPage}
        setCurrentPage={setCurrentPage}
      />
      </Container>
  );
};
