#!/bin/bash
# ===============================================================
# TEST SCRIPT — Carpooling API : USER ENDPOINTS
# ===============================================================
# Ce script permet de tester toutes les routes utilisateur
# (POST /complete, PUT /update, DELETE /delete) avec plusieurs cas :
# succès, erreurs 401, 403, 400, 404
# ===============================================================

BASE_URL="http://localhost:8080/api/v1"
VALID_TOKEN="PASTE_VALID_JWT_HERE"
INVALID_TOKEN="invalid.token.value"
DELETED_USER_TOKEN="PASTE_OLD_DELETED_USER_JWT"

echo "TEST 1: Complete user profile (success)"
curl -X POST "$BASE_URL/user" \
  -H "Authorization: Bearer $VALID_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "photoUser": "https://example.com/photo.jpg",
    "address": "Dakar, Sénégal",
    "roleUser": "ROLE_DRIVER"
  }'
echo -e "\n---"

echo "TEST 2: Complete user profile (missing token)"
curl -X POST "$BASE_URL/user" \
  -H "Content-Type: application/json" \
  -d '{
    "photoUser": "https://example.com/photo.jpg",
    "address": "Dakar",
    "roleUser": "ROLE_DRIVER"
  }'
echo -e "\n---"

echo "TEST 3: Complete user profile (forbidden — ROLE_ADMIN)"
curl -X POST "$BASE_URL/user" \
  -H "Authorization: Bearer $VALID_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "photoUser": "https://example.com/photo.jpg",
    "address": "Dakar",
    "roleUser": "ROLE_ADMIN"
  }'
echo -e "\n---"

echo "TEST 4: Update user (success)"
curl -X PUT "$BASE_URL/user" \
  -H "Authorization: Bearer $VALID_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "updated_user",
    "email": "updated@example.com",
    "phoneNumber": "770000001",
    "password": "Test1957@"
  }'
echo -e "\n---"

echo "TEST 5: Update user (bad request — already exists)"
curl -X PUT "$BASE_URL/user" \
  -H "Authorization: Bearer $VALID_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "existing_username",
    "email": "existing@example.com",
    "phoneNumber": "770000000",
    "password": "Test1957@"
  }'
echo -e "\n---"

echo "TEST 6: Update user (unauthorized — no token)"
curl -X PUT "$BASE_URL/user" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "test_no_token",
    "email": "no_token@example.com",
    "phoneNumber": "770123456",
    "password": "Test1957@"
  }'
echo -e "\n---"

echo "TEST 7: Delete user (success)"
curl -X DELETE "$BASE_URL/user" \
  -H "Authorization: Bearer $VALID_TOKEN"
echo -e "\n---"

echo "TEST 8: Delete user (unauthorized — no token)"
curl -X DELETE "$BASE_URL/user"
echo -e "\n---"

echo "TEST 9: Delete user (not found — already deleted)"
curl -X DELETE "$BASE_URL/user" \
  -H "Authorization: Bearer $DELETED_USER_TOKEN"
echo -e "\n---"

echo "TESTS TERMINÉS — Vérifiez les statuts HTTP et les messages JSON retournés."
