# 🧪 Guide de test Bash — API Admin

## ⚙️ Préparation

1. Assure-toi que ton serveur Spring Boot tourne :
   ```bash
   docker-compose up

2. Rends le script exécutable
    ```bash
    chmod +x test_admin.sh

---

## ▶️ Exécution des tests

Lance simplement :

```bash
./test_admin.sh
```

Chaque requête enverra un curl à l’API avec un message explicatif.

---

## 📋 Scénarios testés

| #  | Description                                  | Statut attendu |
| -- | -------------------------------------------- | -------------- |
| 1  | GET all users (success)                      | 200            |
| 2  | GET all users (no token)                     | 401            |
| 3  | GET all users (token non-admin)              | 403            |
| 4  | GET user by email (success)                  | 200            |
| 5  | GET user by email (user not found)           | 404            |
| 6  | GET user by email (token non-admin)          | 403            |
| 7  | PUT update user role/state (success)         | 200            |
| 8  | PUT update user role/state (invalid body)    | 400            |
| 9  | PUT update user role/state (user not found)  | 404            |
| 10 | PUT update user role/state (token non-admin) | 403            |
| 11 | DELETE user (success)                        | 200            |
| 12 | DELETE user (user not found)                 | 404            |
| 13 | DELETE user (no token)                       | 401            |
| 14 | DELETE user (token non-admin)                | 403            |

---

## 🧾 Vérification

Regarde les sorties ```HTTP/1.1 200 OK```, ```401 Unauthorized```, etc.
Les logs de ton backend (```docker logs <container_name>```) te montreront les traces :

```sql
[ADMIN SERVICE] Update user her role or active ...
[ADMIN SERVICE] Delete user with email ...
[ADMIN SERVICE] Get user with email ...
```

---

## 🧩 Astuce

Tu peux rediriger la sortie dans un fichier pour analyse :

```bash
./test_admin.sh | tee results_admin_tests.log
```



