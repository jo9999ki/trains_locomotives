DROP TABLE public.function;
DROP TABLE public.locomotive;

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

DROP INDEX public.index_locomotive;

CREATE INDEX index_locomotive
    ON public.locomotive USING btree
     (address)
     TABLESPACE pg_default;

INSERT INTO public.locomotive VALUES (0, 9, '99 5906', '1986-01-01');
INSERT INTO public.locomotive VALUES (1, 60, '99 6001', '1986-01-01');


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

INSERT INTO public.function VALUES (0, 15, 'luftpumpe.jpg', 'Luftpumpe', 0);
INSERT INTO public.function VALUES (1, 12, 'Kohlen.jpg', 'Kohlen schaufeln', 0);