-- =====================================
-- V7: Update users table with new roles, tokens, and audit fields
-- =====================================

-- 0. Drop Constraint if exist
ALTER TABLE users DROP CONSTRAINT IF EXISTS role_user_check;

-- 1. Backup old role values in a text column
ALTER TABLE users ADD COLUMN role_old_text VARCHAR(50);
UPDATE users SET role_old_text = role::text;

-- 2. Drop the old role column (which depends on the old enum)
ALTER TABLE users DROP COLUMN role;

-- 3. Drop the old type now that nothing depends on it
DROP TYPE IF EXISTS role_user;

-- 4. Create the new ENUM type with updated roles
CREATE TYPE role_user AS ENUM ('ROLE_DRIVER', 'ROLE_PASSENGER', 'ROLE_ADMIN', 'ROLE_SUPER_ADMIN');

-- 5. Add the new role column with the new type
ALTER TABLE users
    ADD COLUMN role role_user DEFAULT 'ROLE_PASSENGER';

-- 6. Map old role values from the backup text column
--    USER  -> ROLE_PASSENGER
--    ADMIN -> ROLE_ADMIN
UPDATE users
SET role = CASE
        WHEN role_old_text = 'USER' THEN 'ROLE_PASSENGER'::role_user
        WHEN role_old_text = 'ADMIN' THEN 'ROLE_ADMIN'::role_user
        ELSE 'ROLE_PASSENGER'::role_user
    END;

-- 7. Drop the backup column
ALTER TABLE users DROP COLUMN role_old_text;

-- 8. Add other new columns if not exist
ALTER TABLE users
    ADD COLUMN IF NOT EXISTS token VARCHAR(255) UNIQUE,
    ADD COLUMN IF NOT EXISTS refresh_token VARCHAR(255) UNIQUE,
    ADD COLUMN IF NOT EXISTS token_expires_at TIMESTAMP,
    ADD COLUMN IF NOT EXISTS last_login TIMESTAMP,
    ADD COLUMN IF NOT EXISTS is_active BOOLEAN NOT NULL DEFAULT true,
    ADD COLUMN IF NOT EXISTS is_verified BOOLEAN NOT NULL DEFAULT false;

-- 9. Set a proper default value for the role column
ALTER TABLE users ALTER COLUMN role SET DEFAULT 'ROLE_PASSENGER';

-- 10. Create index on token for faster lookups
CREATE INDEX IF NOT EXISTS idx_users_token ON users (token);

-- 11. Create table for refresh tokens (recommended for multi-device support)
CREATE TABLE IF NOT EXISTS user_refresh_tokens (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    token VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT current_timestamp,
    expires_at TIMESTAMP,
    revoked BOOLEAN NOT NULL DEFAULT false,

    CONSTRAINT fk_id_refresh_token_users FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE RESTRICT
    );

-- 12. Trigger to automatically update the "update_at" column on update
CREATE OR REPLACE FUNCTION set_update_at()
RETURNS TRIGGER AS $$
BEGIN
  NEW.update_at = current_timestamp;
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM information_schema.triggers
    WHERE event_object_table = 'users' AND trigger_name = 'users_set_update_at'
  ) THEN
CREATE TRIGGER users_set_update_at
    BEFORE UPDATE ON users
    FOR EACH ROW
    EXECUTE FUNCTION set_update_at();
END IF;
END$$;
