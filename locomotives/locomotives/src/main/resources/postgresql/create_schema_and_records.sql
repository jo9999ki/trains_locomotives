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

-- Index: index_locomotive

-- DROP INDEX public.index_locomotive;

CREATE INDEX index_locomotive
    ON public.locomotive USING btree
    (address)
    TABLESPACE pg_default;

INSERT INTO locomotive VALUES (0, 1, '99 5906', '1986-01-01');