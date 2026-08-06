-- Generated from .local/db/001_schema.sql (pagila) adapted for Flyway.

CREATE VIEW public.actor_info AS
 SELECT a.actor_id,
    a.first_name,
    a.last_name,
    jsonb_object_agg(c.name, ( SELECT array_agg(f.title) AS array_agg
           FROM ((public.film f
             JOIN public.film_category fc_1 ON ((f.film_id = fc_1.film_id)))
             JOIN public.film_actor fa_1 ON ((f.film_id = fa_1.film_id)))
          WHERE ((fc_1.category_id = c.category_id) AND (fa_1.actor_id = a.actor_id))
          GROUP BY fa_1.actor_id)) FILTER (WHERE (c.name IS NOT NULL)) AS film_info
   FROM (((public.actor a
     LEFT JOIN public.film_actor fa ON ((a.actor_id = fa.actor_id)))
     LEFT JOIN public.film_category fc ON ((fa.film_id = fc.film_id)))
     LEFT JOIN public.category c ON ((fc.category_id = c.category_id)))
  GROUP BY a.actor_id, a.first_name, a.last_name;


CREATE VIEW public.customer_list AS
 SELECT cu.customer_id AS id,
    ((cu.first_name || ' '::text) || cu.last_name) AS name,
    a.address,
    a.postal_code AS "zip code",
    a.phone,
    city.city,
    country.country,
        CASE
            WHEN cu.activebool THEN 'active'::text
            ELSE ''::text
        END AS notes,
    cu.store_id AS sid
   FROM (((public.customer cu
     JOIN public.address a ON ((cu.address_id = a.address_id)))
     JOIN public.city ON ((a.city_id = city.city_id)))
     JOIN public.country ON ((city.country_id = country.country_id)));


CREATE VIEW public.film_list AS
 SELECT film.film_id AS fid,
    film.title,
    film.description,
    category.name AS category,
    film.rental_rate AS price,
    film.length,
    film.rating,
    public.group_concat(((actor.first_name || ' '::text) || actor.last_name)) AS actors
   FROM ((((public.category
     LEFT JOIN public.film_category ON ((category.category_id = film_category.category_id)))
     LEFT JOIN public.film ON ((film_category.film_id = film.film_id)))
     JOIN public.film_actor ON ((film.film_id = film_actor.film_id)))
     JOIN public.actor ON ((film_actor.actor_id = actor.actor_id)))
  GROUP BY film.film_id, film.title, film.description, category.name, film.rental_rate, film.length, film.rating;


CREATE VIEW public.nicer_but_slower_film_list AS
 SELECT film.film_id AS fid,
    film.title,
    film.description,
    category.name AS category,
    film.rental_rate AS price,
    film.length,
    film.rating,
    public.group_concat((((upper("substring"(actor.first_name, 1, 1)) || lower("substring"(actor.first_name, 2))) || upper("substring"(actor.last_name, 1, 1))) || lower("substring"(actor.last_name, 2)))) AS actors
   FROM ((((public.category
     LEFT JOIN public.film_category ON ((category.category_id = film_category.category_id)))
     LEFT JOIN public.film ON ((film_category.film_id = film.film_id)))
     JOIN public.film_actor ON ((film.film_id = film_actor.film_id)))
     JOIN public.actor ON ((film_actor.actor_id = actor.actor_id)))
  GROUP BY film.film_id, film.title, film.description, category.name, film.rental_rate, film.length, film.rating;


CREATE MATERIALIZED VIEW public.rental_by_category AS
 SELECT c.name AS category,
    sum(p.amount) AS total_sales
   FROM (((((public.payment p
     JOIN public.rental r ON ((p.rental_id = r.rental_id)))
     JOIN public.inventory i ON ((r.inventory_id = i.inventory_id)))
     JOIN public.film f ON ((i.film_id = f.film_id)))
     JOIN public.film_category fc ON ((f.film_id = fc.film_id)))
     JOIN public.category c ON ((fc.category_id = c.category_id)))
  GROUP BY c.name
  ORDER BY (sum(p.amount)) DESC
  WITH NO DATA;


CREATE VIEW public.sales_by_film_category AS
 SELECT c.name AS category,
    sum(p.amount) AS total_sales
   FROM (((((public.payment p
     JOIN public.rental r ON ((p.rental_id = r.rental_id)))
     JOIN public.inventory i ON ((r.inventory_id = i.inventory_id)))
     JOIN public.film f ON ((i.film_id = f.film_id)))
     JOIN public.film_category fc ON ((f.film_id = fc.film_id)))
     JOIN public.category c ON ((fc.category_id = c.category_id)))
  GROUP BY c.name
  ORDER BY (sum(p.amount)) DESC;


CREATE VIEW public.sales_by_store AS
 SELECT ((c.city || ','::text) || cy.country) AS store,
    ((m.first_name || ' '::text) || m.last_name) AS manager,
    sum(p.amount) AS total_sales
   FROM (((((((public.payment p
     JOIN public.rental r ON ((p.rental_id = r.rental_id)))
     JOIN public.inventory i ON ((r.inventory_id = i.inventory_id)))
     JOIN public.store s ON ((i.store_id = s.store_id)))
     JOIN public.address a ON ((s.address_id = a.address_id)))
     JOIN public.city c ON ((a.city_id = c.city_id)))
     JOIN public.country cy ON ((c.country_id = cy.country_id)))
     JOIN public.staff m ON ((s.manager_staff_id = m.staff_id)))
  GROUP BY cy.country, c.city, s.store_id, m.first_name, m.last_name
  ORDER BY cy.country, c.city;


CREATE VIEW public.staff_list AS
 SELECT s.staff_id AS id,
    ((s.first_name || ' '::text) || s.last_name) AS name,
    a.address,
    a.postal_code AS "zip code",
    a.phone,
    city.city,
    country.country,
    s.store_id AS sid
   FROM (((public.staff s
     JOIN public.address a ON ((s.address_id = a.address_id)))
     JOIN public.city ON ((a.city_id = city.city_id)))
     JOIN public.country ON ((city.country_id = country.country_id)));


CREATE INDEX film_fulltext_idx ON public.film USING gist (fulltext);


CREATE INDEX idx_actor_last_name ON public.actor USING btree (last_name);


CREATE INDEX idx_fk_address_id ON public.customer USING btree (address_id);


CREATE INDEX idx_fk_city_id ON public.address USING btree (city_id);


CREATE INDEX idx_fk_country_id ON public.city USING btree (country_id);


CREATE INDEX idx_fk_film_id ON public.film_actor USING btree (film_id);


CREATE INDEX film_embedding_embedding_hnsw_idx ON public.film_embedding USING hnsw (embedding public.vector_cosine_ops);


CREATE INDEX idx_fk_inventory_id ON public.rental USING btree (inventory_id);


CREATE INDEX idx_fk_language_id ON public.film USING btree (language_id);


CREATE INDEX idx_fk_original_language_id ON public.film USING btree (original_language_id);


CREATE INDEX idx_fk_payment_p2022_01_customer_id ON public.payment_p2022_01 USING btree (customer_id);


CREATE INDEX idx_fk_payment_p2022_01_staff_id ON public.payment_p2022_01 USING btree (staff_id);


CREATE INDEX idx_fk_payment_p2022_02_customer_id ON public.payment_p2022_02 USING btree (customer_id);


CREATE INDEX idx_fk_payment_p2022_02_staff_id ON public.payment_p2022_02 USING btree (staff_id);


CREATE INDEX idx_fk_payment_p2022_03_customer_id ON public.payment_p2022_03 USING btree (customer_id);


CREATE INDEX idx_fk_payment_p2022_03_staff_id ON public.payment_p2022_03 USING btree (staff_id);


CREATE INDEX idx_fk_payment_p2022_04_customer_id ON public.payment_p2022_04 USING btree (customer_id);


CREATE INDEX idx_fk_payment_p2022_04_staff_id ON public.payment_p2022_04 USING btree (staff_id);


CREATE INDEX idx_fk_payment_p2022_05_customer_id ON public.payment_p2022_05 USING btree (customer_id);


CREATE INDEX idx_fk_payment_p2022_05_staff_id ON public.payment_p2022_05 USING btree (staff_id);


CREATE INDEX idx_fk_payment_p2022_06_customer_id ON public.payment_p2022_06 USING btree (customer_id);


CREATE INDEX idx_fk_payment_p2022_06_staff_id ON public.payment_p2022_06 USING btree (staff_id);


CREATE INDEX idx_fk_store_id ON public.customer USING btree (store_id);


CREATE INDEX idx_last_name ON public.customer USING btree (last_name);


CREATE INDEX idx_store_id_film_id ON public.inventory USING btree (store_id, film_id);


CREATE INDEX idx_title ON public.film USING btree (title);


CREATE UNIQUE INDEX idx_unq_manager_staff_id ON public.store USING btree (manager_staff_id);


CREATE UNIQUE INDEX idx_unq_rental_rental_date_inventory_id_customer_id ON public.rental USING btree (rental_date, inventory_id, customer_id);


CREATE INDEX payment_p2022_01_customer_id_idx ON public.payment_p2022_01 USING btree (customer_id);


CREATE INDEX payment_p2022_02_customer_id_idx ON public.payment_p2022_02 USING btree (customer_id);


CREATE INDEX payment_p2022_03_customer_id_idx ON public.payment_p2022_03 USING btree (customer_id);


CREATE INDEX payment_p2022_04_customer_id_idx ON public.payment_p2022_04 USING btree (customer_id);


CREATE INDEX payment_p2022_05_customer_id_idx ON public.payment_p2022_05 USING btree (customer_id);


CREATE INDEX payment_p2022_06_customer_id_idx ON public.payment_p2022_06 USING btree (customer_id);


CREATE UNIQUE INDEX rental_category ON public.rental_by_category USING btree (category);


CREATE UNIQUE INDEX customer_uuid_key ON public.customer USING btree (uuid);


CREATE UNIQUE INDEX rental_uuid_key ON public.rental USING btree (uuid);


CREATE UNIQUE INDEX payment_uuid_key ON public.payment USING btree (uuid, payment_date);


CREATE TRIGGER film_fulltext_trigger BEFORE INSERT OR UPDATE ON public.film FOR EACH ROW EXECUTE FUNCTION tsvector_update_trigger('fulltext', 'pg_catalog.english', 'title', 'description');


CREATE TRIGGER last_updated BEFORE UPDATE ON public.actor FOR EACH ROW EXECUTE FUNCTION public.last_updated();


CREATE TRIGGER last_updated BEFORE UPDATE ON public.address FOR EACH ROW EXECUTE FUNCTION public.last_updated();


CREATE TRIGGER last_updated BEFORE UPDATE ON public.category FOR EACH ROW EXECUTE FUNCTION public.last_updated();


CREATE TRIGGER last_updated BEFORE UPDATE ON public.city FOR EACH ROW EXECUTE FUNCTION public.last_updated();


CREATE TRIGGER last_updated BEFORE UPDATE ON public.country FOR EACH ROW EXECUTE FUNCTION public.last_updated();


CREATE TRIGGER last_updated BEFORE UPDATE ON public.customer FOR EACH ROW EXECUTE FUNCTION public.last_updated();


CREATE TRIGGER last_updated BEFORE UPDATE ON public.film FOR EACH ROW EXECUTE FUNCTION public.last_updated();


CREATE TRIGGER last_updated BEFORE UPDATE ON public.film_actor FOR EACH ROW EXECUTE FUNCTION public.last_updated();


CREATE TRIGGER last_updated BEFORE UPDATE ON public.film_category FOR EACH ROW EXECUTE FUNCTION public.last_updated();


CREATE TRIGGER last_updated BEFORE UPDATE ON public.film_embedding FOR EACH ROW EXECUTE FUNCTION public.last_updated();


CREATE TRIGGER last_updated BEFORE UPDATE ON public.inventory FOR EACH ROW EXECUTE FUNCTION public.last_updated();


CREATE TRIGGER last_updated BEFORE UPDATE ON public.language FOR EACH ROW EXECUTE FUNCTION public.last_updated();


CREATE TRIGGER last_updated BEFORE UPDATE ON public.rental FOR EACH ROW EXECUTE FUNCTION public.last_updated();


CREATE TRIGGER last_updated BEFORE UPDATE ON public.staff FOR EACH ROW EXECUTE FUNCTION public.last_updated();


CREATE TRIGGER last_updated BEFORE UPDATE ON public.store FOR EACH ROW EXECUTE FUNCTION public.last_updated();

