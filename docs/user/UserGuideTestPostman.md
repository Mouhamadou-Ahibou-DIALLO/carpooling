# 🧭 Guide de test Postman — API User

## 🎯 Objectif
Ce guide permet de tester toutes les routes liées à un **utilisateur** :
- Compléter son profil
- Mettre à jour ses informations
- Supprimer son compte

---

## ⚙️ Pré-requis
- Postman installé
- Serveur backend lancé sur `http://localhost:8080`
- Variable d’environnement :
    - `base_url` = `http://localhost:8080`
    - `jwt_token` = un jeton JWT valide
    - `jwt_token_deleted` = un ancien token d’utilisateur supprimé

---

## 📂 Importation de la collection

1. Ouvre Postman
2. Clique sur **Import** → choisis le fichier  
   `user_api.postman_collection.json`
3. Vérifie que les variables sont bien définies dans l’onglet **Environments**

---

## 🧪 Liste des tests

| # | Endpoint | Méthode | Cas testé | Code attendu |
|--|---------|----------|-----------|---------------|
| 1 | `api/v1/user` | POST | Profil completé avec succès | 200 |
| 2 | `api/v1/user` | POST | Token manquant | 401 |
| 3 | `api/v1/user` | POST | Tentative d’attribuer `ROLE_ADMIN` | 403 |
| 4 | `api/v1/user`      | PUT | Mise à jour réussie | 200 |
| 5 | `api/v1/user`      | PUT | Conflit : user déjà existant | 400 |
| 6 | `api/v1/user`      | PUT | Token manquant | 401 |
| 7 | `api/v1/user`      | DELETE | Suppression réussie | 200 |
| 8 | `api/v1/user`      | DELETE | Token manquant | 401 |
| 9 | `api/v1/user`      | DELETE | Utilisateur déjà supprimé | 404 |

---

## 🧾 Résultats attendus

| Cas | Résultat attendu |
|------|------------------|
| Success | Retour d’un `UserResponse` avec les champs mis à jour |
| 401 Unauthorized | JSON avec message `"Unauthorized"` |
| 403 Forbidden | JSON avec message `"You are not allowed to assign ADMIN role"` |
| 404 Not Found | JSON avec message `"User not found"` |
| 400 Bad Request | JSON avec message `"User already exists"` |

---

## 💡 Conseil
Tu peux exécuter les tests en série via Postman Runner ou Newman :
```bash
newman run user_api.postman_collection.json -e postman_environment.json
