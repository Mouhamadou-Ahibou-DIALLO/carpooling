#!/bin/bash

# =====================================
# Script pour tester les endpoints Auth
# =====================================

BASE_URL="http://localhost:8080/api/v1/auth"

# ----------------------
# 1) Register
# ----------------------
echo "----- REGISTER -----"
curl -s -X POST "$BASE_URL/register" \
-H "Content-Type: application/json" \
-d '{
  "email": "user@example.com",
  "username": "john_doe",
  "password": "Password@123",
  "phoneNumber": "0123456789"
}' | jq

echo -e "\n"

# ----------------------
# 2) Login
# ----------------------
echo "----- LOGIN -----"
LOGIN_RESPONSE=$(curl -s -X POST "$BASE_URL/login" \
-H "Content-Type: application/json" \
-d '{
  "email": "user@example.com",
  "password": "Password@123"
}')

echo "$LOGIN_RESPONSE" | jq

# Récupération du JWT et refreshToken
JWT=$(echo "$LOGIN_RESPONSE" | jq -r '.token')
REFRESH_TOKEN=$(echo "$LOGIN_RESPONSE" | jq -r '.refreshToken')

echo -e "\nJWT Token: $JWT"
echo "Refresh Token: $REFRESH_TOKEN"
echo -e "\n"

# ----------------------
# 3) Refresh Token
# ----------------------
echo "----- REFRESH TOKEN -----"
curl -s -X POST "$BASE_URL/refresh_token?refreshToken=$REFRESH_TOKEN" \
-H "Content-Type: application/json" | jq

echo -e "\n"

# ----------------------
# 4) Me Endpoint
# ----------------------
echo "----- ME -----"
curl -s -X GET "$BASE_URL/me" \
-H "Authorization: Bearer $JWT" \
-H "Content-Type: application/json" | jq

echo -e "\n"
