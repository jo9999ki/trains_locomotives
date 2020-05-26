DROP TABLE locomotive IF EXISTS;

CREATE TABLE locomotive (
  id INTEGER  IDENTITY NOT NULL PRIMARY KEY,
  address INTEGER NOT NULL,
  identification VARCHAR(20) NOT NULL,
  revision DATE
);

CREATE INDEX index_locomotive ON locomotive (address);

INSERT INTO locomotive VALUES (0, 1, '99 5906', '1986-01-01');