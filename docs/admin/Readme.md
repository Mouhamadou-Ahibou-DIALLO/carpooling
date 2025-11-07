# Documentation API Administrateur (`/api/v1/admin`)

## Description
Cette section de l’API est réservée aux **administrateurs** et permet :
- De **consulter** la liste des utilisateurs.
- De **modifier** le rôle ou l’état d’un utilisateur (activation/désactivation).
- De **supprimer** un utilisateur.
- De **récupérer** un utilisateur par email.

Toutes les routes exigent que le token JWT contienne le rôle **`ROLE_ADMIN`**.

---

## Autorisation
- Accessible **uniquement** aux utilisateurs ayant `ROLE_ADMIN`.
- Le header `Authorization` doit être présent pour chaque requête :
  ```http
  Authorization: Bearer <token_admin>
  ```

---

## Endpoints disponibles

### 1. Récupérer la liste de tous les utilisateurs

`GET /api/v1/admin`

### Description

Retourne une liste paginée de tous les utilisateurs enregistrés.

### Paramètres de pagination

| Nom    | Type | Défaut | Description                |
| ------ | ---- | ------ | -------------------------- |
| `page` | int  | 0      | Numéro de la page          |
| `size` | int  | 20     | Nombre d’éléments par page |

### Réponse — 200 OK

```json
{
  "content": [
    {
      "id": "c92b54c1-7c3d-4b6d-b4b1-5a9c3b6b8ad2",
      "username": "johndoe",
      "email": "john@example.com",
      "roleUser": "ROLE_PASSENGER",
      "isActive": true
    },
    {
      "id": "c31b54c1-9c3d-4b6d-b4b1-9a7c3b6b8f02",
      "username": "jane",
      "email": "jane@example.com",
      "roleUser": "ROLE_DRIVER",
      "isActive": false
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20
  },
  "totalElements": 2
}
```

### Codes d’erreur possibles

| Code | Signification | Détail                        |
| ---- | ------------- | ----------------------------- |
| 401  | Non autorisé  | Token manquant ou invalide    |
| 403  | Interdit      | L’utilisateur n’est pas admin |

---

### 2. Récupérer un utilisateur par email

`GET /api/v1/admin/{email}`

### Description

Retourne un utilisateur correspondant à l’adresse email fournie.

### Réponse — 200 OK

```json
{
  "id": "c92b54c1-7c3d-4b6d-b4b1-5a9c3b6b8ad2",
  "username": "johndoe",
  "email": "john@example.com",
  "roleUser": "ROLE_PASSENGER",
  "isActive": true
}
```

### Codes d’erreur possibles

| Code | Signification | Détail                                  |
| ---- | ------------- | --------------------------------------- |
| 401  | Non autorisé  | Token invalide                          |
| 403  | Interdit      | L’utilisateur n’est pas admin           |
| 404  | Non trouvé    | Aucun utilisateur trouvé avec cet email |

---

### 3. Modifier le rôle ou l’état d’un utilisateur

`PUT /api/v1/admin/{email}`

### Description

Permet à un administrateur de modifier le rôle ou l’état (isActive) d’un utilisateur.

### Corps de la requête

```json
{
  "roleUser": "ROLE_DRIVER",
  "isActive": true
}
```

### Réponse — 200 OK

```json
{
  "id": "c92b54c1-7c3d-4b6d-b4b1-5a9c3b6b8ad2",
  "username": "johndoe",
  "email": "john@example.com",
  "roleUser": "ROLE_DRIVER",
  "isActive": true
}
```

### Codes d’erreur possibles

| Code | Signification | Détail                        |
| ---- | ------------- | ----------------------------- |
| 401  | Non autorisé  | Token invalide                |
| 403  | Interdit      | L’utilisateur n’est pas admin |
| 404  | Non trouvé    | L’utilisateur n’existe pas    |


---

### 4. Supprimer un utilisateur

`DELETE /api/v1/admin/{email}`

### Description

Supprime un utilisateur du système.

### Réponse — 200 OK

```json
{
  "message": "User deleted successfully",
  "timestamp": "2025-10-16T18:45:22.148"
}
```

### Codes d’erreur possibles

| Code | Signification | Détail                        |
| ---- | ------------- | ----------------------------- |
| 401  | Non autorisé  | Token invalide                |
| 403  | Interdit      | L’utilisateur n’est pas admin |
| 404  | Non trouvé    | L’utilisateur n’existe pas    |

---

## Tests disponibles

Des fichiers de test sont fournis pour valider les endpoints administrateur :

- **Tests Postman** → `AdminGuideTestPostman.md`

Inclut tous les scénarios de réussite et d’échec (200, 401, 403, 404).

- **Tests Bash (CLI)** → `AdminGuideTestBash.md`

Permet d’exécuter les tests rapidement via un script shell.

---

## Notes techniques

- Seuls les utilisateurs avec le rôle `ROLE_ADMIN` peuvent accéder à ces endpoints.

- Les actions sont sécurisées par l’annotation `@PreAuthorize("hasRole('ADMIN')")`.

- L’administrateur ne peut pas modifier son propre rôle via ces endpoints.

- La pagination par défaut renvoie **20 utilisateurs par page**.

---

## Existence d'un Admin

Lors du lancement du projet un profil Admin est déja crée et vous pouvez consulter `AdminProfil.md` pour mieux comprendre.


