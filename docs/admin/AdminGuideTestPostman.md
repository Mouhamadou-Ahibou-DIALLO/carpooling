# 🧑‍💼 Admin API - Postman Collection

Cette collection Postman (`admin_api.json`) contient **4 endpoints principaux** pour l’administration des utilisateurs via `/api/v1/admin`.

---

## Endpoints inclus

- **GET /api/v1/admin** → Liste paginée de tous les utilisateurs.
- **GET /api/v1/admin/{email}** → Récupère un utilisateur spécifique.
- **PUT /api/v1/admin/{email}** → Modifie le rôle ou l’état actif d’un utilisateur.
- **DELETE /api/v1/admin/{email}** → Supprime un utilisateur du système.

---

## Variables à configurer dans Postman

| Variable         | Valeur par défaut             | Description                                  |
|------------------|-------------------------------|----------------------------------------------|
| `baseUrl`        | http://localhost:8080/api/v1  | URL de base de l'API                         |
| `adminToken`     | (à renseigner)                | JWT valide de l’administrateur               |
| `emailUser`      | john@example.com              | Email d’un utilisateur à modifier/supprimer  |

---

## Étapes pour tester

1. Importez le fichier `admin_api.json` dans Postman.
2. Configurez l’environnement avec les variables ci-dessus.
3. Exécutez les tests **dans l’ordre suivant** :
    - `GET All Users`
    - `GET User by Email`
    - `PUT Update User`
    - `DELETE User`
4. Vérifiez les statuts et les réponses JSON selon les cas.

---

## 🧪 Liste des tests

| # | Endpoint | Méthode | Cas testé | Code attendu |
|--|-----------|----------|-----------|---------------|
| 1 | `/admin` | GET | Liste des utilisateurs réussie | 200 |
| 2 | `/admin` | GET | Token manquant | 401 |
| 3 | `/admin` | GET | Token non-admin | 403 |
| 4 | `/admin/{email}` | GET | Utilisateur trouvé | 200 |
| 5 | `/admin/{email}` | GET | Utilisateur inexistant | 404 |
| 6 | `/admin/{email}` | GET | Token non-admin | 403 |
| 7 | `/admin/{email}` | PUT | Mise à jour réussie | 200 |
| 8 | `/admin/{email}` | PUT | Corps JSON invalide | 400 |
| 9 | `/admin/{email}` | PUT | Utilisateur inexistant | 404 |
| 10 | `/admin/{email}` | PUT | Token non-admin | 403 |
| 11 | `/admin/{email}` | DELETE | Suppression réussie | 200 |
| 12 | `/admin/{email}` | DELETE | Utilisateur inexistant | 404 |
| 13 | `/admin/{email}` | DELETE | Token manquant | 401 |
| 14 | `/admin/{email}` | DELETE | Token non-admin | 403 |

---

## 🧾 Résultats attendus

| Cas | Résultat attendu |
|------|------------------|
| Success | Retourne `UserResponse` avec `roleUser` et `isActive` mis à jour, ou message `"User deleted successfully"` |
| 401 Unauthorized | JSON : `{ "message": "Unauthorized" }` |
| 403 Forbidden | JSON : `{ "message": "Access denied: only admins can perform this action" }` |
| 404 Not Found | JSON : `{ "message": "User not found" }` |
| 400 Bad Request | JSON : `{ "message": "Invalid request body" }` |

---

## Notes

Tous les endpoints nécessitent un **token admin valide**.

Les rôles disponibles : ROLE_DRIVER`, `ROLE_PASSENGER`, `ROLE_ADMIN.

Le champ isActive désactive ou réactive un compte utilisateur.

---

---

## 💡 Conseil
Tu peux exécuter les tests en série via Postman Runner ou Newman :
```bash
newman run admin_api.postman_collection.json -e postman_environment.json