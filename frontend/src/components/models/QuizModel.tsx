import Question from "./QuestionModel";

class Quiz {
  id: number;
  lastUpdated?: string;
  version?: number;
  questions?: Question[] = new Array();
  quizType: string;
  name: string;
  verb?: string;
  tense?: string;
  owner?: string;

  constructor(
    id: number,
    questions: Question[],
    quizType: string,
    name: string
  ) {
    this.questions = questions;
    this.id = id;
    this.quizType = quizType;
    this.name = name;
  }
}

export default Quiz;
