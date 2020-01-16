ALTER TABLE public.pledge_vessel ALTER COLUMN imo TYPE varchar USING imo::varchar;
ALTER TABLE public.pledge_vessel ALTER COLUMN mmsi TYPE varchar USING mmsi::varchar;