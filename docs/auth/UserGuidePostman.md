# Auth API - Postman Collection

Cette collection Postman (`auth_api.json`) contient **10 tests** pour l'API d'authentification (`/api/v1/auth`).

## Endpoints inclus

- **POST /api/v1/auth/register** : Inscription d'un utilisateur.
- **POST /api/v1/auth/login** : Connexion avec email + mot de passe.
- **POST /api/v1/auth/refresh** : Rafraîchir un access token via le refresh token.
- **GET /api/v1/auth/me** : Récupérer les infos de l'utilisateur connecté.
- **POST /api/v1/auth/logout** : Supprimer les credentials de l'utilisateur (token, refresh_token)

## Variables à configurer dans Postman

Dans l'onglet **Environnement** de Postman, configurez les variables suivantes :

| Variable        | Valeur par défaut            | Description                        |
|-----------------|------------------------------|------------------------------------|
| `baseUrl`       | http://localhost:8080/api/v1 | URL de base de l'API               |
| `email`         | test@example.com             | Email de l'utilisateur             |
| `password`      | Password@123                 | Mot de passe de l'utilisateur      |
| `phoneNumber`   | +33123456789                 | Numéro de téléphone pour register  |
| `token`         | (rempli automatiquement)     | Access token JWT généré au login   |
| `refreshToken`  | (rempli automatiquement)     | Refresh token généré au login      |

## Étapes pour tester

1. Importez le fichier `auth_api.json` dans Postman.
2. Créez un nouvel environnement Postman avec les variables ci-dessus.
3. Lancez les requêtes dans l'ordre :
    - **Register** (corps JSON inclut maintenant `phoneNumber`) → Login → Refresh → Me
4. Vérifiez que les tests Postman passent (ils sont intégrés).

## Exemple de payload pour Register (obligatoire: email, username, password, phoneNumber)

```json
{
  "email": "user@example.com",
  "username": "john_doe",
  "password": "Password123!",
  "phoneNumber": "+33123456789"
}
```

## Notes

- Le champ `token` et `refreshToken` sont automatiquement sauvegardés dans les variables Postman après un login réussi.
- Vous pouvez ensuite appeler les endpoints protégés (`/me`) et (`/logout`) sans réécrire le token manuellement.
