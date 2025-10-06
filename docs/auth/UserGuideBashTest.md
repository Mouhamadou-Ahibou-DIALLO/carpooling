# Script de test des endpoints Auth

Ce script Bash permet de tester rapidement les endpoints de l'API d'authentification pour ton projet Carpooling.

---

## ⚠️ Prérequis : 

jq installé pour formater le JSON.

Linux/macOS : 

```bash 
brew install jq 
``` 
ou  

```bash
sudo apt install jq 
 ```

---

## Endpoints testés

1. **Register** : `/api/v1/auth/register`
2. **Login** : `/api/v1/auth/login`
3. **Refresh Token** : `/api/v1/auth/refresh_token`
4. **Me** : `/api/v1/auth/me`

---

## Utilisation

1. Assurez-vous que votre API tourne sur `localhost:8080`.
2. Rendez le script exécutable :

```bash
chmod +x test_auth.sh
```

---

## Lancer le script

```bash
./test_auth.sh
```

Le script va afficher les réponses JSON de chaque endpoint et extraire automatiquement le JWT
et le refresh token pour tester **refresh_token** et **me**.

## Dépendances

- curl pour les requêtes HTTP

- jq pour formater le JSON