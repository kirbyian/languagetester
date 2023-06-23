import React from 'react'
import { Link } from 'react-router-dom';
import { Table, NavLink } from 'reactstrap';
import TenseModel from '../models/TenseModel';
import VerbModel from '../models/VerbModel';

export const VerbTable = (props: { verbs: VerbModel[],isAdmin:boolean}) => {
    return (
      <Table striped bordered hover>
        <thead>
          <tr></tr>
          <tr>
            <th>Verb</th>
            <th>Tenses</th>
            {props.isAdmin && <th>Actions</th>}
          </tr>
        </thead>
        <tbody>
          {props.verbs.map((verb) => (
            <tr key={verb.verbid}>
              <td>{verb.verb}</td>
              <td>
                {verb.tenses.map((tense) => (
                  <ul key={tense.id} className="navbar-nav me-auto mb-2 mb-lg-0">
                    <li key={`${verb.verbid}-${tense.id}`} className="nav-item">
                      <Link
                        key={verb.verbid}
                        className="nav-link nav-link-custom"
                        aria-current="page"
                        to={`/conjugations/${verb.verbid}/${tense.id}`}
                      >
                        {tense.tense}
                      </Link>
                    </li>
                  </ul>
                ))}
              </td>
              {props.isAdmin && (
                <td>
                  <Link
                    key={`${verb.verbid}-edit`}
                    className="nav-link nav-link-custom"
                    aria-current="page"
                    to={`/conjugations/edit/${verb.verbid}`}
                  >
                    Edit
                  </Link>
                </td>
              )}
            </tr>
          ))}
        </tbody>
      </Table>
    );
  };

