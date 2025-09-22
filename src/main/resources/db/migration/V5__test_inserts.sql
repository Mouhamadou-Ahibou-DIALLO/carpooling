-- =====================================================
-- Migration V5: Test data inserts (corrected)
-- Carpooling App
-- =====================================================

-- ----------------------
-- USERS
-- ----------------------
INSERT INTO users (username, email, password, phone_number, role)
VALUES
    ('alice',  'alice@example.com',  'hashed_pwd_alice',  '+33601010101', 'USER'),
    ('bob',    'bob@example.com',    'hashed_pwd_bob',    '+33602020202', 'USER'),
    ('charlie','charlie@example.com','hashed_pwd_charlie','+33603030303', 'USER'),
    ('admin',  'admin@example.com',  'hashed_pwd_admin',  '+33699999999', 'ADMIN');

-- ----------------------
-- CARS (charlie is the driver)
-- ----------------------
INSERT INTO cars (user_id, brand, model, place_number)
SELECT id, 'Peugeot', '208', 3
FROM users
WHERE username = 'charlie'
    LIMIT 1;

-- ----------------------
-- TRIPS (Charlie posts a trip Paris -> Marseille)
-- ----------------------
INSERT INTO trips (driver_id, start_location, end_location, departure_time, time_trips, price, available_seats)
SELECT u.id, 'Paris', 'Marseille',
       NOW() + INTERVAL '1 day',
    NOW() + INTERVAL '10 hours',
    50.00,
    3
FROM users u
WHERE u.username = 'charlie'
    LIMIT 1;

-- ----------------------
-- WALLETS (initial balances)
-- ----------------------
INSERT INTO wallets (user_id, balance)
SELECT id, 100.00 FROM users WHERE username = 'alice';
INSERT INTO wallets (user_id, balance)
SELECT id, 20.00 FROM users WHERE username = 'bob';
INSERT INTO wallets (user_id, balance)
SELECT id, 0.00 FROM users WHERE username = 'charlie';
INSERT INTO wallets (user_id, balance)
SELECT id, 0.00 FROM users WHERE username = 'admin';

-- ----------------------
-- RESERVATIONS
-- ----------------------
-- Alice reserves 1 seat -> CONFIRMED
INSERT INTO reservations (trip_id, passenger_id, seats_booked, status_reservation)
SELECT t.id, u.id, 1, 'CONFIRMED'
FROM trips t
         JOIN users u ON u.username = 'alice'
WHERE t.start_location = 'Paris'
    LIMIT 1;

-- Bob attempts to reserve 2 seats -> PENDING (insufficient funds scenario)
INSERT INTO reservations (trip_id, passenger_id, seats_booked, status_reservation)
SELECT t.id, u.id, 2, 'PENDING'
FROM trips t
         JOIN users u ON u.username = 'bob'
WHERE t.start_location = 'Paris'
    LIMIT 1;

-- ----------------------
-- WALLET TRANSACTIONS
-- ----------------------
-- Alice pays her confirmed reservation (DEBIT, SUCCESS)
INSERT INTO wallet_transactions (wallet_id, reservation_id, amount, type_transaction, method_payment, status_transaction)
SELECT w.id, r.id, t.price * r.seats_booked, 'DEBIT', 'BANK_CARD', 'SUCCESS'
FROM wallets w
         JOIN users u ON w.user_id = u.id
         JOIN reservations r ON r.passenger_id = u.id
         JOIN trips t ON r.trip_id = t.id
WHERE u.username = 'alice'
  AND r.status_reservation = 'CONFIRMED'
    LIMIT 1;

-- Bob tries to pay but fails (DEBIT, FAILED)
INSERT INTO wallet_transactions (wallet_id, reservation_id, amount, type_transaction, method_payment, status_transaction)
SELECT w.id, r.id, t.price * r.seats_booked, 'DEBIT', 'BANK_CARD', 'FAILED'
FROM wallets w
         JOIN users u ON w.user_id = u.id
         JOIN reservations r ON r.passenger_id = u.id
         JOIN trips t ON r.trip_id = t.id
WHERE u.username = 'bob'
  AND r.status_reservation = 'PENDING'
    LIMIT 1;

-- ----------------------
-- MESSAGES
-- ----------------------
INSERT INTO messages (sender_id, receiver_id, content)
SELECT a.id, d.id, 'Bonjour, est-ce qu’il reste des places demain ?'
FROM users a, users d
WHERE a.username = 'alice' AND d.username = 'charlie'
    LIMIT 1;

-- ----------------------
-- RATINGS (Alice rates Charlie after trip)
-- ----------------------
INSERT INTO ratings (trip_id, rater_id, rater_user_id, score, comment)
SELECT t.id, r_user.id, u_driver.id, 5, 'Super trajet, merci !'
FROM trips t
         JOIN reservations res ON res.trip_id = t.id
         JOIN users r_user ON r_user.username = 'alice'
         JOIN users u_driver ON u_driver.username = 'charlie'
WHERE r_user.username = 'alice'
  AND t.start_location = 'Paris'
    LIMIT 1;
