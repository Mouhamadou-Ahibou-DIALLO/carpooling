-- Automatically generate token and refresh_token for new lines
ALTER TABLE users
    ALTER COLUMN token SET DEFAULT gen_random_uuid()::text,
ALTER COLUMN refresh_token SET DEFAULT gen_random_uuid()::text;

-- Update existing users who do not yet have a token
UPDATE users
SET token = gen_random_uuid()::text
WHERE token IS NULL;

UPDATE users
SET refresh_token = gen_random_uuid()::text
WHERE refresh_token IS NULL;
