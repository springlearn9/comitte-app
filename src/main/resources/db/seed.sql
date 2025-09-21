-- create tables minimal (if using Hibernate ddl-auto=update these may be optional)
CREATE TABLE IF NOT EXISTS roles (role_id serial PRIMARY KEY, role_name varchar(100) UNIQUE NOT NULL);
CREATE TABLE IF NOT EXISTS authorities (authority_id serial PRIMARY KEY, authority_name varchar(100) UNIQUE NOT NULL);
CREATE TABLE IF NOT EXISTS role_authority_map (role_id int NOT NULL, authority_id int NOT NULL);
CREATE TABLE IF NOT EXISTS users (id serial PRIMARY KEY, username varchar(100) UNIQUE, email varchar(200) UNIQUE, password varchar(500), mobile_number varchar(20), created_timestamp timestamp, updated_timestamp timestamp);
CREATE TABLE IF NOT EXISTS user_role_map (user_id int NOT NULL, role_id int NOT NULL);
CREATE TABLE IF NOT EXISTS member (member_id serial PRIMARY KEY, name varchar(200), mobile varchar(50), aadhar_no varchar(50), address text, created_timestamp timestamp, updated_timestamp timestamp);
CREATE TABLE IF NOT EXISTS comitte (comitte_id serial PRIMARY KEY, owner_id int, comitte_name varchar(255), start_date date, full_amount numeric, members_count int, full_share numeric, due_date_days int, payment_date_days int, created_timestamp timestamp, updated_timestamp timestamp);
CREATE TABLE IF NOT EXISTS comitte_member_map (id serial PRIMARY KEY, comitte_id int, member_id int, share_count int, created_timestamp timestamp, updated_timestamp timestamp);
CREATE TABLE IF NOT EXISTS comitte_bid (id serial PRIMARY KEY, comitte_id int, comitte_number int, final_bidder int, final_bid_amt numeric, bid_date timestamp, bids jsonb, receivers_list jsonb, created_timestamp timestamp, updated_timestamp timestamp);

-- seed roles/authorities
INSERT INTO roles (role_name) VALUES ('ADMIN') ON CONFLICT DO NOTHING;
INSERT INTO roles (role_name) VALUES ('COMITTE_OWNER') ON CONFLICT DO NOTHING;
INSERT INTO roles (role_name) VALUES ('MEMBER') ON CONFLICT DO NOTHING;

INSERT INTO authorities (authority_name) VALUES ('USER_CREATE') ON CONFLICT DO NOTHING;
INSERT INTO authorities (authority_name) VALUES ('COMITTE_CREATE') ON CONFLICT DO NOTHING;
INSERT INTO authorities (authority_name) VALUES ('BID_CREATE') ON CONFLICT DO NOTHING;
INSERT INTO authorities (authority_name) VALUES ('USER_READ') ON CONFLICT DO NOTHING;
INSERT INTO authorities (authority_name) VALUES ('USER_DELETE') ON CONFLICT DO NOTHING;

-- map roles to authorities simple
INSERT INTO role_authority_map (role_id, authority_id) VALUES (1,1) ON CONFLICT DO NOTHING;
INSERT INTO role_authority_map (role_id, authority_id) VALUES (1,2) ON CONFLICT DO NOTHING;
INSERT INTO role_authority_map (role_id, authority_id) VALUES (1,3) ON CONFLICT DO NOTHING;

-- sample users (passwords are bcrypt of 'password' generated using $2a$10$... placeholder)
INSERT INTO users (username, email, password, mobile_number, created_timestamp, updated_timestamp) VALUES
('admin','admin@example.com','$2a$10$u1e8q9abcdef...','9999999999', now(), now())
ON CONFLICT DO NOTHING;
-- map admin to ADMIN role (role_id 1 assumed)
INSERT INTO user_role_map (user_id, role_id) VALUES (1,1) ON CONFLICT DO NOTHING;
