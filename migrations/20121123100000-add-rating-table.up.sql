CREATE TABLE rating (id serial PRIMARY KEY,
                     foursquare_id varchar(100) UNIQUE,
                     name varchar(250) NOT NULL,
                     accessible boolean,
                     created_at timestamp default current_timestamp);
