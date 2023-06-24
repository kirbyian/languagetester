import React, { useEffect, useState } from "react";
import { Nav, NavLink, Row } from "reactstrap";
import { Link } from "react-router-dom";
import { Col, Table } from "react-bootstrap";
import VerbModel from "../../models/VerbModel";
import Pagination from "../../Layouts/Pagination";
import { VerbTable } from "../../Conjugation/VerbTable";

export const AdminConjugationHome = () => {
  const [verbs, setVerbs] = useState<VerbModel[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [httpError, setHttpError] = useState(null);
    // User is currently on this page
    const [currentPage, setCurrentPage] = useState(1);
    // To hold the actual data
    const [recordsPerPage] = useState(5);
    const indexOfLastRecord = currentPage * recordsPerPage;
    const indexOfFirstRecord = indexOfLastRecord - recordsPerPage;
    const nPages = Math.ceil(verbs.length / recordsPerPage)
  
    const authJson = localStorage.getItem("okta-token-storage");
    const authStateToken = JSON.parse(authJson !== null ? authJson : "");
    const currentRecords = verbs.slice(indexOfFirstRecord, indexOfLastRecord);

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
    <div>
      <div className="container text-center">
        <h2 className="text-start">Conjugation Tests</h2>
       <VerbTable verbs={currentRecords} isAdmin={true} />
        <Pagination
        nPages={nPages}
        currentPage={currentPage}
        setCurrentPage={setCurrentPage}
      />
      </div>
    </div>
  );
};
