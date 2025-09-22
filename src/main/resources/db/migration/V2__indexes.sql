-- =====================================================
-- Migration V2: Indexes creation
-- Carpooling App
-- =====================================================

-- ==================
-- USERS
-- ==================
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_created_at ON users(created_at);

-- ==================
-- CARS
-- ==================
CREATE INDEX idx_cars_user_id ON cars(user_id);

-- ==================
-- TRIPS
-- ==================
CREATE INDEX idx_trips_driver_id ON trips(driver_id);
CREATE INDEX idx_trips_locations ON trips(start_location, end_location);
CREATE INDEX idx_trips_departure_time ON trips(departure_time);

-- ==================
-- RESERVATIONS
-- ==================
CREATE INDEX idx_reservations_trip ON reservations(trip_id);
CREATE INDEX idx_reservations_passenger ON reservations(passenger_id);
CREATE INDEX idx_reservations_status ON reservations(status_reservation);

-- ==================
-- MESSAGES
-- ==================
CREATE INDEX idx_messages_sender ON messages(sender_id);
CREATE INDEX idx_messages_receiver ON messages(receiver_id);

-- ==================
-- RATINGS
-- ==================
CREATE INDEX idx_ratings_trip ON ratings(trip_id);
CREATE INDEX idx_ratings_rated_user ON ratings(rater_user_id);

-- ==================
-- WALLETS
-- ==================
CREATE INDEX idx_wallets_user_id ON wallets(user_id);

-- ==================
-- WALLET TRANSACTIONS
-- ==================
CREATE INDEX idx_wallet_tx_wallet ON wallet_transactions(wallet_id);
CREATE INDEX idx_wallet_tx_reservation ON wallet_transactions(reservation_id);
CREATE INDEX idx_wallet_tx_created_at ON wallet_transactions(created_at);
