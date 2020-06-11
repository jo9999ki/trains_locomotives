DROP TABLE locomotive IF EXISTS;
DROP TABLE function IF EXISTS;

CREATE TABLE locomotive (
  id INTEGER  IDENTITY NOT NULL PRIMARY KEY,
  address INTEGER NOT NULL,
  identification VARCHAR(20) NOT NULL,
  revision DATE
);

CREATE INDEX index_locomotive ON locomotive (address);

INSERT INTO locomotive VALUES (nextval('hibernate_sequence'), 9, '99 5906', '1986-01-01');
INSERT INTO public.locomotive VALUES (nextval('hibernate_sequence'), 60, '99 6001', '1986-01-01');

CREATE TABLE function (
  id INTEGER  IDENTITY NOT NULL PRIMARY KEY,
  dccnumber INTEGER NOT NULL,
  imageurl VARCHAR(255) NOT NULL,
  name VARCHAR(30) NOT NULL,
  locomotive_id INTEGER UNSIGNED NOT NULL,
  FOREIGN KEY (locomotive_id) REFERENCES locomotive(id),
);

INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 15, 'luftpumpe.jpg', 'Luftpumpe', 1);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 12, 'Kohlen.jpg', 'Kohlen schaufeln', 1);