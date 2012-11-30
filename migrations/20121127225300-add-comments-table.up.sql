CREATE TABLE comments (id serial PRIMARY KEY,
                       text varchar(250) NOT NULL,
                       rating_id varchar(100) REFERENCES rating(foursquare_id),
                       created_at timestamp default current_timestamp);
