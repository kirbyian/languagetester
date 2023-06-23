import React, { useEffect, useState } from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import { Route, Routes, useNavigate } from "react-router-dom";
import { oktaConfig } from "./lib/oktaConfig";
import { LoginCallback, SecureRoute, Security } from "@okta/okta-react";
import { OktaAuth, toRelativeUrl } from "@okta/okta-auth-js";
import LoginWidget from "./Auth/LoginWidget";
import { QuizHome } from "./components/Quiz/QuizHome";
import { NavBar } from "./components/Layouts/NavBar";
import "./App.css";
import { ConjugationHome } from "./components/Conjugation/ConjugationHome";
import { ConjugationPage } from "./components/Conjugation/ConjugationPage";
import { QuizPage } from "./components/Quiz/QuizPage";
import { LanguageAppHome } from "./components/Layouts/LanguageAppHome";
import languageAppImage from "../../assets/images/languageapp.jpg";
import { AdminHome } from "./components/Admin/AdminHome";
import { AdminCreateNewQuiz } from "./components/Admin/Quizzes/AdminCreateNewQuiz";
import { AdminQuizzesHome } from "./components/Admin/Quizzes/AdminQuizzesHome";
import { AdminConjugationHome } from "./components/Admin/Conjugations/AdminConjugationsHome";
import { AdminCreateNewConjugation } from "./components/Admin/Conjugations/AdminCreateNewConjugation";

export const App = () => {
  const oktaAuth = new OktaAuth(oktaConfig);

  const customAuthHandler = () => {
    navigate("/login");
  };

  const navigate = useNavigate();

  const restoreOriginalUri = async (_oktaAuth: any, originalUri: any) => {
    navigate(toRelativeUrl(originalUri || "/", window.location.origin));
  };

  return (
    <Security
      oktaAuth={oktaAuth}
      restoreOriginalUri={restoreOriginalUri}
      onAuthRequired={customAuthHandler}
    >
      <NavBar />
      <Routes>
        <Route
          path="/conjugations/:verbid/:tenseid"
          element={<ConjugationPage />}
        />
        <Route path="/" element={<LanguageAppHome />} />
        <Route path="/home" element={<LanguageAppHome />} />
        <Route path="/quizhome" element={<QuizHome />} />
        <Route path="/quizzes/:id" element={<QuizPage />} />
        <Route path="/conjugations" element={<ConjugationHome />} />
        <Route
          path="/login"
          element={<LoginWidget config={oktaConfig}></LoginWidget>}
        />
        <Route path="/login/callback" element={<LoginCallback />} />
        <Route path="/admin" element={<AdminHome />} />
        <Route path="/adminquizhome" element={<AdminQuizzesHome />} />
        <Route path="/admincreatequiz" element={<AdminCreateNewQuiz />} />
        <Route path="/admincreateconjugation" element={<AdminCreateNewConjugation />} />
        <Route path="/adminconjugationshome" element={<AdminConjugationHome />} />
        
        
      </Routes>
    </Security>
  );
};
