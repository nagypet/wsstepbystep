execute l ' ';
execute l '--------------------------------------------------------';
execute l '-- TABLES';
execute l '--------------------------------------------------------';

execute l 'Creating table book';
execute ddl '
CREATE TABLE bookstore.book
(
    book_id bigint IDENTITY(1, 1) PRIMARY KEY,
    date_issued date,
    pages integer,
    title character varying(255)
)
';


execute l 'Creating table author';
execute ddl '
CREATE TABLE bookstore.author
(
    author_id bigint IDENTITY(1, 1) PRIMARY KEY,
    name character varying(255)
)
';


execute l 'Creating table bookxauthor';
execute ddl '
CREATE TABLE bookstore.bookxauthor
(
    book_id bigint NOT NULL,
    author_id bigint NOT NULL,
    CONSTRAINT bookxauthor_pkey PRIMARY KEY (book_id, author_id),
    CONSTRAINT bookxauthor_fk1 FOREIGN KEY (book_id)
        REFERENCES bookstore.book (book_id)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT bookxauthor_fk2 FOREIGN KEY (author_id)
        REFERENCES bookstore.author (author_id)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
';


---------------------------------------------------------------------------------------------------------------------
-- Update for step 18
---------------------------------------------------------------------------------------------------------------------
execute l 'Altering table book';
execute ddl '
ALTER TABLE bookstore.book
	ADD 
		createdby character varying(255),
		createdat datetime,
		updatedby character varying(255),
		updatedat datetime
';

execute l 'Altering table author';
execute ddl '
ALTER TABLE bookstore.author
	ADD 
		createdby character varying(255),
		createdat datetime,
		updatedby character varying(255),
		updatedat datetime
';
