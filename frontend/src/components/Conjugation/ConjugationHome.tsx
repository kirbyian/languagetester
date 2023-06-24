import React, { useEffect, useState } from "react";
import Quiz from "../models/QuizModel";
import { Nav, NavLink } from "reactstrap";
import { Link } from "react-router-dom";
import { Col, Container, Row, Table } from "react-bootstrap";
import VerbModel from "../models/VerbModel";
import { VerbTable } from "./VerbTable";
import Pagination from "../Layouts/Pagination";

export const ConjugationHome = () => {
  const [verbs, setVerbs] = useState<VerbModel[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [httpError, setHttpError] = useState(null);

  // To hold the actual data
  const [loading, setLoading] = useState(true);
  const [recordsPerPage] = useState(4);

  const [currentPage, setCurrentPage] = useState(1);
  const indexOfLastRecord = currentPage * recordsPerPage;
  const indexOfFirstRecord = indexOfLastRecord - recordsPerPage;
  const currentRecords = verbs.slice(indexOfFirstRecord, indexOfLastRecord);
  const nPages = Math.ceil(verbs.length / recordsPerPage)

  useEffect(() => {
    const fetchVerbs = async () => {
      const url: string = "http://localhost:8080/api/conjugations/verbs";

      const response = await fetch(url);

      if (response != null && !response.ok) {
        throw new Error("Something went wrong!");
      }

      const responseJson = await response.json();

      const responseData = responseJson;

      const verbTenseDTOs = [];

      for (const key in responseData) {
        verbTenseDTOs.push({
          verbid: responseData[key].id,
          verb: responseData[key].verb,
          tenses: responseData[key].tenses,
        });
      }

      setVerbs(verbTenseDTOs);
      setIsLoading(false);
    };
    fetchVerbs().catch((error: any) => {
      setIsLoading(false);
      setHttpError(error.message);
    });
  }, []);

  if (isLoading) {
    return <h1>Loading</h1>;
  }

  return (
      <Container className="container text-center">
        <Row>
          <Col>
            <h4 className=" text-center">Verb Tests</h4>
          </Col>
        </Row>
  
        <VerbTable verbs={currentRecords} isAdmin={false} />
        <Pagination
        nPages={nPages}
        currentPage={currentPage}
        setCurrentPage={setCurrentPage}
      />
      </Container>
  );
};
