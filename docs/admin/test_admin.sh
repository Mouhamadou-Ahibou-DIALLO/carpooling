#!/bin/bash
# 🧪 Script de test Admin API - test_admin.sh

BASE_URL="http://localhost:8080/api/v1"
ADMIN_TOKEN="{{adminToken}}"  # Remplacer par le token JWT Admin valide
EMAIL_USER="user@example.com"

echo "========================================"
echo "1️⃣ Test GET All Users - Success"
curl -s -X GET "$BASE_URL/admin" -H "Authorization: Bearer $ADMIN_TOKEN" | jq
echo -e "\n========================================"

echo "2️⃣ Test GET User By Email - Success"
curl -s -X GET "$BASE_URL/admin/$EMAIL_USER" -H "Authorization: Bearer $ADMIN_TOKEN" | jq
echo -e "\n========================================"

echo "3️⃣ Test GET User By Email - Not Found"
curl -s -X GET "$BASE_URL/admin/notfound@example.com" -H "Authorization: Bearer $ADMIN_TOKEN" | jq
echo -e "\n========================================"

echo "4️⃣ Test PUT Update User Role/Active - Success"
curl -s -X PUT "$BASE_URL/admin/$EMAIL_USER" \
-H "Authorization: Bearer $ADMIN_TOKEN" \
-H "Content-Type: application/json" \
-d '{"roleUser":"ROLE_DRIVER","isActive":true}' | jq
echo -e "\n========================================"

echo "5️⃣ Test PUT Update User Role/Active - User Not Found"
curl -s -X PUT "$BASE_URL/admin/notfound@example.com" \
-H "Authorization: Bearer $ADMIN_TOKEN" \
-H "Content-Type: application/json" \
-d '{"roleUser":"ROLE_DRIVER","isActive":true}' | jq
echo -e "\n========================================"

echo "6️⃣ Test DELETE User - Success"
curl -s -X DELETE "$BASE_URL/admin/$EMAIL_USER" -H "Authorization: Bearer $ADMIN_TOKEN" | jq
echo -e "\n========================================"

echo "7️⃣ Test DELETE User - Not Found"
curl -s -X DELETE "$BASE_URL/admin/notfound@example.com" -H "Authorization: Bearer $ADMIN_TOKEN" | jq
echo -e "\n========================================"

echo "8️⃣ Test GET All Users - Unauthorized (No Token)"
curl -s -X GET "$BASE_URL/admin" | jq
echo -e "\n========================================"

echo "9️⃣ Test PUT Update User Role/Active - Unauthorized (No Token)"
curl -s -X PUT "$BASE_URL/admin/$EMAIL_USER" \
-H "Content-Type: application/json" \
-d '{"roleUser":"ROLE_DRIVER","isActive":true}' | jq
echo -e "\n========================================"

echo "🔚 Tous les tests Admin sont terminés !"
