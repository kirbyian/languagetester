import TenseModel from "./TenseModel";

class VerbModel {
  verbid: number;
  verb: string;
  tenses: TenseModel[];

  constructor(verbid: number, verb: string,  tenses: TenseModel[]) {
    this.verb = verb;
    this.verbid = verbid;
    this.tenses = tenses;
  }
}

export default VerbModel;
