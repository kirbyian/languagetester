class AnswerModel {
  id: number;
  lastUpdated?: string;
  version?: number;
  answer: string;
  correct: boolean;

  constructor(
    id: number,
    lastUpdated: string,
    answer: string,
    correct: boolean
  ) {
    this.answer = answer;
    this.correct = correct;
    this.lastUpdated = lastUpdated;
    this.id = id;
    this.version = this.version;
  }
}

export default AnswerModel;
