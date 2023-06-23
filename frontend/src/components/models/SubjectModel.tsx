class SubjectModel {
  id: number;
  subject?: string;

  constructor(id: number, subject: string) {
    this.subject = subject;
    this.id = id;
  }
}

export default SubjectModel;
