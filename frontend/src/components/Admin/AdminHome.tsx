import React, { useEffect, useState } from "react";
import { useOktaAuth } from "@okta/okta-react";
import { NavLink } from "reactstrap";
import { Link } from "react-router-dom";
import { Navbar } from "react-bootstrap";
import "./AdminHome.css"; 


export const AdminHome = () => {
  const [userGroups, setUserGroups] = useState<string[]>([]);
  const { oktaAuth, authState } = useOktaAuth();
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

  return (

    <div>
      <h1>Admin Home</h1>
      
      {isAdmin && (
            <Navbar expand="lg admin-navbar">
             <Navbar.Brand>
                <Link
                  className="nav-link nav-link-custom"
                  aria-current="page"
                  to="/admincreatequiz"
                >
                  Create New Quiz{" "}
                </Link>
                </Navbar.Brand>
                <Navbar.Brand>
                <Link
                  className="nav-link nav-link-custom"
                  aria-current="page"
                  to="/admincreateconjugation"
                >
                  Create New Conjugation{" "}
                </Link>
                </Navbar.Brand>
                <Navbar.Brand>
                <Link
                  className="nav-link nav-link-custom"
                  aria-current="page"
                  to="/adminquizhome"
                >
                  Your Quizzes{" "}
                </Link>
                </Navbar.Brand>
                <Navbar.Brand>
                <Link
                  className="nav-link nav-link-custom"
                  aria-current="page"
                  to="/adminconjugationshome"
                >
                  Your Conjugations{" "}
                </Link>
                </Navbar.Brand>
              {isAuthenticated && isAdmin && (
                 <Navbar.Brand>
                  <Link
                    className="nav-link nav-link-custom"
                    aria-current="page"
                    to="/admin"
                  >
                    Admin{" "}
                  </Link>
                  </Navbar.Brand>
              )}
        </Navbar>
      )}

      

      {!isAdmin && (
        <div className="card">
          <h1>You don&apos;t have privledges to access this</h1>
        </div>
      )}
    </div>
  );
};
