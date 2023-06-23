class ConjugationModel {
  id: number;
  lastUpdated?: string;
  version?: number;
  conjugation: string;
  subject: string;
  verb: string;
  quiz: string;
  tense: string;

  constructor(
    id: number,
    lastUpdated: string,
    version: number,
    conjugation: string,
    subject: string,
    verb: string,
    quiz: string,
    tense: string
  ) {
    this.version = version;
    this.conjugation = conjugation;
    this.lastUpdated = lastUpdated;
    this.id = id;
    this.subject = subject;
    this.verb = verb;
    this.quiz = quiz;
    this.tense = tense;
  }
}

export default ConjugationModel;
