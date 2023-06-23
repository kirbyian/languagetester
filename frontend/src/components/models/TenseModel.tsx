class TenseModel {
  id?: number;
  tense?: string;

  constructor(id: number, tense: string) {
    this.tense = tense;
    this.id = id;
  }
}

export default TenseModel;
