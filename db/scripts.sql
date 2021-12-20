CREATE SCHEMA IF NOT EXISTS bookstore AUTHORIZATION postgres;

SET default_tablespace = 'pg_default';


CREATE SEQUENCE bookstore.author_author_id_seq;

ALTER SEQUENCE bookstore.author_author_id_seq
    OWNER TO postgres;
	

CREATE SEQUENCE bookstore.book_book_id_seq;

ALTER SEQUENCE bookstore.book_book_id_seq
    OWNER TO postgres;


CREATE SEQUENCE bookstore.bookxauthor_author_id_seq;

ALTER SEQUENCE bookstore.bookxauthor_author_id_seq
    OWNER TO postgres;


CREATE SEQUENCE bookstore.bookxauthor_book_id_seq;

ALTER SEQUENCE bookstore.bookxauthor_book_id_seq
    OWNER TO postgres;

	

CREATE TABLE IF NOT EXISTS bookstore.book
(
    book_id bigint NOT NULL DEFAULT nextval('bookstore.book_book_id_seq'::regclass),
    date_issued date,
    pages integer,
    title character varying(255) COLLATE pg_catalog."default",
    ADD COLUMN rec_version bigint DEFAULT 0 NOT NULL,
    CONSTRAINT book_pkey PRIMARY KEY (book_id)
);


CREATE TABLE IF NOT EXISTS bookstore.author
(
    author_id bigint NOT NULL DEFAULT nextval('bookstore.author_author_id_seq'::regclass),
    name character varying(255) COLLATE pg_catalog."default",
    ADD COLUMN rec_version bigint DEFAULT 0 NOT NULL,
    CONSTRAINT author_pkey PRIMARY KEY (author_id),
    CONSTRAINT author_ix_name UNIQUE (name)
);


CREATE TABLE IF NOT EXISTS bookstore.bookxauthor
(
    book_id bigint NOT NULL,
    author_id bigint NOT NULL,
    CONSTRAINT bookxauthor_pkey PRIMARY KEY (book_id, author_id),
    CONSTRAINT bookxauthor_fk1 FOREIGN KEY (book_id)
        REFERENCES bookstore.book (book_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT bookxauthor_fk2 FOREIGN KEY (author_id)
        REFERENCES bookstore.author (author_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);


---------------------------------------------------------------------------------------------------------------------
-- Update for step 16
---------------------------------------------------------------------------------------------------------------------
ALTER TABLE bookstore.book
    ADD COLUMN rec_version bigint DEFAULT 0 NOT NULL
;

ALTER TABLE bookstore.author
    ADD COLUMN rec_version bigint DEFAULT 0 NOT NULL
;
