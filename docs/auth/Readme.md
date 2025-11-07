## Auth API (Carpooling App)

Ce module gère l’authentification et l’autorisation des utilisateurs dans l’application de covoiturage.
Il permet de créer un compte, de se connecter et d’obtenir un JWT token pour accéder aux endpoints sécurisés.

Les réponses renvoyées par les endpoints utilisent le DTO UserDTO suivant :

```java
@Builder
public record UserDTO(
    UUID id,
    String email,
    String username,
    String token,
    String refreshToken,
    LocalDateTime tokenExpired,
    RoleUser roleUser,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
```

Remarque : le password n’est jamais renvoyé dans les réponses.

---

## Endpoints disponibles

1. Register (Créer un compte)

   - URL : POST /api/v1/auth/register

    - Description : Permet à un nouvel utilisateur de s’enregistrer.

    - Payload (JSON) :
      ```json
        {
          "email": "user@example.com",
          "username": "john_doe",
          "password": "Password123!",
          "phoneNumber": "+33123456789"
        }
      ```
    
    - Réponse (201) :
      ```json
        {
          "id": "uuid",
          "email": "user@example.com",
          "username": "john_doe",
          "roleUser": "ROLE_PASSENGER",
          "token": "eyJhbGciOiJIUzI1NiIsInR...",
          "refreshToken": "a1b2c3d4e5...",
          "tokenExpired": "2025-10-08T12:34:56",  
          "createdAt": "2025-09-30T09:12:34",
          "updatedAt": "2025-09-30T09:12:34"
        }
      ```

---
      
2. Login (Se connecter)

    - URL : POST /api/v1/auth/login

    - Description : Authentifie un utilisateur et renvoie son JWT + Refresh Token.

    - Payload (JSON) :
      ```json
      {
        "email": "user@example.com",
        "password": "Password123!"
      }
        ```
      
    - Réponse (200 OK) — corps UserDTO (mêmes champs que ci-dessus) :
        ```json
        {
           "id": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
           "email": "user@example.com",
           "username": "john_doe",
           "token": "NEW_ACCESS_TOKEN_HERE",
           "refreshToken": "NEW_REFRESH_TOKEN_HERE",
           "tokenExpired": "2025-10-08T12:34:56",
           "roleUser": "ROLE_PASSENGER",
           "createdAt": "2025-09-30T09:12:34",
           "updatedAt": "2025-10-01T11:00:00"
       }
       ```
      
---

## 3) Refresh token

- URL : POST /api/v1/auth/refresh

- Paramètre : refreshToken (query param) 

- Exemple : POST /api/v1/auth/refresh?refreshToken=d9f3a8b2c4e6f7...

- Comportement :

    - Vérifie que le refreshToken existe, n’est pas expiré et appartient à un utilisateur.

    - Si valide → génère un nouveau JWT (et éventuellement un nouveau refresh token), met à jour l’utilisateur en DB et renvoie UserDTO.

- Réponse (200 OK) :

```json
{
  "id": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
  "email": "user@example.com",
  "username": "john_doe",
  "token": "REFRESHED_ACCESS_TOKEN",
  "refreshToken": "SAME_OR_ROTATED_REFRESH_TOKEN",
  "tokenExpired": "2025-10-15T12:34:56",
  "roleUser": "ROLE_PASSENGER",
  "createdAt": "2025-09-30T09:12:34",
  "updatedAt": "2025-10-01T11:05:00"
}
```

## 4) Me (récupérer les infos de l’utilisateur connecté)

 - URL : GET /api/v1/auth/me

 - Paramètre : Authorization Header

    - Doit contenir le JWT dans le format :
   
    ```makefile
    Authorization: Bearer TON_JWT_ICI
   ```
   
 - Exemple :

 ```bash
 curl -X GET "http://localhost:8080/api/v1/auth/me" \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyNGY5MjQ4OS00OWM2LTQ3ZmUtODE4Mi0zMDExNGM4NjRmOGMiLCJyb2xlX3VzZXIiOiJST0xFX1BBU1NFTkdFUiIsImlhdCI6MTc1OTcxMzA0NiwiZXhwIjoxNzU5NzE2NjQ2fQ.8-ZUamamAfengpvtOWPEipIWjBjYHaG989BPQd6CWE0"
  ```

- Comportement
    - Vérifie que le JWT est présent et valide.

    - Récupère l’utilisateur correspondant en DB via l’ID extrait du JWT.

    - Renvoie les informations de l’utilisateur sous forme de UserDTO.

- Réponse (200 OK) :
```bash
{
  "id": "24f92489-49c6-47fe-8182-30114c864f8c",
  "email": "user@example.com",
  "username": "john_doe",
  "token": "CURRENT_ACCESS_TOKEN",
  "refreshToken": "CURRENT_REFRESH_TOKEN",
  "tokenExpired": "2025-10-13T01:10:46.797",
  "roleUser": "ROLE_PASSENGER",
  "createdAt": "2025-10-06T01:10:46.771161",
  "updatedAt": "2025-10-06T01:10:46.771211"
}
```

## 5) Logout (déconnexion de l’utilisateur connecté)

- URL : POST /api/v1/auth/logout

- Paramètre : Authorization Header

    - Doit contenir le JWT dans le format :

   ```makefile
   Authorization: Bearer TON_JWT_ICI
  ```

- Exemple :

 ```bash
 curl -X GET "http://localhost:8080/api/v1/auth/logout" \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyNGY5MjQ4OS00OWM2LTQ3ZmUtODE4Mi0zMDExNGM4NjRmOGMiLCJyb2xlX3VzZXIiOiJST0xFX1BBU1NFTkdFUiIsImlhdCI6MTc1OTcxMzA0NiwiZXhwIjoxNzU5NzE2NjQ2fQ.8-ZUamamAfengpvtOWPEipIWjBjYHaG989BPQd6CWE0"
  ```

- Comportement :
    - Vérifie que le JWT est valide et correspond à un utilisateur existant.
    - Invalide le token actuel en le supprimant ou en le révoquant côté base de données.
    - Supprime aussi le refresh_token pour forcer une reconnexion totale.
    - Met à jour le champ token et refresh_token à NULL ou à une valeur révoquée.

- Réponse (200 OK)

 ```bash
 {
  "message": "User logged out successfully",
  "timestamp": "2025-10-13T22:47:12"
}
```

---

## Erreurs & format standardisé (exemples)

On renvoie les erreurs dans un format ErrorResponse standard (voir ErrorResponse dans le code). 

Exemples :

Validation / bad request (400):

```json
{
  "type": "about:blank",
  "title": "Validation Failed",
  "status": 400,
  "detail": "Invalid request data",
  "instance": "/api/v1/auth/register",
  "timestamp": "2025-10-01T12:00:00",
  "errorCode": "VALIDATION_ERROR",
  "errors": [
    { "field": "password", "rejectedValue": "abc", "message": "must contain ..." }
  ]
}
```

Refresh token invalide (401):

```json
{
  "type": "about:blank",
  "title": "Unauthorized",
  "status": 401,
  "detail": "Invalid or expired refresh token",
  "instance": "/api/v1/auth/refresh",
  "timestamp": "2025-10-01T12:05:00",
  "errorCode": "AUTH_TOKEN_EXPIRED"
}
```

## Notes & bonnes pratiques

- Ne jamais renvoyer le password dans les réponses.

- Stocker les secrets (JWT secret) dans des variables d’environnement, pas en dur.

- Penser à la révocation du refreshToken (logout, changement de mot de passe).

- Documenter les roleUser possibles : ROLE_PASSENGER, ROLE_DRIVER, ROLE_ADMIN, ROLE_SUPER_ADMIN.