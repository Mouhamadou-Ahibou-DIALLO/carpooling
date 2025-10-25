# Carpooling Frontend

Ce projet est le frontend React de l’application Carpooling.

**Prérequis**

- Docker et Docker Compose installés

- Node.js et npm (si tu veux lancer en local sans Docker)

**Installation et démarrage**

1. Lancer avec Docker (Se placer à la racine du projet)

```bash
# Se déplacer dans le dossier frontend/carpooling-frontend
cd frontend/carpooling-frontend

# lancer l'application
docker-compose up --build
```
- Le frontend sera accessible sur : http://localhost:3000

- Pour arrêter les containers : docker-compose down

2. Lancer en local (sans Docker, dans la racine du projet)

```bash
# Se déplacer dans le dossier frontend/carpooling-frontend
cd frontend/carpooling-frontend

# lancer l'application
npm install
npm start
```

- Le frontend sera accessible sur : http://localhost:3000

- Les modifications sont hot-reloads automatiquement.

---