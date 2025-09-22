-- =====================================================
-- Migration V1: Initial schema creation
-- Carpooling App
-- =====================================================

-- ==================
-- ENUM types
-- ==================

-- User role type
CREATE TYPE role_user AS ENUM ('USER', 'ADMIN');

-- Reservation status
CREATE TYPE status_reservation AS ENUM ('PENDING', 'CONFIRMED', 'CANCELED');

-- Payment methods
CREATE TYPE method_wallet_payments AS ENUM ('ORANGE_MONEY', 'WAVE', 'BANK_CARD');

-- Wallet transaction status
CREATE TYPE status_wallet_transactions AS ENUM ('PENDING', 'SUCCESS', 'FAILED');

-- Wallet transaction type
CREATE TYPE type_wallet_transactions AS ENUM ('CREDIT', 'DEBIT');

-- =========================================================================

-- ==================
-- USERS
-- ==================
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username VARCHAR(255) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20) UNIQUE NOT NULL,
    photo_user VARCHAR(255),
    address VARCHAR(255),
    role role_user NOT NULL DEFAULT 'USER',
    created_at TIMESTAMP NOT NULL DEFAULT current_timestamp,
    update_at TIMESTAMP NOT NULL DEFAULT current_timestamp,

    CONSTRAINT role_user_check CHECK (role IN ('USER', 'ADMIN'))
);

-- ==================
-- CARS
-- ==================
CREATE TABLE cars (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    brand VARCHAR(255),
    model VARCHAR(255),
    photo_car VARCHAR(255),
    place_number INT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT place_number_car_check CHECK (place_number >= 0),
    CONSTRAINT fk_id_cars_users FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE RESTRICT
);

-- ==================
-- TRIPS
-- ==================
CREATE TABLE trips (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    driver_id UUID NOT NULL,
    start_location VARCHAR(255) NOT NULL,
    end_location VARCHAR(255) NOT NULL,
    departure_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    time_trips TIMESTAMP NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    available_seats INT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_id_users_trip FOREIGN KEY (driver_id) REFERENCES users(id) ON DELETE RESTRICT,
    CONSTRAINT available_seats_trips_check CHECK (available_seats <= 3),
    CONSTRAINT price_check CHECK (price >= 0)
);

-- ==================
-- RESERVATIONS
-- ==================
CREATE TABLE reservations (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    trip_id UUID NOT NULL,
    passenger_id UUID NOT NULL,
    seats_booked INT NOT NULL DEFAULT 1,
    status_reservation status_reservation NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_id_user_reservation FOREIGN KEY (passenger_id) REFERENCES users(id) ON DELETE RESTRICT,
    CONSTRAINT fk_id_trip_reservation FOREIGN KEY (trip_id) REFERENCES trips(id) ON DELETE CASCADE,
    CONSTRAINT seats_booked_reservation_check CHECK (seats_booked BETWEEN 1 AND 3),
    CONSTRAINT status_reservation_check CHECK (status_reservation IN ('PENDING', 'CONFIRMED', 'CANCELED'))
);

-- ==================
-- MESSAGES
-- ==================
CREATE TABLE messages (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    sender_id UUID NOT NULL,
    receiver_id UUID NOT NULL,
    content TEXT,
    sent_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_id_sender_messages FOREIGN KEY (sender_id) REFERENCES users(id) ON DELETE RESTRICT,
    CONSTRAINT fk_id_receiver_messages FOREIGN KEY (receiver_id) REFERENCES users(id) ON DELETE RESTRICT
);

-- ==================
-- RATINGS
-- ==================
CREATE TABLE ratings (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    trip_id UUID NOT NULL,
    rater_id UUID NOT NULL,
    rater_user_id UUID NOT NULL,
    score INT,
    comment TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_id_rater_rating FOREIGN KEY (rater_id) REFERENCES users(id) ON DELETE RESTRICT,
    CONSTRAINT fk_id_rater_user_rating FOREIGN KEY (rater_user_id) REFERENCES users(id) ON DELETE RESTRICT,
    CONSTRAINT fk_id_trip_rating FOREIGN KEY (trip_id) REFERENCES trips(id) ON DELETE CASCADE,
    CONSTRAINT score_check CHECK (score BETWEEN 1 AND 5)
);

-- ==================
-- WALLETS
-- ==================
CREATE TABLE wallets (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    balance DECIMAL(10,2) NOT NULL DEFAULT 0.0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_id_user_wallets FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE RESTRICT,
    CONSTRAINT balance_check CHECK (balance >= 0)
);

-- ==================
-- WALLET TRANSACTIONS
-- ==================
CREATE TABLE wallet_transactions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    wallet_id UUID NOT NULL,
    reservation_id UUID NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    type_transaction type_wallet_transactions NOT NULL,
    method_payment method_wallet_payments NOT NULL,
    status_transaction status_wallet_transactions NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_id_wallet_wt FOREIGN KEY (wallet_id) REFERENCES wallets(id) ON DELETE CASCADE,
    CONSTRAINT fk_id_reservation_wt FOREIGN KEY (reservation_id) REFERENCES reservations(id) ON DELETE CASCADE,
    CONSTRAINT amount_check CHECK (amount >= 0),
    CONSTRAINT type_transaction_check CHECK (type_transaction IN ('CREDIT', 'DEBIT')),
    CONSTRAINT method_payments_check CHECK (method_payment IN ('ORANGE_MONEY', 'WAVE', 'BANK_CARD')),
    CONSTRAINT status_transaction_check CHECK (status_transaction IN ('PENDING', 'SUCCESS', 'FAILED'))
);