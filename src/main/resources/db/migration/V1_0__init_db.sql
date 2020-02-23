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

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


--
-- Name: delete_costs_dz(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.delete_costs_dz() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
	BEGIN
			update dz
			set zs_dz = ta.sumZsDz,
			zs_zz = ta.sumZzZz,
			rs_dz = ta.sumRsDz,
			rs_zz = ta.sumRsZz,
			ss = ta.sumSs
			from 
			(select d.dz_id as id, sum(ps.zs_dz) as sumZsDz, sum(ps.zs_zz) as sumZzZz, sum(ps.rs_dz) as sumRsDz, sum(ps.rs_zz) as sumRsZz, sum(ps.ss) as sumSs
			from pledge_subject as ps 
			join dz_ps as dzps on ps.pledge_subject_id = dzps.pledge_subject_id
			join dz as d on d.dz_id = dzps.dz_id
			where d.dz_id = old.dz_id
			group by 1  ) ta
			where dz.dz_id = ta.id;
	
	return old;
	
	END;
$$;


ALTER FUNCTION public.delete_costs_dz() OWNER TO postgres;

--
-- Name: insert_costs_dz(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.insert_costs_dz() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
	BEGIN
			update dz
			set zs_dz = ta.sumZsDz,
			zs_zz = ta.sumZzZz,
			rs_dz = ta.sumRsDz,
			rs_zz = ta.sumRsZz,
			ss = ta.sumSs
			from 
			(select d.dz_id as id, sum(ps.zs_dz) as sumZsDz, sum(ps.zs_zz) as sumZzZz, sum(ps.rs_dz) as sumRsDz, sum(ps.rs_zz) as sumRsZz, sum(ps.ss) as sumSs
			from pledge_subject as ps 
			join dz_ps as dzps on ps.pledge_subject_id = dzps.pledge_subject_id
			join dz as d on d.dz_id = dzps.dz_id
			where d.dz_id = new.dz_id
			group by 1  ) ta
			where dz.dz_id = ta.id;
	
	return new;
	
	END;
$$;


ALTER FUNCTION public.insert_costs_dz() OWNER TO postgres;

--
-- Name: trigger_conclusion(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.trigger_conclusion() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
declare
	dateMon date;
begin
	select date_conclusion into dateMon
	from "pledge_subject"
	where "pledge_subject_id" = new."pledge_subject_id";
						
	if (new.date >= dateMon) then
		update "pledge_subject"
		set "zs_dz" = new."zs_dz",
		"zs_zz" = new."zs_zz",
		"rs_dz" = new."rs_dz",
		"rs_zz" = new."rs_zz",
		"ss" = new."ss",
		"date_conclusion" = new."date"
		where "pledge_subject_id" = new."pledge_subject_id";
	end if;

	return new;
end;
$$;


ALTER FUNCTION public.trigger_conclusion() OWNER TO postgres;

--
-- Name: trigger_monitoring(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.trigger_monitoring() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
declare
	dateMon date;
begin
	select date_monitoring into dateMon
	from "pledge_subject"
	where "pledge_subject_id" = new."pledge_subject_id";
						
	if (new.date >= dateMon) then
		update "pledge_subject"
		set date_monitoring = new.date,
		status_monitoring = new.status,
		type_of_monitoring = new.type
		where "pledge_subject_id" = new."pledge_subject_id";
	end if;

	return new;
end;
$$;


ALTER FUNCTION public.trigger_monitoring() OWNER TO postgres;

--
-- Name: update_costs_dz(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.update_costs_dz() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
	BEGIN
		if(new.zs_dz != old.zs_dz) then
			update dz
			set zs_dz = ta.summa
			from 
			(select d.dz_id as id, sum(ps.zs_dz) as summa
			from pledge_subject as ps 
			join dz_ps as dzps on ps.pledge_subject_id = dzps.pledge_subject_id
			join dz as d on d.dz_id = dzps.dz_id
			where d.dz_id in (select d1.dz_id
			from pledge_subject as ps1 
			join dz_ps as dzps1 on ps1.pledge_subject_id = dzps1.pledge_subject_id
			join dz as d1 on d1.dz_id = dzps1.dz_id
			where ps1.pledge_subject_id = old.pledge_subject_id)
			group by 1) ta
			where dz.dz_id = ta.id;
		end if;
	
		if(new.zs_zz != old.zs_zz) then
			update dz
			set zs_zz = ta.summa
			from 
			(select d.dz_id as id, sum(ps.zs_zz) as summa
			from pledge_subject as ps 
			join dz_ps as dzps on ps.pledge_subject_id = dzps.pledge_subject_id
			join dz as d on d.dz_id = dzps.dz_id
			where d.dz_id in (select d1.dz_id
			from pledge_subject as ps1 
			join dz_ps as dzps1 on ps1.pledge_subject_id = dzps1.pledge_subject_id
			join dz as d1 on d1.dz_id = dzps1.dz_id
			where ps1.pledge_subject_id = old.pledge_subject_id)
			group by 1) ta
			where dz.dz_id = ta.id;
		end if;
	
		if(new.rs_dz != old.rs_dz) then
			update dz
			set rs_dz = ta.summa
			from 
			(select d.dz_id as id, sum(ps.rs_dz) as summa
			from pledge_subject as ps 
			join dz_ps as dzps on ps.pledge_subject_id = dzps.pledge_subject_id
			join dz as d on d.dz_id = dzps.dz_id
			where d.dz_id in (select d1.dz_id
			from pledge_subject as ps1 
			join dz_ps as dzps1 on ps1.pledge_subject_id = dzps1.pledge_subject_id
			join dz as d1 on d1.dz_id = dzps1.dz_id
			where ps1.pledge_subject_id = old.pledge_subject_id)
			group by 1) ta
			where dz.dz_id = ta.id;
		end if;
	
		if(new.rs_zz != old.rs_zz) then
			update dz
			set rs_zz = ta.summa
			from 
			(select d.dz_id as id, sum(ps.rs_zz) as summa
			from pledge_subject as ps 
			join dz_ps as dzps on ps.pledge_subject_id = dzps.pledge_subject_id
			join dz as d on d.dz_id = dzps.dz_id
			where d.dz_id in (select d1.dz_id
			from pledge_subject as ps1 
			join dz_ps as dzps1 on ps1.pledge_subject_id = dzps1.pledge_subject_id
			join dz as d1 on d1.dz_id = dzps1.dz_id
			where ps1.pledge_subject_id = old.pledge_subject_id)
			group by 1) ta
			where dz.dz_id = ta.id;
		end if;
	
		if(new.ss != old.ss) then
			update dz
			set ss = ta.summa
			from 
			(select d.dz_id as id, sum(ps.ss) as summa
			from pledge_subject as ps 
			join dz_ps as dzps on ps.pledge_subject_id = dzps.pledge_subject_id
			join dz as d on d.dz_id = dzps.dz_id
			where d.dz_id in (select d1.dz_id
			from pledge_subject as ps1 
			join dz_ps as dzps1 on ps1.pledge_subject_id = dzps1.pledge_subject_id
			join dz as d1 on d1.dz_id = dzps1.dz_id
			where ps1.pledge_subject_id = old.pledge_subject_id)
			group by 1) ta
			where dz.dz_id = ta.id;
		end if;
	
	return new;
	
	END;
$$;


ALTER FUNCTION public.update_costs_dz() OWNER TO postgres;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: client_individual; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.client_individual (
    client_id bigint NOT NULL,
    surname character varying NOT NULL,
    name character varying NOT NULL,
    patronymic character varying,
    pasport_number character varying
);


ALTER TABLE public.client_individual OWNER TO postgres;

--
-- Name: client_legal_entity; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.client_legal_entity (
    client_id bigint NOT NULL,
    organizational_form character varying NOT NULL,
    name character varying NOT NULL,
    inn character varying
);


ALTER TABLE public.client_legal_entity OWNER TO postgres;

--
-- Name: client_manager; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.client_manager (
    client_manager_id bigint NOT NULL,
    surname character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    patronymic character varying(255)
);


ALTER TABLE public.client_manager OWNER TO postgres;

--
-- Name: client_manager_client_manager_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.client_manager_client_manager_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.client_manager_client_manager_id_seq OWNER TO postgres;

--
-- Name: client_manager_client_manager_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.client_manager_client_manager_id_seq OWNED BY public.client_manager.client_manager_id;


--
-- Name: client_prime; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.client_prime (
    client_id bigint NOT NULL,
    client_manager_id bigint NOT NULL,
    employee_id bigint NOT NULL,
    type_of_client character varying(2) NOT NULL,
    CONSTRAINT check_client_prime_type_of_client CHECK (((type_of_client)::text = ANY (ARRAY[('юл'::character varying)::text, ('фл'::character varying)::text])))
);


ALTER TABLE public.client_prime OWNER TO postgres;

--
-- Name: client_prime_client_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.client_prime_client_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.client_prime_client_id_seq OWNER TO postgres;

--
-- Name: client_prime_client_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.client_prime_client_id_seq OWNED BY public.client_prime.client_id;


--
-- Name: cost_history; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.cost_history (
    pledge_subject_id bigint NOT NULL,
    date date NOT NULL,
    zs_dz numeric(255,2) NOT NULL,
    zs_zz numeric(255,2) NOT NULL,
    rs_dz numeric(255,2) NOT NULL,
    rs_zz numeric(255,2) NOT NULL,
    ss numeric(255,2) NOT NULL,
    cost_history_id bigint NOT NULL,
    appraiser character varying,
    num_appraisal_report character varying,
    date_appraisal_report character varying
);


ALTER TABLE public.cost_history OWNER TO postgres;

--
-- Name: cost_history_cost_history_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.cost_history_cost_history_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.cost_history_cost_history_id_seq OWNER TO postgres;

--
-- Name: cost_history_cost_history_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.cost_history_cost_history_id_seq OWNED BY public.cost_history.cost_history_id;


--
-- Name: dz; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.dz (
    dz_id bigint NOT NULL,
    num_dz character varying(255) NOT NULL,
    date_begin_dz date NOT NULL,
    date_end_dz date NOT NULL,
    perv_posl character varying(16) NOT NULL,
    pledgor_id bigint NOT NULL,
    status character varying(255) NOT NULL,
    notice character varying(1000),
    zs_dz numeric(255,2) NOT NULL,
    zs_zz numeric(255,2) NOT NULL,
    rs_dz numeric(255,2) NOT NULL,
    rs_zz numeric(255,2) NOT NULL,
    ss numeric(255,2) NOT NULL,
    CONSTRAINT check_dz_perv_posl CHECK (((perv_posl)::text = ANY (ARRAY[('перв'::character varying)::text, ('посл'::character varying)::text]))),
    CONSTRAINT check_dz_status CHECK (((status)::text = ANY (ARRAY[('закрыт'::character varying)::text, ('открыт'::character varying)::text])))
);


ALTER TABLE public.dz OWNER TO postgres;

--
-- Name: dz_dz_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.dz_dz_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.dz_dz_id_seq OWNER TO postgres;

--
-- Name: dz_dz_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.dz_dz_id_seq OWNED BY public.dz.dz_id;


--
-- Name: dz_ps; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.dz_ps (
    id bigint NOT NULL,
    dz_id bigint NOT NULL,
    pledge_subject_id bigint NOT NULL
);


ALTER TABLE public.dz_ps OWNER TO postgres;

--
-- Name: dz_ps_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.dz_ps_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.dz_ps_id_seq OWNER TO postgres;

--
-- Name: dz_ps_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.dz_ps_id_seq OWNED BY public.dz_ps.id;


--
-- Name: employee; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.employee (
    employee_id bigint NOT NULL,
    surname character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    patronymic character varying(255)
);


ALTER TABLE public.employee OWNER TO postgres;

--
-- Name: employee_employee_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.employee_employee_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.employee_employee_id_seq OWNER TO postgres;

--
-- Name: employee_employee_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.employee_employee_id_seq OWNED BY public.employee.employee_id;


--
-- Name: encumbrance; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.encumbrance (
    encumbrance_id bigint NOT NULL,
    pledgesubject_id bigint NOT NULL,
    name character varying NOT NULL,
    type_of_encumbrance character varying NOT NULL,
    in_favor_of_whom character varying NOT NULL,
    date_begin date NOT NULL,
    date_end date NOT NULL,
    num_of_encumbrance character varying NOT NULL,
    CONSTRAINT check_encumbrance_type_of_encumbrance CHECK (((type_of_encumbrance)::text = ANY (ARRAY[('залог'::character varying)::text, ('арест'::character varying)::text, ('аренда'::character varying)::text, ('сервитут'::character varying)::text, ('доверительное управление'::character varying)::text])))
);


ALTER TABLE public.encumbrance OWNER TO postgres;

--
-- Name: encumbrance_encumbrance_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.encumbrance_encumbrance_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.encumbrance_encumbrance_id_seq OWNER TO postgres;

--
-- Name: encumbrance_encumbrance_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.encumbrance_encumbrance_id_seq OWNED BY public.encumbrance.encumbrance_id;


--
-- Name: insurance; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.insurance (
    insurance_id bigint NOT NULL,
    num_insurance character varying(255) NOT NULL,
    date_begin date NOT NULL,
    date_end date NOT NULL,
    sum_insured numeric(255,2) NOT NULL,
    date_insurance_contract date NOT NULL,
    pledgesubject_id bigint NOT NULL,
    payment_of_insurance_premium character varying NOT NULL,
    franchise_amount numeric(255,2) NOT NULL,
    CONSTRAINT check_insurance_payment CHECK (((payment_of_insurance_premium)::text = ANY (ARRAY[('да'::character varying)::text, ('нет'::character varying)::text])))
);


ALTER TABLE public.insurance OWNER TO postgres;

--
-- Name: insurance_insurance_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.insurance_insurance_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.insurance_insurance_id_seq OWNER TO postgres;

--
-- Name: insurance_insurance_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.insurance_insurance_id_seq OWNED BY public.insurance.insurance_id;


--
-- Name: kd; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.kd (
    kd_id bigint NOT NULL,
    num_kd character varying(255) NOT NULL,
    date_begin_kd date NOT NULL,
    loaner_id bigint NOT NULL,
    status character varying(255) NOT NULL,
    loan_amount numeric(255,2) NOT NULL,
    loan_debt numeric(255,2) NOT NULL,
    interest_rate numeric(24,6) NOT NULL,
    pfo smallint NOT NULL,
    quality_category smallint NOT NULL,
    date_end_kd date NOT NULL,
    CONSTRAINT check_kd_pfo CHECK (((pfo >= 1) AND (pfo <= 5))),
    CONSTRAINT check_kd_quality_category CHECK (((quality_category >= 1) AND (quality_category <= 5))),
    CONSTRAINT check_kd_status CHECK (((status)::text = ANY (ARRAY[('открыт'::character varying)::text, ('закрыт'::character varying)::text])))
);


ALTER TABLE public.kd OWNER TO postgres;

--
-- Name: kd_dz; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.kd_dz (
    id bigint NOT NULL,
    kd_id bigint NOT NULL,
    dz_id bigint NOT NULL
);


ALTER TABLE public.kd_dz OWNER TO postgres;

--
-- Name: kd_dz_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.kd_dz_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.kd_dz_id_seq OWNER TO postgres;

--
-- Name: kd_dz_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.kd_dz_id_seq OWNED BY public.kd_dz.id;


--
-- Name: kd_kd_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.kd_kd_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.kd_kd_id_seq OWNER TO postgres;

--
-- Name: kd_kd_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.kd_kd_id_seq OWNED BY public.kd.kd_id;


--
-- Name: monitoring; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.monitoring (
    pledge_subject_id bigint NOT NULL,
    date date NOT NULL,
    status character varying(255) NOT NULL,
    employee character varying(255) NOT NULL,
    monitoring_id bigint NOT NULL,
    type character varying NOT NULL,
    notice character varying,
    collateral_value numeric(255,2),
    CONSTRAINT check_monitoring_status CHECK (((status)::text = ANY (ARRAY[('в наличии'::character varying)::text, ('утрата'::character varying)::text, ('частичная утрата'::character varying)::text]))),
    CONSTRAINT check_monitoring_type CHECK (((type)::text = ANY (ARRAY[('документарный'::character varying)::text, ('визуальный'::character varying)::text])))
);


ALTER TABLE public.monitoring OWNER TO postgres;

--
-- Name: monitoring_monitoring_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.monitoring_monitoring_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.monitoring_monitoring_id_seq OWNER TO postgres;

--
-- Name: monitoring_monitoring_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.monitoring_monitoring_id_seq OWNED BY public.monitoring.monitoring_id;


--
-- Name: pledge_auto; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pledge_auto (
    pledge_subject_id bigint NOT NULL,
    brand_auto character varying(255) NOT NULL,
    model_auto character varying(255) NOT NULL,
    vin character varying(255),
    num_of_engine character varying(255),
    num_of_pts character varying(255),
    year_of_manufacture_auto smallint,
    inventory_number_auto character varying(255),
    type_of_auto character varying NOT NULL,
    horsepower numeric(255,0),
    CONSTRAINT check_pledge_auto_type_of_auto CHECK (((type_of_auto)::text = ANY (ARRAY[('бульдозер'::character varying)::text, ('экскаватор'::character varying)::text, ('прицеп'::character varying)::text, ('погрузчик'::character varying)::text, ('кран'::character varying)::text, ('дорожно-строительная'::character varying)::text, ('комбайн'::character varying)::text, ('трактор'::character varying)::text, ('пассажирский транспорт'::character varying)::text, ('грузовой транспорт'::character varying)::text, ('легковой транспорт'::character varying)::text, ('ж/д транспорт'::character varying)::text, ('иное'::character varying)::text])))
);


ALTER TABLE public.pledge_auto OWNER TO postgres;

--
-- Name: pledge_equipment; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pledge_equipment (
    pledge_subject_id bigint NOT NULL,
    brand_equip character varying(255) NOT NULL,
    model_equip character varying(255) NOT NULL,
    serial_number character varying(255),
    year_of_manufacture_equip smallint,
    inventory_number_equip character varying(255),
    type_of_equipment character varying NOT NULL,
    productivity numeric(255,0),
    type_of_productivity character varying,
    CONSTRAINT check_pledge_equipment_type_of_equipment CHECK (((type_of_equipment)::text = ANY (ARRAY[('металлообработка'::character varying)::text, ('лесообработка'::character varying)::text, ('торговое'::character varying)::text, ('офисное'::character varying)::text, ('сети ито'::character varying)::text, ('рекламное'::character varying)::text, ('пищевое'::character varying)::text, ('автомобильное'::character varying)::text, ('азс'::character varying)::text, ('химическое'::character varying)::text, ('измерительное'::character varying)::text, ('медицинское'::character varying)::text, ('нефте-газовое'::character varying)::text, ('карьерное и горное'::character varying)::text, ('подъемное'::character varying)::text, ('авиационное'::character varying)::text, ('строительое'::character varying)::text, ('ресторанное'::character varying)::text, ('транспортировка'::character varying)::text, ('упаковачное'::character varying)::text, ('хранение'::character varying)::text, ('с/х назначения'::character varying)::text, ('иное'::character varying)::text])))
);


ALTER TABLE public.pledge_equipment OWNER TO postgres;

--
-- Name: pledge_realty_building; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pledge_realty_building (
    pledge_subject_id bigint NOT NULL,
    readiness_degree smallint NOT NULL,
    year_of_construction smallint NOT NULL,
    market_segment character varying NOT NULL,
    area_building numeric(255,2) NOT NULL,
    cadastral_num_building character varying(255) NOT NULL,
    conditional_num_building character varying
);


ALTER TABLE public.pledge_realty_building OWNER TO postgres;

--
-- Name: pledge_realty_land_lease; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pledge_realty_land_lease (
    pledge_subject_id bigint NOT NULL,
    permitted_use_land_lease character varying(255) NOT NULL,
    built_up_land_lease character varying(255) NOT NULL,
    cadastral_num_of_building_land_lease character varying(1000),
    begin_date_lease date NOT NULL,
    end_date_lease date NOT NULL,
    land_category_land_lease character varying NOT NULL,
    area_land_lease numeric(255,2) NOT NULL,
    cadastral_num_land_lease character varying(255) NOT NULL,
    conditional_num_land_lease character varying,
    CONSTRAINT check_land_lease_built_up CHECK (((built_up_land_lease)::text = ANY (ARRAY[('да'::character varying)::text, ('нет'::character varying)::text])))
);


ALTER TABLE public.pledge_realty_land_lease OWNER TO postgres;

--
-- Name: pledge_realty_land_ownership; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pledge_realty_land_ownership (
    pledge_subject_id bigint NOT NULL,
    permitted_use_land_own character varying(255) NOT NULL,
    built_up_land_own character varying(255) NOT NULL,
    cadastral_num_of_building_land_own character varying(1000),
    land_category_land_own character varying NOT NULL,
    area_land_own numeric(255,2) NOT NULL,
    cadastral_num_land_own character varying(255) NOT NULL,
    conditional_num_land_own character varying,
    CONSTRAINT check_land_ownership_built_up CHECK (((built_up_land_own)::text = ANY (ARRAY[('да'::character varying)::text, ('нет'::character varying)::text])))
);


ALTER TABLE public.pledge_realty_land_ownership OWNER TO postgres;

--
-- Name: pledge_realty_room; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pledge_realty_room (
    pledge_subject_id bigint NOT NULL,
    floor_location character varying(255) NOT NULL,
    market_segment_room character varying NOT NULL,
    market_segment_building character varying NOT NULL,
    area_room numeric(255,2) NOT NULL,
    cadastral_num_room character varying(255) NOT NULL,
    conditional_num_room character varying
);


ALTER TABLE public.pledge_realty_room OWNER TO postgres;

--
-- Name: pledge_securities; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pledge_securities (
    pledge_subject_id bigint NOT NULL,
    nominal_value numeric(255,2) NOT NULL,
    actual_value numeric(255,2) NOT NULL,
    type_of_securities character varying NOT NULL,
    CONSTRAINT check_pledge_securities_type_of_securities CHECK (((type_of_securities)::text = ANY (ARRAY[('доли в ук'::character varying)::text, ('акции'::character varying)::text, ('вексель'::character varying)::text, ('паи'::character varying)::text])))
);


ALTER TABLE public.pledge_securities OWNER TO postgres;

--
-- Name: pledge_subject; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pledge_subject (
    pledge_subject_id bigint NOT NULL,
    name character varying(1000) NOT NULL,
    liquidity character varying(255) NOT NULL,
    zs_dz numeric(255,2) NOT NULL,
    zs_zz numeric(255,2) NOT NULL,
    rs_dz numeric(255,2) NOT NULL,
    rs_zz numeric(255,2) NOT NULL,
    ss numeric(255,2) NOT NULL,
    date_monitoring date NOT NULL,
    date_conclusion date NOT NULL,
    status_monitoring character varying NOT NULL,
    type_of_collateral character varying NOT NULL,
    type_of_pledge character varying NOT NULL,
    type_of_monitoring character varying NOT NULL,
    adress_region character varying NOT NULL,
    adress_district character varying,
    adress_city character varying,
    adress_street character varying,
    adress_building character varying,
    adress_premises character varying,
    insurance_obligation character varying NOT NULL,
    CONSTRAINT "check_pledgeSubject_liquidity" CHECK (((liquidity)::text = ANY (ARRAY[('высокая'::character varying)::text, ('средняя'::character varying)::text, ('ниже средней'::character varying)::text, ('низкая'::character varying)::text]))),
    CONSTRAINT check_pledgesubject_insurance_obligation CHECK (((insurance_obligation)::text = ANY (ARRAY[('да'::character varying)::text, ('нет'::character varying)::text]))),
    CONSTRAINT check_pledgesubject_status_monitoring CHECK (((status_monitoring)::text = ANY (ARRAY[('в наличии'::character varying)::text, ('утрата'::character varying)::text, ('частичная утрата'::character varying)::text]))),
    CONSTRAINT check_pledgesubject_type_of_collateral CHECK (((type_of_collateral)::text = ANY (ARRAY[('Авто/спецтехника'::character varying)::text, ('Оборудование'::character varying)::text, ('ТМЦ'::character varying)::text, ('Ценные бумаги'::character varying)::text, ('Недвижимость - ЗУ - собственность'::character varying)::text, ('Недвижимость - ЗУ - право аренды'::character varying)::text, ('Недвижимость - здание/сооружение'::character varying)::text, ('Недвижимость - помещение'::character varying)::text, ('Судно'::character varying)::text]))),
    CONSTRAINT check_pledgesubject_type_of_monitoring CHECK (((type_of_monitoring)::text = ANY (ARRAY[('документарный'::character varying)::text, ('визуальный'::character varying)::text]))),
    CONSTRAINT check_pledgesubject_type_of_pledge CHECK (((type_of_pledge)::text = ANY (ARRAY[('возвратная'::character varying)::text, ('рычаговая'::character varying)::text, ('ограничивающая'::character varying)::text, ('информационная'::character varying)::text])))
);


ALTER TABLE public.pledge_subject OWNER TO postgres;

--
-- Name: pledge_subject_pledge_subject_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.pledge_subject_pledge_subject_id_seq
    START WITH 1691
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.pledge_subject_pledge_subject_id_seq OWNER TO postgres;

--
-- Name: pledge_subject_pledge_subject_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.pledge_subject_pledge_subject_id_seq OWNED BY public.pledge_subject.pledge_subject_id;


--
-- Name: pledge_tbo; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pledge_tbo (
    pledge_subject_id bigint NOT NULL,
    count_of_tbo numeric(255,2),
    carrying_amount numeric(255,2),
    type_of_tbo character varying NOT NULL,
    CONSTRAINT check_pledge_tbo_type_of_tbo CHECK (((type_of_tbo)::text = ANY (ARRAY[('транспорт'::character varying)::text, ('запчасти'::character varying)::text, ('одежда'::character varying)::text, ('продукты питания'::character varying)::text, ('алкоголь'::character varying)::text, ('нефтехимия'::character varying)::text, ('металлопродукция'::character varying)::text, ('стройматериалы'::character varying)::text, ('крс'::character varying)::text, ('мрс'::character varying)::text, ('медикаменты'::character varying)::text, ('сантехника'::character varying)::text])))
);


ALTER TABLE public.pledge_tbo OWNER TO postgres;

--
-- Name: pledge_vessel; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pledge_vessel (
    pledge_subject_id bigint NOT NULL,
    imo character varying NOT NULL,
    mmsi character varying,
    flag character varying NOT NULL,
    vessel_type character varying NOT NULL,
    gross_tonnage bigint NOT NULL,
    deadweight bigint NOT NULL,
    year_built smallint NOT NULL,
    status character varying NOT NULL,
    CONSTRAINT check_pledge_vessel_vessel_type CHECK (((vessel_type)::text = ANY (ARRAY[('general cargo (gcd)'::character varying)::text, ('container ship (con)'::character varying)::text, ('log carrier/timber (log)'::character varying)::text, ('ro-ro (r/r)'::character varying)::text, ('bulk carrier (b/c)'::character varying)::text, ('ore/oil carrier (o/o)'::character varying)::text, ('oil/bulk/ore carrier (obo)'::character varying)::text, ('tanker product (tnp)'::character varying)::text, ('tanker crude (tnc)'::character varying)::text, ('tanker storage (tns)'::character varying)::text, ('tanker vlcc/ulcc (tnv)'::character varying)::text, ('chemical tanker (chm)'::character varying)::text, ('lpg/lng carrier (gas)'::character varying)::text, ('offshore supply vessel (osv)'::character varying)::text, ('heavy lift vessel (hvl)'::character varying)::text, ('survey vessel (srv)'::character varying)::text, ('passenger ship (pas)'::character varying)::text, ('reefer (rfg)'::character varying)::text, ('livestock carrier (liv)'::character varying)::text, ('tug'::character varying)::text, ('fishing trawler (fsh)'::character varying)::text, ('dredger (drg)'::character varying)::text, ('м'::character varying)::text, ('м-сп'::character varying)::text, ('о'::character varying)::text, ('р'::character varying)::text, ('л'::character varying)::text]))),
    CONSTRAINT check_pledge_vessel_year_built CHECK (((year_built >= 1900) AND (year_built <= 2100)))
);


ALTER TABLE public.pledge_vessel OWNER TO postgres;

--
-- Name: client_manager client_manager_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.client_manager ALTER COLUMN client_manager_id SET DEFAULT nextval('public.client_manager_client_manager_id_seq'::regclass);


--
-- Name: client_prime client_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.client_prime ALTER COLUMN client_id SET DEFAULT nextval('public.client_prime_client_id_seq'::regclass);


--
-- Name: cost_history cost_history_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cost_history ALTER COLUMN cost_history_id SET DEFAULT nextval('public.cost_history_cost_history_id_seq'::regclass);


--
-- Name: dz dz_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.dz ALTER COLUMN dz_id SET DEFAULT nextval('public.dz_dz_id_seq'::regclass);


--
-- Name: dz_ps id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.dz_ps ALTER COLUMN id SET DEFAULT nextval('public.dz_ps_id_seq'::regclass);


--
-- Name: employee employee_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.employee ALTER COLUMN employee_id SET DEFAULT nextval('public.employee_employee_id_seq'::regclass);


--
-- Name: encumbrance encumbrance_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.encumbrance ALTER COLUMN encumbrance_id SET DEFAULT nextval('public.encumbrance_encumbrance_id_seq'::regclass);


--
-- Name: insurance insurance_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.insurance ALTER COLUMN insurance_id SET DEFAULT nextval('public.insurance_insurance_id_seq'::regclass);


--
-- Name: kd kd_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.kd ALTER COLUMN kd_id SET DEFAULT nextval('public.kd_kd_id_seq'::regclass);


--
-- Name: kd_dz id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.kd_dz ALTER COLUMN id SET DEFAULT nextval('public.kd_dz_id_seq'::regclass);


--
-- Name: monitoring monitoring_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.monitoring ALTER COLUMN monitoring_id SET DEFAULT nextval('public.monitoring_monitoring_id_seq'::regclass);


--
-- Name: pledge_subject pledge_subject_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pledge_subject ALTER COLUMN pledge_subject_id SET DEFAULT nextval('public.pledge_subject_pledge_subject_id_seq'::regclass);


--
-- Name: client_manager client_manager_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.client_manager
    ADD CONSTRAINT client_manager_pkey PRIMARY KEY (client_manager_id);


--
-- Name: cost_history cost_history_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cost_history
    ADD CONSTRAINT cost_history_pkey PRIMARY KEY (cost_history_id);


--
-- Name: dz dz_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.dz
    ADD CONSTRAINT dz_pkey PRIMARY KEY (dz_id);


--
-- Name: dz_ps dz_ps_dz_id_pledge_subject_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.dz_ps
    ADD CONSTRAINT dz_ps_dz_id_pledge_subject_id_key UNIQUE (dz_id, pledge_subject_id);


--
-- Name: dz_ps dz_ps_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.dz_ps
    ADD CONSTRAINT dz_ps_pk PRIMARY KEY (id);


--
-- Name: employee employee_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.employee
    ADD CONSTRAINT employee_pkey PRIMARY KEY (employee_id);



--
-- Name: insurance insurance_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.insurance
    ADD CONSTRAINT insurance_pkey PRIMARY KEY (insurance_id);


--
-- Name: kd_dz kd_dz_kd_id_dz_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.kd_dz
    ADD CONSTRAINT kd_dz_kd_id_dz_id_key UNIQUE (kd_id, dz_id);


--
-- Name: kd_dz kd_dz_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.kd_dz
    ADD CONSTRAINT kd_dz_pk PRIMARY KEY (id);


--
-- Name: kd kd_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.kd
    ADD CONSTRAINT kd_pkey PRIMARY KEY (kd_id);


--
-- Name: monitoring monitoring_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.monitoring
    ADD CONSTRAINT monitoring_pkey PRIMARY KEY (monitoring_id);


--
-- Name: client_individual pk_client_individual; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.client_individual
    ADD CONSTRAINT pk_client_individual PRIMARY KEY (client_id);


--
-- Name: client_legal_entity pk_client_legal_entity; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.client_legal_entity
    ADD CONSTRAINT pk_client_legal_entity PRIMARY KEY (client_id);


--
-- Name: client_prime pk_client_prime_id; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.client_prime
    ADD CONSTRAINT pk_client_prime_id PRIMARY KEY (client_id);


--
-- Name: encumbrance pk_encumbrance; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.encumbrance
    ADD CONSTRAINT pk_encumbrance PRIMARY KEY (encumbrance_id);


--
-- Name: pledge_tbo pledge_TBO_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pledge_tbo
    ADD CONSTRAINT "pledge_TBO_pkey" PRIMARY KEY (pledge_subject_id);


--
-- Name: pledge_realty_room pledge__realty_room_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pledge_realty_room
    ADD CONSTRAINT pledge__realty_room_pkey PRIMARY KEY (pledge_subject_id);


--
-- Name: pledge_auto pledge_auto_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pledge_auto
    ADD CONSTRAINT pledge_auto_pkey PRIMARY KEY (pledge_subject_id);


--
-- Name: pledge_equipment pledge_equipment_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pledge_equipment
    ADD CONSTRAINT pledge_equipment_pkey PRIMARY KEY (pledge_subject_id);


--
-- Name: pledge_realty_building pledge_realty_building_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pledge_realty_building
    ADD CONSTRAINT pledge_realty_building_pkey PRIMARY KEY (pledge_subject_id);


--
-- Name: pledge_realty_land_lease pledge_realty_land_lease_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pledge_realty_land_lease
    ADD CONSTRAINT pledge_realty_land_lease_pkey PRIMARY KEY (pledge_subject_id);


--
-- Name: pledge_realty_land_ownership pledge_realty_land_ownership_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pledge_realty_land_ownership
    ADD CONSTRAINT pledge_realty_land_ownership_pkey PRIMARY KEY (pledge_subject_id);


--
-- Name: pledge_securities pledge_securities_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pledge_securities
    ADD CONSTRAINT pledge_securities_pkey PRIMARY KEY (pledge_subject_id);


--
-- Name: pledge_vessel pledge_vessel_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pledge_vessel
    ADD CONSTRAINT pledge_vessel_pk PRIMARY KEY (pledge_subject_id);


--
-- Name: pledge_subject ps_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pledge_subject
    ADD CONSTRAINT ps_pkey PRIMARY KEY (pledge_subject_id);



--
-- Name: cost_history trigger_cost_history; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER trigger_cost_history BEFORE INSERT OR UPDATE ON public.cost_history FOR EACH ROW EXECUTE PROCEDURE public.trigger_conclusion();


--
-- Name: dz_ps trigger_delete_costs_dz; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER trigger_delete_costs_dz BEFORE UPDATE ON public.dz_ps FOR EACH ROW EXECUTE PROCEDURE public.delete_costs_dz();


--
-- Name: dz_ps trigger_insert_costs_dz; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER trigger_insert_costs_dz BEFORE UPDATE ON public.dz_ps FOR EACH ROW EXECUTE PROCEDURE public.insert_costs_dz();


--
-- Name: monitoring trigger_monitoring; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER trigger_monitoring BEFORE INSERT OR UPDATE ON public.monitoring FOR EACH ROW EXECUTE PROCEDURE public.trigger_monitoring();


--
-- Name: pledge_subject trigger_update_costs_dz; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER trigger_update_costs_dz AFTER UPDATE OF zs_dz, zs_zz, rs_dz, rs_zz, ss ON public.pledge_subject FOR EACH ROW EXECUTE PROCEDURE public.update_costs_dz();


--
-- Name: client_individual fk_client_individual_client_prime; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.client_individual
    ADD CONSTRAINT fk_client_individual_client_prime FOREIGN KEY (client_id) REFERENCES public.client_prime(client_id);


--
-- Name: client_legal_entity fk_client_legal_entity_client_prime; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.client_legal_entity
    ADD CONSTRAINT fk_client_legal_entity_client_prime FOREIGN KEY (client_id) REFERENCES public.client_prime(client_id);


--
-- Name: client_prime fk_client_prime_client_mamager; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.client_prime
    ADD CONSTRAINT fk_client_prime_client_mamager FOREIGN KEY (client_manager_id) REFERENCES public.client_manager(client_manager_id);


--
-- Name: client_prime fk_client_prime_employee; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.client_prime
    ADD CONSTRAINT fk_client_prime_employee FOREIGN KEY (employee_id) REFERENCES public.employee(employee_id);


--
-- Name: dz fk_dz_client_prime; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.dz
    ADD CONSTRAINT fk_dz_client_prime FOREIGN KEY (pledgor_id) REFERENCES public.client_prime(client_id);


--
-- Name: dz_ps fk_dz_ps_dz_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.dz_ps
    ADD CONSTRAINT fk_dz_ps_dz_id FOREIGN KEY (dz_id) REFERENCES public.dz(dz_id);


--
-- Name: dz_ps fk_dz_ps_ps_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.dz_ps
    ADD CONSTRAINT fk_dz_ps_ps_id FOREIGN KEY (pledge_subject_id) REFERENCES public.pledge_subject(pledge_subject_id);


--
-- Name: encumbrance fk_encumbrance_pledgesubject; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.encumbrance
    ADD CONSTRAINT fk_encumbrance_pledgesubject FOREIGN KEY (pledgesubject_id) REFERENCES public.pledge_subject(pledge_subject_id);


--
-- Name: kd fk_kd_client_prime; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.kd
    ADD CONSTRAINT fk_kd_client_prime FOREIGN KEY (loaner_id) REFERENCES public.client_prime(client_id);


--
-- Name: monitoring fk_monitoring_pledge_subject; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.monitoring
    ADD CONSTRAINT fk_monitoring_pledge_subject FOREIGN KEY (pledge_subject_id) REFERENCES public.pledge_subject(pledge_subject_id);


--
-- Name: pledge_tbo fk_pledgeSubject_id_TBO; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pledge_tbo
    ADD CONSTRAINT "fk_pledgeSubject_id_TBO" FOREIGN KEY (pledge_subject_id) REFERENCES public.pledge_subject(pledge_subject_id);


--
-- Name: pledge_auto fk_pledgeSubject_id_auto; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pledge_auto
    ADD CONSTRAINT "fk_pledgeSubject_id_auto" FOREIGN KEY (pledge_subject_id) REFERENCES public.pledge_subject(pledge_subject_id);


--
-- Name: pledge_equipment fk_pledgeSubject_id_equip; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pledge_equipment
    ADD CONSTRAINT "fk_pledgeSubject_id_equip" FOREIGN KEY (pledge_subject_id) REFERENCES public.pledge_subject(pledge_subject_id);


--
-- Name: pledge_securities fk_pledgeSubject_id_securities; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pledge_securities
    ADD CONSTRAINT "fk_pledgeSubject_id_securities" FOREIGN KEY (pledge_subject_id) REFERENCES public.pledge_subject(pledge_subject_id);


--
-- Name: pledge_realty_building fk_pledge_subject_id_build_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pledge_realty_building
    ADD CONSTRAINT fk_pledge_subject_id_build_id FOREIGN KEY (pledge_subject_id) REFERENCES public.pledge_subject(pledge_subject_id);


--
-- Name: pledge_realty_land_lease fk_pledge_subject_id_land_lease_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pledge_realty_land_lease
    ADD CONSTRAINT fk_pledge_subject_id_land_lease_id FOREIGN KEY (pledge_subject_id) REFERENCES public.pledge_subject(pledge_subject_id);


--
-- Name: pledge_realty_land_ownership fk_pledge_subject_id_land_own_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pledge_realty_land_ownership
    ADD CONSTRAINT fk_pledge_subject_id_land_own_id FOREIGN KEY (pledge_subject_id) REFERENCES public.pledge_subject(pledge_subject_id);


--
-- Name: pledge_realty_room fk_pledge_subject_id_room_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pledge_realty_room
    ADD CONSTRAINT fk_pledge_subject_id_room_id FOREIGN KEY (pledge_subject_id) REFERENCES public.pledge_subject(pledge_subject_id);


--
-- Name: insurance fk_pledgesubject_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.insurance
    ADD CONSTRAINT fk_pledgesubject_id FOREIGN KEY (pledgesubject_id) REFERENCES public.pledge_subject(pledge_subject_id);


--
-- Name: cost_history fk_pledgesubject_id_cost; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cost_history
    ADD CONSTRAINT fk_pledgesubject_id_cost FOREIGN KEY (pledge_subject_id) REFERENCES public.pledge_subject(pledge_subject_id);


--
-- Name: pledge_vessel fk_pledgesubject_id_vessel; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pledge_vessel
    ADD CONSTRAINT fk_pledgesubject_id_vessel FOREIGN KEY (pledge_subject_id) REFERENCES public.pledge_subject(pledge_subject_id);


--
-- Name: kd_dz kd_dz_new_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.kd_dz
    ADD CONSTRAINT kd_dz_new_fk FOREIGN KEY (kd_id) REFERENCES public.kd(kd_id);


--
-- Name: kd_dz kd_dz_new_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.kd_dz
    ADD CONSTRAINT kd_dz_new_fk_1 FOREIGN KEY (dz_id) REFERENCES public.dz(dz_id);

