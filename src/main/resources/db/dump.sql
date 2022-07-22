--
-- PostgreSQL database dump
--

-- Dumped from database version 14.2
-- Dumped by pg_dump version 14.2

-- Started on 2022-06-30 13:01:10

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 214 (class 1259 OID 20055)
-- Name: authorities; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.authorities (
    id bigint NOT NULL,
    authority character varying(255),
    username bigint
);


ALTER TABLE public.authorities OWNER TO postgres;

--
-- TOC entry 213 (class 1259 OID 20054)
-- Name: authorities_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.authorities_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.authorities_id_seq OWNER TO postgres;

--
-- TOC entry 3339 (class 0 OID 0)
-- Dependencies: 213
-- Name: authorities_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.authorities_id_seq OWNED BY public.authorities.id;


--
-- TOC entry 212 (class 1259 OID 20041)
-- Name: notes; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.notes (
    note_id bigint NOT NULL,
    date date DEFAULT now(),
    done boolean DEFAULT false,
    task character varying(255),
    user_id bigint
);


ALTER TABLE public.notes OWNER TO postgres;

--
-- TOC entry 211 (class 1259 OID 20040)
-- Name: notes_note_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.notes_note_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.notes_note_id_seq OWNER TO postgres;

--
-- TOC entry 3340 (class 0 OID 0)
-- Dependencies: 211
-- Name: notes_note_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.notes_note_id_seq OWNED BY public.notes.note_id;


--
-- TOC entry 210 (class 1259 OID 20030)
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    user_id bigint NOT NULL,
    email character varying(255),
    password character varying(255),
    username character varying(255)
);


ALTER TABLE public.users OWNER TO postgres;

--
-- TOC entry 209 (class 1259 OID 20029)
-- Name: users_user_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.users_user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.users_user_id_seq OWNER TO postgres;

--
-- TOC entry 3341 (class 0 OID 0)
-- Dependencies: 209
-- Name: users_user_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.users_user_id_seq OWNED BY public.users.user_id;


--
-- TOC entry 3178 (class 2604 OID 20058)
-- Name: authorities id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.authorities ALTER COLUMN id SET DEFAULT nextval('public.authorities_id_seq'::regclass);


--
-- TOC entry 3175 (class 2604 OID 20044)
-- Name: notes note_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.notes ALTER COLUMN note_id SET DEFAULT nextval('public.notes_note_id_seq'::regclass);


--
-- TOC entry 3174 (class 2604 OID 20033)
-- Name: users user_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users ALTER COLUMN user_id SET DEFAULT nextval('public.users_user_id_seq'::regclass);


--
-- TOC entry 3333 (class 0 OID 20055)
-- Dependencies: 214
-- Data for Name: authorities; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 3331 (class 0 OID 20041)
-- Dependencies: 212
-- Data for Name: notes; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.notes (note_id, date, done, task, user_id) VALUES (1, '2022-06-17', false, 'emm', 1);
INSERT INTO public.notes (note_id, date, done, task, user_id) VALUES (2, '2022-06-17', true, 'lll', 1);
INSERT INTO public.notes (note_id, date, done, task, user_id) VALUES (3, '2022-06-18', false, 'aaa', 1);
INSERT INTO public.notes (note_id, date, done, task, user_id) VALUES (4, '2022-06-19', false, 'kkk', 2);


--
-- TOC entry 3329 (class 0 OID 20030)
-- Dependencies: 210
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.users (user_id, email, password, username) VALUES (1, 'email4@com', '12345', 'user4');
INSERT INTO public.users (user_id, email, password, username) VALUES (2, 'ksd@Mail.com', 'kate', 'sonya');
INSERT INTO public.users (user_id, email, password, username) VALUES (3, 'mail', '12345', 'name');


--
-- TOC entry 3342 (class 0 OID 0)
-- Dependencies: 213
-- Name: authorities_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.authorities_id_seq', 1, false);


--
-- TOC entry 3343 (class 0 OID 0)
-- Dependencies: 211
-- Name: notes_note_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.notes_note_id_seq', 1, false);


--
-- TOC entry 3344 (class 0 OID 0)
-- Dependencies: 209
-- Name: users_user_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.users_user_id_seq', 1, false);


--
-- TOC entry 3186 (class 2606 OID 20060)
-- Name: authorities authorities_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.authorities
    ADD CONSTRAINT authorities_pkey PRIMARY KEY (id);


--
-- TOC entry 3184 (class 2606 OID 20048)
-- Name: notes notes_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.notes
    ADD CONSTRAINT notes_pkey PRIMARY KEY (note_id);


--
-- TOC entry 3180 (class 2606 OID 20039)
-- Name: users uk_r43af9ap4edm43mmtq01oddj6; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT uk_r43af9ap4edm43mmtq01oddj6 UNIQUE (username);


--
-- TOC entry 3182 (class 2606 OID 20037)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (user_id);


--
-- TOC entry 3187 (class 2606 OID 20049)
-- Name: notes fkechaouoa6kus6k1dpix1u91c; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.notes
    ADD CONSTRAINT fkechaouoa6kus6k1dpix1u91c FOREIGN KEY (user_id) REFERENCES public.users(user_id) ON DELETE CASCADE;


--
-- TOC entry 3188 (class 2606 OID 20061)
-- Name: authorities fkhjuy9y4fd8v5m3klig05ktofg; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.authorities
    ADD CONSTRAINT fkhjuy9y4fd8v5m3klig05ktofg FOREIGN KEY (username) REFERENCES public.users(user_id);


-- Completed on 2022-06-30 13:01:11

--
-- PostgreSQL database dump complete
--

