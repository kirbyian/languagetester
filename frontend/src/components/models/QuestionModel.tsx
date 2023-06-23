import Answer from "./AnswerModel";

class Question {
  id: number;
  lastUpdated?: string;
  version?: number;
  question: string;
  answers: Answer[] = new Array();

  constructor(
    id: number,
    lastUpdated: string,
    question: string,
    answers: Answer[]
  ) {
    this.question = question;
    this.answers = answers;
    this.lastUpdated = lastUpdated;
    this.id = id;
  }
}

export default Question;
