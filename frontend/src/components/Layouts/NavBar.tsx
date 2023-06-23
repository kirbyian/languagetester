import { useOktaAuth } from "@okta/okta-react";
import React, { useEffect, useState } from "react";
import { Link, NavLink } from "react-router-dom";
import { Button } from "reactstrap";
import "./NavBar.css";

export const NavBar = () => {
  const { oktaAuth, authState } = useOktaAuth();
  const [userGroups, setUserGroups] = useState<string[]>([]);
  const authJson = localStorage.getItem("okta-token-storage");
  const [isAdmin, setIsAdmin] = useState<boolean>(false);
  const [isAuthenticated, setIsAuthenticated] = useState<boolean>(false);

  useEffect(() => {
    // Get the user's groups
    if (authState?.isAuthenticated && !authState.isPending) {
      setIsAuthenticated(true);

      const groups = new Array<string>();
      const token = oktaAuth.getIdToken;
      const authStateToken = JSON.parse(authJson !== null ? authJson : "");
      authStateToken.idToken.claims?.Groups.forEach((group: string) =>
        groups.push(group)
      );
      setUserGroups(groups);
      if (groups.includes("Admin")) {
        setIsAdmin(true);
      }
    }
  }, [authState]);

  if (!authState) {
    return <div>Loading</div>;
  }

  const handleLogout = async () => oktaAuth.signOut();
  return (
    <nav className="navbar navbar-expand-lg main-color ">
      <div className="container-fluid main-color">
        <a className="navbar-brand nav-link-custom" href="/">
          Home
        </a>
        <button
          className="navbar-toggler nav-toggle"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#navbarSupportedContent"
          aria-controls="navbarSupportedContent"
          aria-expanded="false"
          aria-label="Toggle navigation"
        >
          <span className="navbar-toggler-icon"></span>
        </button>
        <div className="collapse navbar-collapse" id="navbarSupportedContent">
          <ul className="navbar-nav me-auto mb-2 mb-lg-0">
            <li className="nav-item">
              <NavLink
                className="nav-link nav-link-custom"
                aria-current="page"
                to="/quizhome"
              >
                Quizzes{" "}
              </NavLink>
            </li>
            <li className="nav-item">
              <NavLink
                className="nav-link nav-link-custom"
                aria-current="page"
                to="/conjugations"
              >
                Conjugations{" "}
              </NavLink>
            </li>
            {isAuthenticated && isAdmin && (
              <li className="nav-item">
                <NavLink
                  className="nav-link nav-link-custom"
                  aria-current="page"
                  to="/admin"
                >
                  Admin{" "}
                </NavLink>
              </li>
            )}
          </ul>
          {!isAuthenticated && (
            <Button className="sign-in-btn">
              <NavLink className="nav-link" to="/login">
                Sign In
              </NavLink>
            </Button>
          )}
          {isAuthenticated && (
            <Button className="sign-in-btn" onClick={handleLogout}>
              <NavLink className="nav-link" to="/login">
                Sign Out
              </NavLink>
            </Button>
          )}
        </div>
      </div>
    </nav>
  );
};
