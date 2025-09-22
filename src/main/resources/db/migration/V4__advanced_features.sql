-- =====================================================
-- Migration V4: Advanced features
-- Carpooling App
-- =====================================================

-- ==================
-- STATUS HISTORY
-- ==================

-- History of reservation statuses
CREATE TABLE reservation_status_history (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    reservation_id UUID NOT NULL,
    old_status status_reservation,
    new_status status_reservation NOT NULL,
    changed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    changed_by UUID,

    CONSTRAINT fk_id_user_rsh FOREIGN KEY (changed_by) REFERENCES users(id) ON DELETE RESTRICT,
    CONSTRAINT fk_id_reservations_rsh FOREIGN KEY (reservation_id) REFERENCES reservations(id) ON DELETE CASCADE,
    CONSTRAINT old_status_reservation_check_rsh CHECK (old_status IN ('PENDING', 'CONFIRMED', 'CANCELED')),
    CONSTRAINT new_status_reservation_check_rsh CHECK (new_status IN ('PENDING', 'CONFIRMED', 'CANCELED'))
);

-- History of wallet transaction statuses
CREATE TABLE wallet_tx_status_history (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    wallet_tx_id UUID NOT NULL,
    old_status status_wallet_transactions,
    new_status status_wallet_transactions NOT NULL,
    changed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    changed_by UUID,

    CONSTRAINT fk_id_user_wsh FOREIGN KEY (changed_by) REFERENCES users(id) ON DELETE RESTRICT,
    CONSTRAINT fk_id_wallet_t_wallet_tra FOREIGN KEY (wallet_tx_id) REFERENCES wallet_transactions(id) ON DELETE CASCADE,
    CONSTRAINT old_status_transaction_check_wts CHECK (old_status IN ('PENDING', 'SUCCESS', 'FAILED')),
    CONSTRAINT new_status_transaction_check_wts CHECK (new_status IN ('PENDING', 'SUCCESS', 'FAILED'))
);

-- ==================
-- AUTH: REFRESH TOKENS
-- ==================
CREATE TABLE refresh_tokens (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    token VARCHAR(512) NOT NULL UNIQUE,
    expires_at TIMESTAMP NOT NULL,
    revoked BOOLEAN DEFAULT false,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_id_user_refresh_token FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE RESTRICT,
    CONSTRAINT revoked_check CHECK (revoked IN (false, true))
);

-- ==================
-- AUDIT LOGS
-- ==================
CREATE TABLE audit_logs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID,
    action VARCHAR(255) NOT NULL,
    entity_type VARCHAR(50),
    entity_id UUID,
    details JSONB,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_id_user_audit_logs FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE RESTRICT
);

-- ==================
-- ADVANCED VIEWS
-- ==================

-- Revenue per driver
CREATE OR REPLACE VIEW driver_earnings AS
SELECT u.id AS driver_id,
       u.username,
       COALESCE(SUM(wt.amount),0) AS total_earned
FROM users u
         JOIN trips t ON t.driver_id = u.id
         JOIN reservations r ON r.trip_id = t.id
         JOIN wallet_transactions wt ON wt.reservation_id = r.id
WHERE wt.type_transaction = 'DEBIT'
  AND wt.status_transaction = 'SUCCESS'
GROUP BY u.id, u.username;

-- Number of reservations per day
CREATE OR REPLACE VIEW reservation_stats AS
SELECT DATE(created_at) AS day,
    COUNT(*) AS total_reservations
FROM reservations
GROUP BY DATE(created_at)
ORDER BY day DESC;

-- Load factor per journey
CREATE OR REPLACE VIEW trip_occupancy AS
SELECT t.id AS trip_id,
       t.start_location,
       t.end_location,
       t.available_seats,
       COALESCE(SUM(r.seats_booked),0) AS booked_seats,
       ROUND(
               (COALESCE(SUM(r.seats_booked),0)::DECIMAL / NULLIF(t.available_seats,0)) * 100, 2
       ) AS occupancy_rate
FROM trips t
         LEFT JOIN reservations r ON r.trip_id = t.id AND r.status_reservation = 'CONFIRMED'
GROUP BY t.id, t.start_location, t.end_location, t.available_seats;
