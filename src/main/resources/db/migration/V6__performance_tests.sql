-- =====================================================
-- Migration V6: Performance test data
-- Carpooling App
-- Objective: insert a large volume of data to test the performance
-- =====================================================

-- ==================
-- Users (1000 fake users)
-- ==================
INSERT INTO users (username, email, password, phone_number, role)
SELECT
    'user_' || g,
    'user_' || g || '@test.com',
    'hashed_password',
    '+330000' || LPAD(g::text, 4, '0'),
    'USER'::role_user
FROM generate_series(1, 1000) g;

-- ==================
-- Cars (each user = 1 car)
-- ==================
INSERT INTO cars (user_id, brand, model, place_number)
SELECT
    u.id,
    'Brand_' || floor(random()*10)::int,
    'Model_' || floor(random()*100)::int,
    (1 + floor(random()*3))::int
FROM users u
ORDER BY random()
    LIMIT 800; -- not all users have a car

-- ==================
-- Trips (500 random trips)
-- ==================
INSERT INTO trips (driver_id, start_location, end_location, departure_time, time_trips, price, available_seats)
SELECT
    u.id,
    'City_' || floor(random()*50)::int,
    'City_' || floor(random()*50)::int,
    NOW() + (g || ' minutes')::interval,
    NOW() + ((g+60) || ' minutes')::interval,
    (5 + random()*30)::numeric(10,2),
    (1 + floor(random()*3))::int
FROM users u
         JOIN generate_series(1, 500) g ON true
ORDER BY random()
    LIMIT 500;

-- ==================
-- Reservations (2000 random)
-- ==================
INSERT INTO reservations (trip_id, passenger_id, seats_booked, status_reservation)
SELECT
    t.id,
    u.id,
    1,
    CASE WHEN random() < 0.7 THEN 'CONFIRMED'::status_reservation ELSE 'PENDING'::status_reservation END
FROM trips t
         JOIN users u ON u.id <> t.driver_id
ORDER BY random()
    LIMIT 2000;

-- ==================
-- Wallets (each user with 100 €)
-- ==================
INSERT INTO wallets (user_id, balance)
SELECT id, 100::numeric(10,2)
FROM users;

-- ==================
-- Wallet transactions (5000 random, only CREDIT to avoid balance errors)
-- ==================
INSERT INTO wallet_transactions (wallet_id, reservation_id, amount, type_transaction, method_payment, status_transaction)
SELECT
    w.id,
    r.id,
    (5 + random()*30)::numeric(10,2) AS amount,
    'CREDIT'::type_wallet_transactions AS type_transaction,
    (ARRAY['ORANGE_MONEY'::method_wallet_payments,'WAVE'::method_wallet_payments,'BANK_CARD'::method_wallet_payments])[floor(random()*3+1)] AS method_payment,
    (ARRAY['PENDING'::status_wallet_transactions,'SUCCESS'::status_wallet_transactions,'FAILED'::status_wallet_transactions])[floor(random()*3+1)] AS status_transaction
FROM wallets w
    JOIN reservations r ON r.passenger_id = w.user_id
ORDER BY random()
    LIMIT 5000;

