# 🧪 Guide de test Bash — API User

## ⚙️ Préparation

1. Assure-toi que ton serveur Spring Boot tourne :
   ```bash
   docker-compose up
   
2. Rends le script exécutable
    ```bash
    chmod +x test_user.sh
   
---

## ▶️ Exécution des tests

Lance simplement :

```bash
./test_user.sh
```

Chaque requête enverra un curl à l’API avec un message explicatif.

---

## 📋 Scénarios testés

| # | Description                                  | Statut attendu |
| - | -------------------------------------------- | -------------- |
| 1 | Complete user profile (success)              | 200            |
| 2 | Complete user profile (no token)             | 401            |
| 3 | Complete user profile (forbidden admin role) | 403            |
| 4 | Update user (success)                        | 200            |
| 5 | Update user (user already exists)            | 400            |
| 6 | Update user (unauthorized)                   | 401            |
| 7 | Delete user (success)                        | 200            |
| 8 | Delete user (no token)                       | 401            |
| 9 | Delete user (already deleted)                | 404            |

---

## 🧾 Vérification

Regarde les sorties ```HTTP/1.1 200 OK```, ```401 Unauthorized```, etc.
Les logs de ton backend (```docker logs <container_name>```) te montreront les traces :

```sql
User saved successfully ...
User updated successfully ...
Delete user ...
```

---

## 🧩 Astuce

Tu peux rediriger la sortie dans un fichier pour analyse :

```bash
./test_user.sh | tee results_user_tests.log
```



