-- =====================================
-- V9: Simplify roles (remove ROLE_SUPER_ADMIN)
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
CREATE TYPE role_user AS ENUM ('ROLE_DRIVER', 'ROLE_PASSENGER', 'ROLE_ADMIN');

-- 5. Add the new role column with the new type
ALTER TABLE users
    ADD COLUMN role role_user DEFAULT 'ROLE_PASSENGER';

-- 6. Map old role values from the backup text column
--    ROLE_SUPER_ADMIN -> ROLE_ADMIN
UPDATE users
SET role = CASE
        WHEN role_old_text = 'ROLE_SUPER_ADMIN' THEN 'ROLE_ADMIN'::role_user
        WHEN role_old_text = 'ROLE_DRIVER' THEN 'ROLE_DRIVER'::role_user
        WHEN role_old_text = 'ROLE_PASSENGER' THEN 'ROLE_PASSENGER'::role_user
        WHEN role_old_text = 'ROLE_ADMIN' THEN 'ROLE_ADMIN'::role_user
        ELSE 'ROLE_PASSENGER'::role_user
    END;


-- 7. Drop the backup column
ALTER TABLE users DROP COLUMN role_old_text;


-- 8. Reapply default
ALTER TABLE users ALTER COLUMN role SET DEFAULT 'ROLE_PASSENGER';

-- 9. Add documentation
COMMENT ON TYPE role_user IS 'Defines user roles: DRIVER, PASSENGER, ADMIN';
COMMENT ON COLUMN users.role IS 'Role assigned to the user';