import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { Row, Table } from "react-bootstrap";
import { Col } from "reactstrap";
import { Pagination } from "../../Layouts/Pagination";
import { AdminQuizzesTable } from "../Quizzes/AdminQuizzesTable";
import { set } from "react-hook-form";
import Quiz from "../../models/QuizModel";

export const AdminQuizzesHome = () => {
  const [quizzes, setQuizzes] = useState<Quiz[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [httpError, setHttpError] = useState(null);
  // User is currently on this page
  const [currentPage, setCurrentPage] = useState(1);
  // To hold the actual data
  const [recordsPerPage] = useState(5);
  const indexOfLastRecord = currentPage * recordsPerPage;
  const indexOfFirstRecord = indexOfLastRecord - recordsPerPage;
  const nPages = Math.ceil(quizzes.length / recordsPerPage)

  const authJson = localStorage.getItem("okta-token-storage");
  const authStateToken = JSON.parse(authJson !== null ? authJson : "");
  const currentRecords = quizzes.slice(indexOfFirstRecord, indexOfLastRecord);


  useEffect(() => {
    const fetchQuizzes = async () => {

      const url: string = "http://localhost:8080/api/quizzes/user";

      const requestOptions = {
          method: "GET",
          headers: {
              Authorization: `Bearer ${authStateToken.accessToken?.accessToken}`,
              "Content-Type": "application/json",
          },
      };

      const response = await fetch(url, requestOptions);

      if (response != null && !response.ok) {
          throw new Error("Something went wrong!");
      }

      const responseJson = await response.json();

      const responseData = responseJson;
      const quizList: Quiz[] = [];

          for (const key in responseData) {
              quizList.push(new Quiz(
                  responseData[key].id,
                  responseData[key].questions,
                  responseData[key].quizType,
                  responseData[key].name,
              ));
          }

      setQuizzes(quizList);
      setIsLoading(false);
    };
    fetchQuizzes().catch((error: any) => {
      setIsLoading(false);
      setHttpError(error.message);
    });
  }, []);

  if (isLoading) {
    return <h1>Loading</h1>;
  }

  if (httpError !== null) {
    return (
      <div className="container m-5">
        <p>{httpError}</p>
      </div>
    );
  }

  return (
    <div className="mt-5 container text-center">
      <br />
      <ul className="navbar-nav ms-auto"></ul>
     
      <AdminQuizzesTable currentRecords={currentRecords} />

      <Pagination
        nPages={nPages}
        currentPage={currentPage}
        setCurrentPage={setCurrentPage}
      />
    </div>
  );
};
