# 📘 Documentation API Utilisateur (`/api/v1/user`)

## 🧩 Description
Cette section de l’API gère la **gestion du profil utilisateur**.  
Elle permet :
- De **compléter** le profil après l’inscription.
- De **mettre à jour** les informations personnelles.
- De **supprimer** son compte.

Tous les endpoints nécessitent un **token JWT valide** transmis via le header `Authorization`.

---

## 🔐 Autorisation
- Accessible uniquement aux utilisateurs authentifiés (`ROLE_PASSENGER`, `ROLE_DRIVER`, `ROLE_ADMIN`).
- Un utilisateur **ne peut pas s’attribuer le rôle `ADMIN`**.
- Les champs uniques (`email`, `username`, `phoneNumber`) sont vérifiés automatiquement.

---

## 🧭 Endpoints disponibles

### 1️⃣ Compléter le profil
**`POST /api/v1/user`**

#### 📝 Description
Permet à un utilisateur de compléter son profil après son inscription.

#### 🔖 Headers
```http
Authorization: Bearer <token>
Content-Type: application/json
```

### 📦 Corps de la requête

```json
{
  "photoUser": "https://example.com/photo.png",
  "address": "Paris, France",
  "roleUser": "ROLE_DRIVER"
}
```

### ✅ Réponse — 200 OK

```json
{
  "id": "c92b54c1-7c3d-4b6d-b4b1-5a9c3b6b8ad2",
  "username": "johndoe",
  "email": "john@example.com",
  "photoUser": "https://example.com/photo.png",
  "address": "Paris, France",
  "roleUser": "ROLE_DRIVER",
  "isVerified": true,
  "isActive": true
}
```

### ⚠️ Codes d’erreur possibles

| Code | Signification | Détail                                |
| ---- | ------------- | ------------------------------------- |
| 401  | Non autorisé  | Token invalide ou expiré              |
| 403  | Interdit      | Tentative d’attribution du rôle ADMIN |
| 404  | Non trouvé    | Utilisateur inexistant                |

---

## 2️⃣ Mettre à jour le profil

`PUT /api/v1/user`

### 📝 Description

Permet à un utilisateur de modifier ses informations (email, username, numéro de téléphone, etc.).

### 📦 Corps de la requête

```json
{
  "email": "newmail@example.com",
  "username": "newusername",
  "phoneNumber": "0612345678",
  "password": "Test@2101"
}
```

### ✅ Réponse — 200 OK

```json
{
  "id": "c92b54c1-7c3d-4b6d-b4b1-5a9c3b6b8ad2",
  "username": "newusername",
  "email": "newmail@example.com",
  "phoneNumber": "0612345678",
  "address": "Paris, France",
  "roleUser": "ROLE_DRIVER",
  "isVerified": true,
  "isActive": true
}
```

### ⚠️ Codes d’erreur possibles

| Code | Signification    | Détail                                           |
| ---- | ---------------- | ------------------------------------------------ |
| 400  | Mauvaise requête | Un utilisateur existe déjà avec ces informations |
| 401  | Non autorisé     | Token invalide ou expiré                         |
| 404  | Non trouvé       | Utilisateur inexistant                           |

---

## 3️⃣ Supprimer le compte

`DELETE /api/v1/user`

### 📝 Description

Supprime définitivement le compte de l’utilisateur connecté.

### ✅ Réponse — 200 OK

```json
{
  "message": "User deleted successfully",
  "timestamp": "2025-10-16T17:40:30.148"
}
```

### ⚠️ Codes d’erreur possibles

| Code | Signification | Détail                   |
| ---- | ------------- | ------------------------ |
| 401  | Non autorisé  | Token invalide ou expiré |
| 404  | Non trouvé    | Utilisateur inexistant   |

---

## 🧪 Tests disponibles

Des fichiers de test sont fournis pour valider les endpoints :

- **Tests Postman** → `UserGuideTestPostman.md`

Contient les scénarios de test avec les réponses attendues.

- **Tests Bash (API en CLI)** → `UserGuideTestBash.md`

Permet de tester rapidement les requêtes via un script shell.

---

## 🧠 Notes techniques

- Le rôle `ROLE_ADMIN` ne peut être attribué que par un administrateur via l’API `/api/v1/admin`.

- Le token JWT contient l’identifiant (`id`) et le rôle (`role_user`) de l’utilisateur.

- Lors d’une mise à jour, seuls les champs non nuls sont copiés sur l’utilisateur existant.

- L’adresse, la photo et le rôle (`ROLE_DRIVER` / `ROLE_PASSENGER`) sont obligatoires lors de la complétion du profil.



