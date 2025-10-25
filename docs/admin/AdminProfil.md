# 🧑‍💼 Documentation Administrateur — Application Carpooling

## 📘 Présentation

Lors du premier démarrage de l’application, **un compte administrateur par défaut** est automatiquement créé.  
Ce compte permet d’accéder aux fonctionnalités d’administration du système (gestion des utilisateurs, rôles, etc.).

La création de ce compte est gérée par la classe :

**api.carpooling.configuration.AdminInitializer**


Cette classe s’exécute automatiquement au démarrage de Spring (`@PostConstruct`) **uniquement si aucun administrateur n’existe déjà** dans la base de données.

---

## 👤 Identifiants de l’administrateur par défaut

| Champ | Valeur |
|--------|--------|
| **Nom d’utilisateur** | `admin Carpooling` |
| **Email** | `admin@carpooling.com` |
| **Numéro de téléphone** | `+33611223344` |
| **Mot de passe** | `Admin@123` |
| **Rôle** | `ROLE_ADMIN` |
| **Actif** | `true` |
| **Vérifié** | `true` |

⚠️ **Important :**  
Il est fortement recommandé de **modifier le mot de passe par défaut dès la première connexion**, pour des raisons de sécurité.

---

## 🔐 Génération des jetons (tokens)

Lors de la création du compte administrateur, les éléments suivants sont automatiquement générés et affichés dans les logs :

- **JWT Token** — Utilisé pour l’authentification sur l’API
- **Refresh Token** — Permet de régénérer le JWT
- **Date d’expiration** — Durée de validité du token (par défaut : 7 jours)

Exemple de sortie console :

```text
Admin created:
Email: admin@carpooling.com

Password: Admin@123
Token: <JWT_TOKEN>
RefreshToken: <REFRESH_TOKEN>
TokenExpiresAt: 2025-10-23T17:40:29.329
```

```yaml

---

## 🔁 Comportement lors des redémarrages

- Si le compte administrateur existe déjà dans la base, il **ne sera pas recréé**.  
- Le log suivant apparaîtra alors :
```

**User already exists**

```yaml

L’application continuera ensuite son démarrage normalement.

---

## ⚙️ Modification des informations administrateur

Pour modifier les informations par défaut de l’administrateur :

1. Mettre à jour la classe `AdminInitializer` située dans :
```

**api.carpooling.configuration.AdminInitializer**

```yaml
2. Ou bien, modifier directement les données correspondantes dans la base de données.

---

## 🧭 Étapes recommandées après le premier lancement

- Se connecter avec le compte administrateur par défaut.  
- Créer d’autres comptes utilisateurs avec les rôles appropriés :
- `ROLE_DRIVER`
- `ROLE_PASSENGER`
- Supprimer ou désactiver le compte administrateur par défaut avant la mise en production (optionnel mais recommandé).

---

## 🗂️ Notes supplémentaires

- L’administrateur est le seul à pouvoir attribuer ou modifier les rôles des autres utilisateurs.
- Aucun utilisateur ne peut se définir lui-même comme **ADMIN**.
- Les rôles disponibles dans le système sont :
- `ROLE_ADMIN`
- `ROLE_DRIVER`
- `ROLE_PASSENGER`

---

© Carpooling Project — Documentation interne
```