DROP TABLE public.function;
DROP TABLE public.locomotive;

DROP SEQUENCE public.hibernate_sequence;

CREATE SEQUENCE public.hibernate_sequence;

CREATE TABLE public.locomotive
(
    id bigint NOT NULL,
    address integer NOT NULL,
    identification character varying(20) COLLATE pg_catalog."default" NOT NULL,
    revision date,
    CONSTRAINT locomotive_pkey PRIMARY KEY (id),
    CONSTRAINT locomotive_address_check CHECK (address >= 0 AND address <= 9999)
)

TABLESPACE pg_default;

ALTER TABLE public.locomotive
    OWNER to dcc;

-- DROP INDEX public.index_locomotive;

CREATE INDEX index_locomotive
    ON public.locomotive USING btree
     (address)
     TABLESPACE pg_default;

INSERT INTO public.locomotive VALUES (nextval('hibernate_sequence'), 9, '99 5906', '2010-04-20');
INSERT INTO public.locomotive VALUES (nextval('hibernate_sequence'), 60, '99 6001', '1967-03-06');
INSERT INTO public.locomotive VALUES (nextval('hibernate_sequence'), 64, '99 1564', '1973-10-16');
INSERT INTO public.locomotive VALUES (nextval('hibernate_sequence'), 68, '99 1568', '1973-10-16');
INSERT INTO public.locomotive VALUES (nextval('hibernate_sequence'), 93, '99 193', '1963-12-19');

CREATE TABLE public.function
(
    id bigint NOT NULL,
    dccnumber integer NOT NULL,
    imageurl character varying(255) COLLATE pg_catalog."default",
    name character varying(30) COLLATE pg_catalog."default" NOT NULL,
    locomotive_id bigint,
    CONSTRAINT function_pkey PRIMARY KEY (id),
    CONSTRAINT fk8prh5cnaguci9o3n3pxfp4pmq FOREIGN KEY (locomotive_id)
        REFERENCES public.locomotive (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE public.function
    OWNER to dcc;

INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 0, 'licht.jpg', 'Licht vorne', 1);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 1, 'sound.jpg', 'Sound EIN/AUS', 1);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 2, 'pfeife_lang.jpg', 'Pfeife lang', 1);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 3, 'pfeife_kurz.jpg', 'Pfeife kurz', 1);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 4, 'glocke.jpg', 'Glocke', 1);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 5, 'kohle.jpg', 'Licht Triebwerk', 1);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 6, 'rangieren.jpg', 'Rangiergang', 1);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 7, 'rauch.jpg', 'Rauchgenerator', 1);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 8, 'licht.jpg', 'Licht Führerstand', 1);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 9, 'sound.jpg', 'Sound EIN/AUS', 1);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 10, 'zylinder_blasen.jpg', 'Schaffnerpfiff', 1);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 11, 'luftpumpe.jpg', 'Druckluftpunpe', 1);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 12, 'wasserpunpe.jpg', 'Wasserpumpe', 1);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 13, 'luftpumpe.jpg', 'Druckluftpunpe', 1);

-- locomotive id 2, 99 6001
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 0, 'licht.jpg', 'Licht vorne', 2);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 1, 'sound.jpg', 'Sound EIN/AUS', 2);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 2, 'pfeife_lang.jpg', 'Pfeife lang', 2);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 3, 'glocke.jpg', 'Glocke', 2);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 4, 'pfeife_kurz.jpg', 'Pfeife kurz', 2);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 5, 'licht.jpg', 'Licht Triebwerk', 2);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 6, 'rangieren.jpg', 'Rangiergang', 2);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 7, 'rauch.jpg', 'Rauchgenerator', 2);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 8, 'licht.jpg', 'Licht Führerstand', 2);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 9, 'kurve.jpg', 'Kurvenquietschen', 2);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 10, 'ansage.jpg', 'Schaffnerpfiff', 2);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 11, 'kohle.jpg', 'Kohle schaufeln', 2);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 12, 'luftpumpe.jpg', 'Druckluftpumpe', 2);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 13, 'abschlammen.jpg', 'Abschlammen', 2);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 14, 'licht.jpg', 'Rotes Rücklicht', 2);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 15, 'wasserpumpe', 'Injektor', 2);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 16, 'bremse.jpg', 'Bremse anlegen (lösen autom.)', 2);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 17, 'licht.jpg', 'Schienenstöße', 2);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 18, 'sand.jpg', 'Sanden', 2);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 19, 'kuppeln.jpg', 'Kuppeln', 2);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 20, 'Ansage.jpg', 'nach Wernigeroder einsteigen', 2);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 21, 'Ansage.jpg', 'Ankunft Eisfelder Talmühle', 2);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 22, 'Ansage.jpg', 'nach Nordhausen einsteigen', 2);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 23, 'lautstaerke.jpg', 'Lautstärke Regelung', 2);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 24, 'soundfader.jpg', 'Soundfader', 2);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 25, 'zylinder_blasen.jpg', 'Zylinder ausblasen', 3);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 26, 'ueberdruck.jpg', 'Sicherheitsventil', 3);

-- locomotive id 3, 99 1564
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 0, 'licht.jpg', 'Licht vorne', 3);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 1, 'pfeife_lang.jpg', 'Pfeife lang', 3);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 2, 'pfeife_kurz.jpg', 'Pfeife kurz', 3);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 3, 'ansage.jpg', 'Ansage Abfahrt', 3);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 4, 'glocke.jpg', 'Glocke', 3);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 5, 'kupplung.jpg', 'Kupplung hinten', 3);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 6, 'kupplung.jpg', 'Kupplung vorne', 3);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 7, 'rauch.jpg', 'Rauchgenerator', 3);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 8, 'zylinder_blasen.jpg', 'Zylinder ausblasen', 3);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 9, 'ansage.jpg', 'Ansage Fahrkartenkontrolle', 3);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 10, 'blaeser.jpg', 'Hilfsbläser', 3);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 11, 'wasserpumpe.jpg', 'Wasserstrahlpumpe', 3);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 12, 'kohle.jpg', 'Kohle schaufeln', 3);

-- locomotive id 4, 99 1568
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 0, 'licht.jpg', 'Licht vorne', 4);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 1, 'pfeife_lang.jpg', 'Pfeife lang', 4);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 2, 'bremse.jpg', 'Bremse', 4);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 3, 'glocke.jpg', 'Glocker', 4);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 4, 'ansage.jpg', 'Ansage Abfahrt', 4);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 5, 'wasserpumpe.jpg', 'Wasserstrahlpumpe', 4);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 6, 'blaeser.jpg', 'Hilfsbläser', 4);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 7, 'rauch.jpg', 'Rauchgenerator', 4);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 8, 'sound.jpg', 'Sound EIN/AUS', 4);

-- locomotive id 5, 99 193
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 0, 'licht.jpg', 'Licht vorne', 5);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 1, 'sound.jpg', 'Sound EIN/AUS', 5);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 2, 'glocke.jpg', 'Glocke', 5);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 3, 'pfeife_lang.jpg', 'Pfeife lang', 5);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 4, 'licht.jpg', 'Innenbeleuchtung', 5);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 5, 'pfeife_kurz.jpg', 'Pfeife kurz', 5);

-- SELECT setval('public."hibernate_sequence"',4);