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

INSERT INTO public.locomotive VALUES (nextval('hibernate_sequence'), 9, '99 5906', '1986-01-01');
INSERT INTO public.locomotive VALUES (nextval('hibernate_sequence'), 60, '99 6001', '1986-01-01');
INSERT INTO public.locomotive VALUES (nextval('hibernate_sequence'), 64, '99 1564', '1973-10-16');

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

INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 15, 'luftpumpe.jpg', 'Luftpumpe', 1);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 12, 'Kohlen.jpg', 'Kohlen schaufeln', 1);

-- locomotive id 3, 99 1564
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 0, 'licht.jpg', 'Licht_vorne', 3);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 1, 'pfeife_lang.jpg', 'Pfeife_lang', 3);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 2, 'pfeife_kurz.jpg', 'Pfeife_kurz', 3);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 3, 'ansage.jpg', 'Einsteigen und Türen schließen', 3);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 4, 'glocke.jpg', 'Glocke', 3);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 5, 'kupplung.jpg', 'Kupplung hinten', 3);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 6, 'kupplung.jpg', 'Kupplung vorne', 3);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 7, 'dampf.jpg', 'Verdampfer', 3);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 8, 'zylinder_blasen.jpg', 'Zylinder ausblasen', 3);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 9, 'ansage.jpg', 'Die Fahrkarten bitte', 3);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 10, 'blaeser.jpg', 'Hilfsbläser', 3);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 11, 'pumpe.jpg', 'Wasserstrahlpumpe', 3);
INSERT INTO public.function VALUES (nextval('hibernate_sequence'), 12, 'kohle.jpg', 'Kohle schaufeln', 3);

-- SELECT setval('public."hibernate_sequence"',4);