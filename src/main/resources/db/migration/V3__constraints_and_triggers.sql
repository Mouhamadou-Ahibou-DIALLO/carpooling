-- =====================================================
-- Migration V3: Extra constraints, triggers and views
-- Carpooling App
-- =====================================================

-- ==================
-- EXTRA CONSTRAINTS
-- ==================

-- Prevent duplicate reservations (1 passenger can’t book same trip twice)
ALTER TABLE reservations
    ADD CONSTRAINT uq_reservation_trip_passenger UNIQUE (trip_id, passenger_id);

-- Optional: ensure available seats in trips are non-negative
ALTER TABLE trips
    ADD CONSTRAINT available_seats_positive CHECK (available_seats >= 0);

-- ==================
-- TRIGGERS & FUNCTIONS
-- ==================

-- 1. Prevent negative wallet balance
CREATE OR REPLACE FUNCTION check_wallet_balance()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.type_transaction = 'DEBIT' AND NEW.status_transaction = 'SUCCESS' THEN
        IF (SELECT balance FROM wallets WHERE id = NEW.wallet_id) < NEW.amount THEN
            RAISE EXCEPTION 'Insufficient balance in wallet %', NEW.wallet_id;
END IF;
END IF;
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_check_wallet_balance
    BEFORE INSERT ON wallet_transactions
    FOR EACH ROW
    EXECUTE FUNCTION check_wallet_balance();


-- 2. Update wallet balance automatically after transaction
CREATE OR REPLACE FUNCTION update_wallet_balance()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.status_transaction = 'SUCCESS' THEN
        IF NEW.type_transaction = 'CREDIT' THEN
UPDATE wallets
SET balance = balance + NEW.amount,
    updated_at = NOW()
WHERE id = NEW.wallet_id;
ELSIF NEW.type_transaction = 'DEBIT' THEN
UPDATE wallets
SET balance = balance - NEW.amount,
    updated_at = NOW()
WHERE id = NEW.wallet_id;
END IF;
END IF;
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_update_wallet_balance
    AFTER INSERT ON wallet_transactions
    FOR EACH ROW
    EXECUTE FUNCTION update_wallet_balance();

-- ==================
-- ANALYTICAL VIEWS
-- ==================

-- View: total spending per user (passenger)
CREATE OR REPLACE VIEW user_spending AS
SELECT u.id AS user_id,
       u.username,
       COALESCE(SUM(wt.amount), 0) AS total_spent
FROM users u
         LEFT JOIN wallets w ON w.user_id = u.id
         LEFT JOIN wallet_transactions wt
                   ON wt.wallet_id = w.id
                       AND wt.type_transaction = 'DEBIT'
                       AND wt.status_transaction = 'SUCCESS'
GROUP BY u.id, u.username;

-- View: top 5 drivers by number of trips
CREATE OR REPLACE VIEW top_drivers AS
SELECT u.id AS driver_id,
       u.username,
       COUNT(t.id) AS trips_count
FROM users u
         JOIN trips t ON t.driver_id = u.id
GROUP BY u.id, u.username
ORDER BY trips_count DESC
    LIMIT 5;
