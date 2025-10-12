-- drop table bids;
-- drop table comitte_member_map;
-- drop table user_role_map
-- drop table comittes;
-- drop table members;

-- select * from members;
-- select member_id, email, name from members;
-- select * from comittes;
-- select comitte_id, owner_id, comitte_name from comittes;
-- select * from comitte_member_map;
-- select * from bids;


INSERT INTO members (
	username, email, password, mobile, name, 
	"aadhar_no", address, created_timestamp, updated_timestamp
) VALUES
('bippan.khichra', 'bippan.k@example.com', 'password123', '8449761645', 'Bippan Khichra', '513022260012', '123 Kirti Nagar, New Delhi', NOW(), NOW()),
('vikas.rajaura', 'vikas.r@example.com', 'password123', '7827593310', 'Vikas Rajaura', '513022260032', '456 Sector 18, Noida', NOW(), NOW()),
('harendra.singh', 'harendra.s@example.com', 'password123', '8505848064', 'Harendra Singh', '608512345678', '789 Malviya Nagar, Jaipur', NOW(), NOW()),
('sundar.tyagi', 'sundar.t@example.com', 'password123', '9917786273', 'Sundar Tyagi', '608598765432', '101 Civil Lines, Meerut', NOW(), NOW()),
('ajit.kastley', 'ajit.k@example.com', 'password123', '9557285517', 'Ajit Kastley', '337511223344', '212 Rajpur Road, Dehradun', NOW(), NOW()),
('sanjay.tyagi', 'sanjay.t@example.com', 'password123', '9876543210', 'Sanjay Tyagi', '063199887766', '333 Gandhi Chowk, Ghaziabad', NOW(), NOW()),
('priya.sharma', 'priya.s@example.com', 'password123', '9988776655', 'Priya Sharma', '778899001122', '5th Avenue, Gurgaon', NOW(), NOW()),
('rahul.verma', 'rahul.v@example.com', 'password123', '9123456789', 'Rahul Verma', '112233445566', 'Lajpat Nagar, New Delhi', NOW(), NOW()),
('anisha.gupta', 'anisha.g@example.com', 'password123', '9555666777', 'Anisha Gupta', '445566778899', 'Sector 15, Chandigarh', NOW(), NOW()),
('mohan.joshi', 'mohan.j@example.com', 'password123', '8877665544', 'Mohan Joshi', '998877665544', '44B Park Street, Kolkata', NOW(), NOW());


INSERT INTO comittes (
		owner_id, comitte_name, start_date, full_amount, members_count, full_share, 
		due_date_days, payment_date_days, created_timestamp, updated_timestamp
) VALUES
(1, '2.0L 15Jan24 Bippan', '2024-01-15', 200000, 16, 12500, 5, 7, NOW(), NOW()),
(1, '2.0L 05Mar24 Bippan', '2024-03-05', 200000, 16, 12500, 5, 7, NOW(), NOW()),
(5, '1.05L 10Jan24 Ajit', '2024-01-10', 105000, 15, 7000, 5, 7, NOW(), NOW()),
(5, '1.05L 15Jan24 Ajit', '2024-01-15', 105000, 15, 7000, 5, 7, NOW(), NOW()),
(6, '1.05L 10Dec24 Sundar', '2024-12-10', 105000, 15, 7000, 5, 7, NOW(), NOW()),
(6, '2.40L 10Nov24 Sanjay', '2024-11-10', 240000, 15, 16000, 5, 7, NOW(), NOW()),
(7, '1.50L 10Mar25 Sanjay', '2025-03-10', 150000, 15, 10000, 8, 10, NOW(), NOW()),
(4, 'Sundar', '2025-07-10', 150000, 15, 10000, 5, 5, NOW(), NOW());



INSERT INTO comitte_member_map (
	comitte_id, member_id, share_count,
	created_timestamp, updated_timestamp
) values
(1,	2, 1, NOW(), NOW()),
(2,	2, 1, NOW(), NOW()),
(3,	2, 1, NOW(), NOW()),
(4,	2, 1, NOW(), NOW()),
(5,	2, 1, NOW(), NOW()),
(6,	2, 1, NOW(), NOW()),
(5,	3, 1, NOW(), NOW()),
(6,	3, 1, NOW(), NOW()),
(5,	4, 1, NOW(), NOW()),
(6,	6, 1, NOW(), NOW()),
(1,	2, 1, NOW(), NOW()),
(2,	2, 1, NOW(), NOW()),
(7,	2, 1, NOW(), NOW()),
(7,	3, 1, NOW(), NOW()),
(7,	6, 1, NOW(), NOW()),
(8,	2, 1, NOW(), NOW()),
(8,	4, 1, NOW(), NOW());



INSERT INTO bids (comitte_id, comitte_number, final_bidder, final_bid_amt, bid_date, created_timestamp, updated_timestamp)
VALUES
(1, 1, NULL, 0, '2024-01-15', NOW(), NOW()),
(1, 2, NULL, 50000, '2024-02-15', NOW(), NOW()),
(1, 3, NULL, 60000, '2024-03-15', NOW(), NOW()),
(1, 4, NULL, 50000, '2024-04-15', NOW(), NOW()),
(1, 5, NULL, 29500, '2024-05-15', NOW(), NOW()),
(1, 6, NULL, 20000, '2024-06-15', NOW(), NOW()),
(1, 7, NULL, 24000, '2024-07-15', NOW(), NOW()),
(1, 8, 2, 18000, '2024-08-15', NOW(), NOW()),
(1, 9, NULL, 21000, '2024-09-15', NOW(), NOW()),
(1, 10, NULL, 12000, '2024-10-15', NOW(), NOW()),
(1, 11, NULL, 15000, '2024-11-15', NOW(), NOW()),
(1, 12, NULL, 10000, '2024-12-15', NOW(), NOW()),
(1, 13, NULL, 8000, '2025-01-19', NOW(), NOW()),
(1, 14, NULL, 6000, '2025-02-18', NOW(), NOW()),
(1, 15, NULL, 4000, '2025-03-15', NOW(), NOW()),
(1, 16, NULL, 2000, '2025-04-11', NOW(), NOW()),

(2, 1, NULL, 0, '2024-03-05', NOW(), NOW()),
(2, 2, NULL, 65000, '2024-04-05', NOW(), NOW()),
(2, 3, NULL, 50000, '2024-05-05', NOW(), NOW()),
(2, 4, NULL, 40000, '2024-06-05', NOW(), NOW()),
(2, 5, NULL, 26000, '2024-07-05', NOW(), NOW()),
(2, 6, NULL, 30000, '2024-08-05', NOW(), NOW()),
(2, 7, NULL, 25000, '2024-09-05', NOW(), NOW()),
(2, 8, NULL, 21000, '2024-10-05', NOW(), NOW()),
(2, 9, 2, 16000, '2024-11-05', NOW(), NOW()),
(2, 10, 2, 14000, '2024-12-05', NOW(), NOW()),
(2, 11, NULL, 12000, '2025-01-05', NOW(), NOW()),
(2, 12, NULL, 10000, '2025-02-10', NOW(), NOW()),
(2, 13, NULL, 8000, '2025-03-11', NOW(), NOW()),
(2, 14, NULL, 6000, '2025-04-13', NOW(), NOW()),

(3, 1, NULL, 0, '2024-01-10', NOW(), NOW()),
(3, 2, NULL, 27000, '2024-02-10', NOW(), NOW()),
(3, 3, NULL, 21000, '2024-03-10', NOW(), NOW()),
(3, 4, NULL, 18500, '2024-04-10', NOW(), NOW()),
(3, 5, NULL, 16000, '2024-05-10', NOW(), NOW()),
(3, 6, NULL, 14000, '2024-06-10', NOW(), NOW()),
(3, 7, NULL, 12000, '2024-07-10', NOW(), NOW()),
(3, 8, 2, 10500, '2024-08-10', NOW(), NOW()),
(3, 9, NULL, 9000, '2024-09-10', NOW(), NOW()),
(3, 10, NULL, 7500, '2024-10-10', NOW(), NOW()),
(3, 11, NULL, 6000, '2024-11-10', NOW(), NOW()),
(3, 12, NULL, 4500, '2024-12-10', NOW(), NOW()),
(3, 13, NULL, 3000, '2025-01-10', NOW(), NOW()),
(3, 14, NULL, 1500, '2025-02-10', NOW(), NOW()),
(3, 15, NULL, 0, '2025-03-10', NOW(), NOW()),

(4, 1, NULL, 22500, '2024-01-15', NOW(), NOW()),
(4, 2, NULL, 0, '2024-02-15', NOW(), NOW()),
(4, 3, NULL, 27000, '2024-03-15', NOW(), NOW()),
(4, 4, NULL, 17500, '2024-04-15', NOW(), NOW()),
(4, 5, NULL, 15500, '2024-05-15', NOW(), NOW()),
(4, 6, NULL, 13500, '2024-06-15', NOW(), NOW()),
(4, 7, NULL, 12000, '2024-07-15', NOW(), NOW()),
(4, 8, NULL, 10500, '2024-08-15', NOW(), NOW()),
(4, 9, NULL, 9000, '2024-09-15', NOW(), NOW()),
(4, 10, 2, 7500, '2024-10-15', NOW(), NOW()),
(4, 11, NULL, 6000, '2024-11-15', NOW(), NOW()),
(4, 12, NULL, 4500, '2024-12-15', NOW(), NOW()),
(4, 13, NULL, 3000, '2025-01-15', NOW(), NOW()),
(4, 14, NULL, 1500, '2025-02-15', NOW(), NOW()),
(4, 15, NULL, 0, '2025-03-15', NOW(), NOW()),

(5, 1, NULL, 27000, '2024-12-10', NOW(), NOW()),
(5, 2, NULL, 0, '2025-01-10', NOW(), NOW()),
(5, 3, 3, 25000, '2025-02-12', NOW(), NOW()),
(5, 4, NULL, 20500, '2025-03-11', NOW(), NOW()),
(5, 5, NULL, 17500, '2025-04-10', NOW(), NOW()),
(5, 6, NULL, 13600, '2025-05-10', NOW(), NOW()),
(5, 7, NULL, 12200, '2025-06-12', NOW(), NOW()),
(5, 8, NULL, 11000, '2025-07-10', NOW(), NOW()),
(5, 9, NULL, 12200, '2025-08-10', NOW(), NOW()),
(5, 10, NULL, 8000, '2025-09-10', NOW(), NOW()),

(6, 1, NULL, 0, '2024-11-10', NOW(), NOW()),
(6, 2, NULL, 70500, '2024-12-10', NOW(), NOW()),
(6, 3, NULL, 77000, '2025-01-10', NOW(), NOW()),
(6, 4, NULL, 65000, '2025-02-10', NOW(), NOW()),
(6, 5, NULL, 63000, '2025-03-10', NOW(), NOW()),
(6, 6, NULL, 58000, '2025-04-10', NOW(), NOW()),
(6, 7, NULL, 48000, '2025-05-10', NOW(), NOW()),
(6, 8, NULL, 41000, '2025-06-10', NOW(), NOW()),
(6, 9, NULL, 41000, '2025-07-10', NOW(), NOW()),
(6, 10, 4, 30000, '2025-08-10', NOW(), NOW()),
(6, 11, NULL, 24000, '2025-09-10', NOW(), NOW()),

(7, 1, 6, 0, '2025-03-10', NOW(), NOW()),
(7, 2, NULL, 38000, '2025-04-10', NOW(), NOW()),
(7, 3, NULL, 33000, '2025-05-16', NOW(), NOW()),
(7, 4, NULL, 30000, '2025-06-10', NOW(), NOW()),
(7, 5, 2, 27000, '2025-07-10', NOW(), NOW()),
(7, 6, NULL, 25000, '2025-08-10', NOW(), NOW()),
(7, 7, NULL, 23000, '2025-09-10', NOW(), NOW()),

(8, 1, 4, 0, '2025-07-10', NOW(), NOW()),
(8, 2, NULL, 32000, '2025-08-20', NOW(), NOW()),
(8, 3, NULL, 28000, '2025-09-22', NOW(), NOW());



-- seed roles/authorities
INSERT INTO roles (role_id, role_name) VALUES (1, 'MEMBER') ON CONFLICT DO NOTHING;
INSERT INTO roles (role_id, role_name) VALUES (2, 'ADMIN') ON CONFLICT DO NOTHING;

INSERT INTO authorities (authority_id, authority_name) VALUES (1, 'MEMBER_CREATE') ON CONFLICT DO NOTHING;
INSERT INTO authorities (authority_id, authority_name) VALUES (2, 'MEMBER_UPDATE') ON CONFLICT DO NOTHING;
INSERT INTO authorities (authority_id, authority_name) VALUES (3, 'MEMBER_DELETE') ON CONFLICT DO NOTHING;
INSERT INTO authorities (authority_id, authority_name) VALUES (4, 'MEMBER_VIEW') ON CONFLICT DO NOTHING;
INSERT INTO authorities (authority_id, authority_name) VALUES (5, 'MEMBER_SEARCH') ON CONFLICT DO NOTHING;

INSERT INTO authorities (authority_id, authority_name) VALUES (11, 'COMITTE_CREATE') ON CONFLICT DO NOTHING;
INSERT INTO authorities (authority_id, authority_name) VALUES (12, 'COMITTE_UPDATE') ON CONFLICT DO NOTHING;
INSERT INTO authorities (authority_id, authority_name) VALUES (13, 'COMITTE_DELETE') ON CONFLICT DO NOTHING;
INSERT INTO authorities (authority_id, authority_name) VALUES (14, 'COMITTE_VIEW') ON CONFLICT DO NOTHING;
INSERT INTO authorities (authority_id, authority_name) VALUES (15, 'COMITTE_SEARCH') ON CONFLICT DO NOTHING;
INSERT INTO authorities (authority_id, authority_name) VALUES (16, 'ASSIGN_MEMBER_IN_COMITTE') ON CONFLICT DO NOTHING;
INSERT INTO authorities (authority_id, authority_name) VALUES (17, 'GET_COMITTE_BIDS') ON CONFLICT DO NOTHING;


INSERT INTO authorities (authority_id, authority_name) VALUES (21, 'BID_CREATE') ON CONFLICT DO NOTHING;
INSERT INTO authorities (authority_id, authority_name) VALUES (22, 'BID_UPDATE') ON CONFLICT DO NOTHING;
INSERT INTO authorities (authority_id, authority_name) VALUES (23, 'BID_DELETE') ON CONFLICT DO NOTHING;
INSERT INTO authorities (authority_id, authority_name) VALUES (24, 'BID_VIEW') ON CONFLICT DO NOTHING;
INSERT INTO authorities (authority_id, authority_name) VALUES (25, 'BID_SEARCH') ON CONFLICT DO NOTHING;

INSERT INTO authorities (authority_id, authority_name) VALUES (31, 'COMITTE_MEMBER_MAP_CREATE') ON CONFLICT DO NOTHING;
INSERT INTO authorities (authority_id, authority_name) VALUES (32, 'COMITTE_MEMBER_MAP_UPDATE') ON CONFLICT DO NOTHING;
INSERT INTO authorities (authority_id, authority_name) VALUES (33, 'COMITTE_MEMBER_MAP_DELETE') ON CONFLICT DO NOTHING;
INSERT INTO authorities (authority_id, authority_name) VALUES (34, 'COMITTE_MEMBER_MAP_VIEW') ON CONFLICT DO NOTHING;
INSERT INTO authorities (authority_id, authority_name) VALUES (35, 'COMITTE_MEMBER_MAP_SEARCH') ON CONFLICT DO NOTHING;

-- map roles to authorities simple
insert into role_authority_map(role_id, authority_id) values
(1, 1),
(1, 2),
(1, 3),
(1, 4),
(1, 5),
(1, 11),
(1, 12),
(1, 13),
(1, 14),
(1, 15),
(1, 16),
(1, 17),
(1, 21),
(1, 22),
(1, 23),
(1, 24),
(1, 25),
(1, 31),
(1, 32),
(1, 33),
(1, 34),
(1, 35);

