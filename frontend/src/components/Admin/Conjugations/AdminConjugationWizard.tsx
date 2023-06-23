import React, { useEffect, useRef, useState } from "react";
import { Col, Container, Input, Row } from "reactstrap";
import { Button } from "react-bootstrap";
import { set } from "react-hook-form";
import { redirect, useNavigate } from "react-router-dom";
import { useOktaAuth } from "@okta/okta-react";
import VerbModel from "../../models/VerbModel";
import TenseModel from "../../models/TenseModel";
import SubjectModel from "../../models/SubjectModel";

export const AdminConjugationWizard = () => {
    const [step, setStep] = useState(1);
    const [isLoading, setIsLoading] = useState(true);
    const [httpError, setHttpError] = useState(null);
    const [verb, setVerb] = useState("");
    const [tenses, setTenses] = useState<TenseModel[]>([]);
    const [subjects, setSubjects] = useState<SubjectModel[]>([]);

    const [verbTenseDTO, setVerbTenseDTO] = useState<VerbModel | undefined>();
    let verbs = useRef<VerbModel[]>([]);
    const verbInputField = useRef<HTMLInputElement>(null);
    const selectedVerbField = useRef<HTMLSelectElement>(null);
    const selectedTenseField = useRef<HTMLSelectElement>(null);
    const subjectConjugationMap = new Map<Number, String>();

    const base_url = `${process.env.REACT_APP_SPRING_BASE_URL}${process.env.REACT_APP_SPRING_PORT}`;

    const navigate = useNavigate();

    const onNext = async () => {
        if (step === 1) {
            validateVerbFields(verbInputField, selectedVerbField);

            let verbApiParam = "";
            let isVerbSelected = false;
            // If the user entered a new verb, set the verb to that value
            if (verbInputField.current && verbInputField.current.value) {
                setVerb(verbInputField.current.value);
                verbApiParam = verbInputField.current.value;
            } else if (selectedVerbField.current && selectedVerbField.current.value) {
                setVerb(selectedVerbField.current.value);
                verbApiParam = selectedVerbField.current.value;
                isVerbSelected = true;
            }
            //  check if verb already exists in database
            // empty tense model list
            let returnedVerb = new VerbModel(0, "", []);
            if (!isVerbSelected) {
                const verbExistsResponse = await fetch(
                    `${base_url}/api/verbs/exists/${verbApiParam}`

                );

                validateAPIResponse(verbExistsResponse);

                const verbExistsJson = await verbExistsResponse.json();

                const responseData = verbExistsJson;

                //  check if verb already exist
                if (responseData === true) {
                    alert("Verb already exists");
                    return;
                } else {
                    // create new verb
                    returnedVerb = await createVerb(verbApiParam);

                }

            } else {
                returnedVerb = await getVerbDetailsByVerbName(verbApiParam);
            }

            let returnedAllTenses = getAllTenses();

            filterTenses(returnedVerb.tenses, await returnedAllTenses);

            setSubjects(await getAllSubjects());

        } else if (step === 2) {


        }

        setStep(step + 1);
    };

    async function getAllTenses(): Promise<TenseModel[]> {
        const url: string = `${base_url}/api/tenses`;

        const response = await fetch(url);

        validateAPIResponse(response);

        const responseJson = await response.json();

        const tensesResponse = responseJson;
        setTenses(tensesResponse);
        setIsLoading(false);
        return tensesResponse;
    }

    const filterTenses = (verbTenses: TenseModel[], allTenses: TenseModel[]) => {
        if (verbTenses == null || verbTenses.length === 0) {
            setTenses(allTenses);
            return;
        }
        const filteredTenses = allTenses.filter(tense =>
            !verbTenses.map(verbTense => verbTense.id).includes(tense.id));
        setTenses(filteredTenses);
    };

    async function createVerb(verb: string): Promise<VerbModel> {
        const url = `${base_url}/api/verbs/${verb}`;

        const authJson = localStorage.getItem("okta-token-storage");
        const authStateToken = JSON.parse(authJson !== null ? authJson : "");

        const requestOptions = {
            method: "POST",
            headers: {
                Authorization: `Bearer ${authStateToken?.accessToken?.accessToken}`,
                "Content-Type": "application/json",
            },
        };

        const createVerbResponse = await fetch(url, requestOptions);
        validateAPIResponse(createVerbResponse);
        let data = await createVerbResponse.json();
        let returnedVerb = new VerbModel(data.id, data.verb, data.tenses);

        alert("Verb created successfully!");
        return returnedVerb;
    }


    const getVerbDetailsByVerbName = async (verb: string): Promise<VerbModel> => {

        const url: string = `${base_url}/api/verbs/${verb}`;

        const response = await fetch(url);

        validateAPIResponse(response);

        const responseJson = await response.json();

        const verbTenseDTO = responseJson;
        verbTenseDTO.verbid = responseJson.id;
        setVerbTenseDTO(verbTenseDTO)
        setIsLoading(false);
        return verbTenseDTO;

    };

    const getAllSubjects = async (): Promise<[SubjectModel]> => {

        const url: string = `${base_url}/api/subjects`;

        const response = await fetch(url);

        validateAPIResponse(response);

        const responseJson = await response.json();

        let subjectModelList = responseJson;
        return subjectModelList;
        ;

    };


    const onPrevious = () => {
        setStep(step - 1);
    };


    const FormWizardStep1 = React.memo((props: { onNext: () => void }) => {
        const { onNext } = props;

        useEffect(() => {
            const fetchConjugations = async () => {
                const url: string = `${base_url}/api/verbs`;

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

                verbs.current = verbTenseDTOs;
                setIsLoading(false);
            };
            fetchConjugations().catch((error: any) => {
                setIsLoading(false);
                setHttpError(error.message);
            });
        }, [verbTenseDTO]);

        return (
            <div>
                <br />
                <Row>
                    <Col className="d-flex justify-content-center">
                        <h3>Create Conjugation Wizard</h3>
                    </Col>
                </Row>
                <br />
                <Row>
                    <Col auto className="d-flex justify-content-center">
                        <label>
                            <h5>Select a verb:</h5>
                            <select ref={selectedVerbField}>
                                <option selected> -- Select an option -- </option>
                                {verbs.current.map((verb) => (
                                    <option key={verb.verbid} value={verb.verb}>
                                        {verb.verb}
                                    </option>
                                ))}
                            </select>
                        </label>
                    </Col>
                </Row>
                <Row>
                    <Col className="d-flex justify-content-center">
                        <h5>or</h5>
                    </Col>
                </Row>
                <Row>
                    <Col className="d-flex justify-content-center">
                        <br />
                        <label>
                            <h5>Enter a new Verb to create</h5>
                            <input type="text" ref={verbInputField} />
                        </label>
                    </Col>
                </Row>

                <Row>
                    <Col className="d-flex justify-content-center">
                        <Button onClick={onNext}>Next</Button>
                    </Col>
                </Row>
            </div>
        );
    });

    const FormWizardStep2 = (props: {
        onNext: () => void;
        onPrevious: () => void;
        verb: string;
    }) => {
        const { onNext, onPrevious } = props;

        const handleChange = (event: any) => {

            console.log(event.target.name);
            console.log(event.target.value);
            subjectConjugationMap.set(Number(event.target.name), event.target.value);
        };

        const convertMapToObject = <K, V>(map: Map<K, V>): Record<string, V> => {
            const obj: Record<string, V> = {};
            for (const [key, value] of map.entries()) {
                obj[String(key)] = value;
            }
            return obj;
        };


        const createConjugationForTenseAndVerb = async () => {

            const url = `${base_url}/api/conjugations?verbID=${verbTenseDTO?.verbid}&tenseID=${selectedTenseField.current?.value}`;

            const authJson = localStorage.getItem("okta-token-storage");
            const authStateToken = JSON.parse(authJson !== null ? authJson : "");

            // need to wrap subjectConjugationMap.entries in an object


            const requestOptions = {
                method: "POST",
                headers: {
                    Authorization: `Bearer ${authStateToken?.accessToken?.accessToken}`,
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(convertMapToObject(subjectConjugationMap)),
            };

            const createConjugationResponse = await fetch(url, requestOptions);
            validateAPIResponse(createConjugationResponse);
            let returnedVerb = createConjugationResponse.json();

            alert("Conjugation created successfully!");
            // return returnedVerb;

        }

        return (
            <Container>
                <Row>
                    <Col className="d-flex justify-content-center">

                        <h2>{verb}</h2>

                    </Col>
                </Row>

                <Row>

                    <Row>
                        <Col auto className="d-flex justify-content-center">
                            <label>
                                <h5>Select a tense:</h5>
                                <select ref={selectedTenseField} >
                                    <option selected> -- Select an option -- </option>
                                    {tenses.map((tense) => (
                                        <option key={tense.id} value={tense.id}>
                                            {tense.tense}
                                        </option>
                                    ))}
                                </select>
                            </label>
                        </Col>
                    </Row>
                    <br />
                    <br />
                    <Row>
                        {subjects.map((subject) => (

                            <Row key={subject.id} className="d-flex justify-content-center">
                                <Col>
                                    <label>
                                        {subject.subject !== undefined ? subject.subject : ""}
                                    </label>
                                </Col>
                                <Col>
                                    <Input onChange={handleChange} type="text" name={subject.id.toString()} />
                                </Col>
                            </Row>
                        ))}
                    </Row>

                    <Button onClick={createConjugationForTenseAndVerb}>Save Conjugation</Button>

                </Row>
            </Container>
        );
    };

    const FormWizardStep3 = (props: {
        onPrevious: () => void;
    }) => {
        const { onPrevious } = props;

        return (
            <Container className="justify-content-md-center">

                <Row className="justify-content-md-center">
                    <Col md="auto">
                        <Button onClick={onPrevious}>Previous</Button>
                    </Col>

                    <Col md="auto"></Col>
                </Row>
            </Container>
        );
    };

    const FormWizard = () => {
        const [step, setStep] = useState(1);
    };

    return (
        <Container className="justify-content-md-center">
            <br />
            {step === 1 && <FormWizardStep1 onNext={onNext} />}
            {step === 2 && <FormWizardStep2 onNext={onNext} onPrevious={onPrevious} verb={verb} />}
            {step === 3 && <FormWizardStep3 onPrevious={onPrevious} />}
        </Container>
    );
};


function validateAPIResponse(response: Response) {
    if (response != null && !response.ok) {
        throw new Error("Something went wrong!");
    }
}

function validateVerbIsOnlySelectedOrEntered(verbInputField: React.RefObject<HTMLInputElement>, selectedVerbField: React.RefObject<HTMLSelectElement>) {
    return verbInputField.current?.value && selectedVerbField.current?.value && verbInputField.current?.value !== selectedVerbField.current?.value
        && selectedVerbField.current?.value !== "-- Select an option --";
}

function validateVerbisSelectedOrEntered(verbInputField: React.RefObject<HTMLInputElement>, selectedVerbField: React.RefObject<HTMLSelectElement>) {
    return !verbInputField.current?.value && !selectedVerbField.current?.value;
}

function validateVerbFields(verbInputField: React.RefObject<HTMLInputElement>, selectedVerbField: React.RefObject<HTMLSelectElement>) {
    if (validateVerbIsOnlySelectedOrEntered(verbInputField, selectedVerbField)) {
        alert("Please select a verb or enter a new verb, not both");
    }
    else if (validateVerbisSelectedOrEntered(verbInputField, selectedVerbField)) {
        alert("Please select a verb or enter a new verb");
    }
    return;
}



