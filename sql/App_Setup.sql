set schema 'languageapp'

-- verbs definition

-- Drop table

-- DROP TABLE verbs;

CREATE TABLE verbs (
	id serial4 NOT NULL,
	last_updated timestamp NULL,
	"version" int4 NULL,
	verb varchar(100) NULL,
	CONSTRAINT verbs_pkey PRIMARY KEY (id)
);


-- tenses definition

-- Drop table

-- DROP TABLE tenses;

CREATE TABLE tenses (
	id serial4 NOT NULL,
	last_updated timestamp NULL,
	"version" int4 NULL,
	tense varchar(100) NULL,
	CONSTRAINT tenses_pkey PRIMARY KEY (id)
);

-- tense_verbs definition

-- Drop table

-- DROP TABLE tense_verbs;

CREATE TABLE tense_verbs (
	verb_id int4 NOT NULL,
	tense_id int4 NOT NULL,
	CONSTRAINT tense_verbs_pkey PRIMARY KEY (verb_id, tense_id)
);


-- tense_verbs foreign keys

ALTER TABLE tense_verbs ADD CONSTRAINT tense_verbs_tense_id_fk FOREIGN KEY (tense_id) REFERENCES tenses(id);
ALTER TABLE tense_verbs ADD CONSTRAINT tense_verbs_verb_id_fk FOREIGN KEY (verb_id) REFERENCES verbs(id);


-- languageapp.subjects definition

-- Drop table

-- DROP TABLE languageapp.subjects;

CREATE TABLE subjects (
	id serial4 NOT NULL,
	last_updated timestamp NULL,
	"version" int4 NULL,
	subject varchar(50) NULL,
	CONSTRAINT subjects_pkey PRIMARY KEY (id)
);


-- languageapp.quizzes definition

-- Drop table

-- DROP TABLE languageapp.quizzes;

CREATE TABLE languageapp.quizzes (
	id serial4 NOT NULL,
	last_updated timestamp NULL,
	"version" int4 NULL,
	"name" varchar(100) NULL,
	quiz_type varchar(100) NOT NULL,
	"owner" varchar(100) NULL,
	CONSTRAINT quizzes_pkey PRIMARY KEY (id)
);

-- languageapp.questions definition

-- Drop table

-- DROP TABLE languageapp.questions;

CREATE TABLE languageapp.questions (
	id serial4 NOT NULL,
	last_updated timestamp NULL,
	"version" int4 NULL,
	question varchar(100) NOT NULL,
	quizid int4 NOT NULL,
	CONSTRAINT questions_pkey PRIMARY KEY (id)
);


-- languageapp.questions foreign keys

ALTER TABLE languageapp.questions ADD CONSTRAINT questions_quizid_fkey FOREIGN KEY (quizid) REFERENCES languageapp.quizzes(id);

-- languageapp.conjugations definition

-- Drop table

-- DROP TABLE languageapp.conjugations;

CREATE TABLE languageapp.conjugations (
	id serial4 NOT NULL,
	last_updated timestamp NULL,
	"version" int4 NULL,
	conjugation varchar(50) NULL,
	subjectid int4 NOT NULL,
	tenseid int4 NOT NULL,
	verbid int4 NOT NULL,
	quizid int4 NOT NULL,
	"owner" varchar(100) NULL,
	CONSTRAINT conjugations_pkey PRIMARY KEY (id)
);


-- languageapp.conjugations foreign keys

ALTER TABLE languageapp.conjugations ADD CONSTRAINT conjugations_subject_id_fk FOREIGN KEY (subjectid) REFERENCES languageapp.subjects(id);
ALTER TABLE languageapp.conjugations ADD CONSTRAINT conjugations_tense_id_fk FOREIGN KEY (tenseid) REFERENCES languageapp.tenses(id);

-- languageapp.answers definition

-- Drop table

-- DROP TABLE languageapp.answers;

CREATE TABLE languageapp.answers (
	id serial4 NOT NULL,
	last_updated timestamp NULL,
	"version" int4 NULL,
	answer varchar(100) NOT NULL,
	is_correct bool NULL,
	question_id int4 NOT NULL,
	CONSTRAINT answers_pkey PRIMARY KEY (id)
);


-- languageapp.answers foreign keys

ALTER TABLE languageapp.answers ADD CONSTRAINT answers_question_id_fk FOREIGN KEY (question_id) REFERENCES languageapp.questions(id);




INSERT INTO languageapp.verbs (last_updated,"version",verb) VALUES
	 ('2023-04-16 18:22:09.000',1,'Estar'),
	 ('2023-04-16 18:22:09.000',0,'Ser'),
	 ('2023-04-16 18:22:09.000',0,'Falar'),
	 ('2023-04-16 18:22:09.000',1,'Comer'),
	 ('2023-04-16 18:22:09.000',0,'Partir'),
	 (NULL,1,'Ir'),
	 (NULL,0,'Ter'),
	 (NULL,0,'Jantar'),
	 (NULL,1,'Haver'),
	 (NULL,1,'Chegar');
INSERT INTO languageapp.verbs (last_updated,"version",verb) VALUES
	 (NULL,1,'Beber');
	
	

INSERT INTO languageapp.tenses (last_updated,"version",tense) VALUES
	 ('2023-04-16 18:22:09.000',0,'Presente'),
	 ('2023-04-16 18:22:09.000',0,'Pretérito Perfeito'),
	 ('2023-04-16 18:22:09.000',0,'Pretérito Imperfeito'),
	 ('2023-04-16 18:22:09.000',0,'Condicional'),
	 ('2023-04-16 18:22:09.000',0,'Futuro do Presente');

INSERT INTO languageapp.tense_verbs (verb_id,tense_id) VALUES
	 (9,1),
	 (7,1),
	 (1,1),
	 (3,1),
	 (4,1),
	 (5,1),
	 (3,2),
	 (3,3),
	 (4,3);
	
	
INSERT INTO languageapp.subjects (last_updated,"version",subject) VALUES
	 ('2023-04-16 18:22:09.000',0,'Eu'),
	 ('2023-04-16 18:22:09.000',0,'Você'),
	 ('2023-04-16 18:22:09.000',0,'Ele/Ela'),
	 ('2023-04-16 18:22:09.000',0,'Nós'),
	 ('2023-04-16 18:22:09.000',0,'Vocês'),
	 ('2023-04-16 18:22:09.000',0,'Eles/Elas');

	
	
	
	INSERT INTO languageapp.quizzes (last_updated,"version","name",quiz_type,"owner") VALUES
	 ('2023-04-16 18:22:09.000',0,'Quiz1','VOCABULARY','iankirby1991@hotmail.com'),
	 ('2023-04-16 18:22:09.000',0,'Falar: Presente','CONJUGATION',NULL),
	 ('2023-04-16 18:22:09.000',0,'Comer:Presente','CONJUGATION',NULL),
	 ('2023-04-16 18:22:09.000',0,'Partir:Presente','CONJUGATION',NULL),
	 ('2023-04-16 18:22:09.000',0,'Falar-Pretérito Perfeito','CONJUGATION',NULL),
	 (NULL,0,'Chegar:Presente','CONJUGATION',NULL),
	 (NULL,0,'Vestimentos','VOCABULARY','iankirby1991@hotmail.com'),
	 (NULL,0,'Falar:Pretérito Imperfeito','CONJUGATION',NULL),
	 (NULL,0,'Comer:Pretérito Imperfeito','CONJUGATION',NULL),
	 (NULL,0,'Ir:Presente','CONJUGATION',NULL);
INSERT INTO languageapp.quizzes (last_updated,"version","name",quiz_type,"owner") VALUES
	 (NULL,0,'Haver:Presente','CONJUGATION',NULL),
	 (NULL,0,'Estar:Presente','CONJUGATION',NULL),
	 (NULL,0,'Quiz 1','VOCABULARY','iankirby1991@hotmail.com'),
	 (NULL,0,'Beber:Presente','CONJUGATION',NULL),
	 (NULL,0,'Beber:Presente','CONJUGATION',NULL);

INSERT INTO languageapp.questions (last_updated,"version",question,quizid) VALUES
	 ('2023-04-16 18:22:09.000',1,'What is the word for Hat in Portuguese?',1),
	 ('2023-04-16 18:22:09.000',0,'What is the word for Chicken in Portuguese?',1),
	 ('2023-04-16 18:22:09.000',0,'What is the word for "to walk" in Portuguese?',1),
	 (NULL,0,'Dress',14),
	 (NULL,0,'Hat',14),
	 (NULL,0,'Belt',14);

	
INSERT INTO languageapp.conjugations (last_updated,"version",conjugation,subjectid,tenseid,verbid,quizid,"owner") VALUES
	 ('2023-04-16 18:22:09.000',0,'Falo',1,1,3,2,NULL),
	 ('2023-04-16 18:22:09.000',0,'Fala',2,1,3,2,NULL),
	 ('2023-04-16 18:22:09.000',0,'Fala',3,1,3,2,NULL),
	 ('2023-04-16 18:22:09.000',0,'Falamos',4,1,3,2,NULL),
	 ('2023-04-16 18:22:09.000',0,'Falam',5,1,3,2,NULL),
	 ('2023-04-16 18:22:09.000',0,'Falam',6,1,3,2,NULL),
	 ('2023-04-16 18:22:09.000',0,'Como',1,1,4,4,NULL),
	 ('2023-04-16 18:22:09.000',0,'Come',2,1,4,4,NULL),
	 ('2023-04-16 18:22:09.000',0,'Comemos',3,1,4,4,NULL),
	 ('2023-04-16 18:22:09.000',0,'Comem',4,1,4,4,NULL);
INSERT INTO languageapp.conjugations (last_updated,"version",conjugation,subjectid,tenseid,verbid,quizid,"owner") VALUES
	 ('2023-04-16 18:22:09.000',0,'Comem',5,1,4,4,NULL),
	 ('2023-04-16 18:22:09.000',0,'Parto',1,1,5,6,NULL),
	 ('2023-04-16 18:22:09.000',0,'Parte',2,1,5,6,NULL),
	 ('2023-04-16 18:22:09.000',0,'Partimos',3,1,5,6,NULL),
	 ('2023-04-16 18:22:09.000',0,'Partem',4,1,5,6,NULL),
	 ('2023-04-16 18:22:09.000',0,'Partem',5,1,5,6,NULL),
	 (NULL,0,'falei',1,2,3,7,NULL),
	 (NULL,0,'falou',2,2,3,7,NULL),
	 (NULL,0,'falou',3,2,3,7,NULL),
	 (NULL,0,'falámos',4,2,3,7,NULL);
INSERT INTO languageapp.conjugations (last_updated,"version",conjugation,subjectid,tenseid,verbid,quizid,"owner") VALUES
	 (NULL,0,'falaram',5,2,3,7,NULL),
	 (NULL,0,'falaram',6,2,3,7,NULL),
	 (NULL,0,'falava',1,3,3,15,NULL),
	 (NULL,0,'falava',2,3,3,15,NULL),
	 (NULL,0,'falava',3,3,3,15,NULL),
	 (NULL,0,'falávamos',4,3,3,15,NULL),
	 (NULL,0,'falavam',5,3,3,15,NULL),
	 (NULL,0,'falavam',6,3,3,15,NULL),
	 (NULL,0,'comia',1,3,4,16,NULL),
	 (NULL,0,'comia',2,3,4,16,NULL);
INSERT INTO languageapp.conjugations (last_updated,"version",conjugation,subjectid,tenseid,verbid,quizid,"owner") VALUES
	 (NULL,0,'comia',3,3,4,16,NULL),
	 (NULL,0,'comíamos',4,3,4,16,NULL),
	 (NULL,0,'comiam',5,3,4,16,NULL),
	 (NULL,0,'comiam',6,3,4,16,NULL),
	 (NULL,0,'vou',1,1,8,17,NULL),
	 (NULL,0,'vai',2,1,8,17,NULL),
	 (NULL,0,'vai',3,1,8,17,NULL),
	 (NULL,0,'vamos',4,1,8,17,NULL),
	 (NULL,0,'vão',5,1,8,17,NULL),
	 (NULL,0,'vão',6,1,8,17,NULL);
INSERT INTO languageapp.conjugations (last_updated,"version",conjugation,subjectid,tenseid,verbid,quizid,"owner") VALUES
	 (NULL,0,'chego',1,1,9,23,NULL),
	 (NULL,0,'chega',2,1,9,23,NULL),
	 (NULL,0,'chega',3,1,9,23,NULL),
	 (NULL,0,'chegamos',4,1,9,23,NULL),
	 (NULL,0,'chegam',5,1,9,23,NULL),
	 (NULL,0,'chegam',6,1,9,23,NULL),
	 (NULL,0,'bebo',1,1,7,29,NULL),
	 (NULL,0,'Bebe',2,1,7,29,NULL),
	 (NULL,0,'Bebe',3,1,7,29,NULL),
	 (NULL,0,'Bebemos ',4,1,7,29,NULL);
INSERT INTO languageapp.conjugations (last_updated,"version",conjugation,subjectid,tenseid,verbid,quizid,"owner") VALUES
	 (NULL,0,'hei',1,1,13,18,NULL),
	 (NULL,0,'há',2,1,13,18,NULL),
	 (NULL,0,'há',3,1,13,18,NULL),
	 (NULL,0,'havemos',4,1,13,18,NULL),
	 (NULL,0,'hão',5,1,13,18,NULL),
	 (NULL,0,'hão',6,1,13,18,NULL),
	 (NULL,0,'estou',1,1,1,19,NULL),
	 (NULL,0,'está',2,1,1,19,NULL),
	 (NULL,0,'está',3,1,1,19,NULL),
	 (NULL,0,'estamos',4,1,1,19,NULL);
INSERT INTO languageapp.conjugations (last_updated,"version",conjugation,subjectid,tenseid,verbid,quizid,"owner") VALUES
	 (NULL,0,'estão',5,1,1,19,NULL),
	 (NULL,0,'estão',6,1,1,19,NULL),
	 (NULL,0,'Bebem',5,1,7,29,NULL),
	 (NULL,0,'Bebem',6,1,7,29,NULL);


INSERT INTO languageapp.answers (last_updated,"version",answer,is_correct,question_id) VALUES
	 ('2023-04-16 18:22:09.000',1,'Chapeu',true,1),
	 ('2023-04-16 18:22:09.000',1,'Chapel',false,1),
	 ('2023-04-16 18:22:09.000',1,'Chavel',false,1),
	 ('2023-04-16 18:22:09.000',1,'Frango',true,2),
	 ('2023-04-16 18:22:09.000',1,'Pollo',false,2),
	 ('2023-04-16 18:22:09.000',1,'Framboesa',false,2),
	 ('2023-04-16 18:22:09.000',1,'Carne de Vaca',false,2),
	 ('2023-04-16 18:22:09.000',1,'Trabalhar',false,3),
	 ('2023-04-16 18:22:09.000',1,'Andar',true,3),
	 ('2023-04-16 18:22:09.000',1,'Comprar',false,3);
INSERT INTO languageapp.answers (last_updated,"version",answer,is_correct,question_id) VALUES
	 (NULL,0,'vestido',true,4),
	 (NULL,0,'chapeu',false,4),
	 (NULL,0,'chapeu',true,5),
	 (NULL,0,'Saia',false,5),
	 (NULL,0,'cinto',true,6),
	 (NULL,0,'calça',false,6);


ALTER TABLE languageapp.questions
ADD CONSTRAINT fk_questions_quiz_id
FOREIGN KEY (quizid) REFERENCES languageapp.quizzes (id) ON DELETE CASCADE;

CREATE TABLE languages (
	id serial4 NOT NULL,
	last_updated timestamp NULL,
	"version" int4 NULL,
	name varchar(100) NULL,
	code varchar(3) NULL,
	CONSTRAINT languages_pkey PRIMARY KEY (id)
);

ALTER TABLE languageapp.quizzes

--Conjugation indexes
CREATE INDEX idx_tenseid
ON conjugations (tenseid);

CREATE INDEX idx_subjectid
ON conjugations (subjectid);

CREATE INDEX idx_verbid
ON conjugations (verbid);

CREATE INDEX idx_quizid
ON conjugations (quizid);

--Answer indexes
CREATE INDEX idx_question_id
ON answers (question_id);

--Languages indexes
CREATE INDEX idx_code
ON languages (code);

--Questions indexes
CREATE INDEX idx_questions_quizid
ON questions (quizid);

--Quizzes indexes
CREATE INDEX idx_quizzes_quiz_type
ON quizzes (quiz_type);

CREATE INDEX idx_quizzes_languageid
ON quizzes (languageid);

--Subject indexes
CREATE INDEX idx_subjects_subject
ON subjects (subject);

CREATE INDEX idx_subjects_languageid
ON subjects (languageid);

--Tense indexes
CREATE INDEX idx_tenses_tense
ON tenses (tense);

CREATE INDEX idx_tenses_languageid
ON tenses (languageid);

--Verb indexes
CREATE INDEX idx_verbs_verb
ON verbs (verb);

CREATE INDEX idx_verbs_languageid
ON verbs (languageid);

