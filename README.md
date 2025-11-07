# Carpooling

## Description
Carpooling est une application web et mobile qui facilite le covoiturage entre voisins, collègues ou étudiants.  
L’objectif est de réduire le trafic, favoriser la mobilité durable et créer un réseau social de proximité autour du partage de trajets.

## Fonctionnalités principales
- Gestion de profil (nom, photo, préférences, voiture)
- Publier et rechercher des trajets (départ, arrivée, horaires, places disponibles)
- Messagerie intégrée entre conducteurs et passagers
- Notifications en temps réel
- Système d’évaluations et de commentaires
- Simulation de paiement (Orange Money, Wave, Carte bancaire)

---

## Objectifs
- Réduire le nombre de véhicules en circulation
- Promouvoir la mobilité durable
- Offrir des économies financières aux utilisateurs
- Créer un réseau social local fiable et sécurisé

---

## Stack technique

**Backend**
- Java 21 (Temurin)
- Spring Boot 3+
- PostgresSQL 17.4
- Redis 8 (RC)
- Flyway (migrations SQL)
- Docker & Docker Compose
- TestContainers (tests d’intégration)
- Swagger (OpenAPI)
- Spring Actuator (monitoring & health checks)

**Frontend**
- React.js
- npm (Node Package Manager)

---

## Démarrage rapide

### 1. Prérequis
- Java 21
- Docker & Docker Compose
- Gradle (ou `./gradlew`)
- IntelliJ IDEA ou VS Code

### 2. Configuration de l’environnement
Créer un fichier `.env` à la racine du projet :
```env
POSTGRES_HOST=...
POSTGRES_PORT=5432
POSTGRES_USER=...
POSTGRES_PASSWORD=...
POSTGRES_DB=...

REDIS_HOST=redis
REDIS_PORT=6379
```

### 3. Build & Start des services

```bash
docker compose up --build
```


### 4. Accès aux services
API REST
- URL principale : http://localhost:8080
- Base path API : /api/v1

Documentation API (Swagger)
- UI : http://localhost:8080/swagger-ui/index.html
- OpenAPI JSON : http://localhost:8080/v3/api-docs

Monitoring & Health (Actuator)
- Health Check : http://localhost:8080/actuator/health
- Info : http://localhost:8080/actuator/info
- Métriques : http://localhost:8080/actuator/metrics
- Tous les endpoints : http://localhost:8080/actuator


### 5. Frontend

Pour accéder à la partie frontend du projet accéder au dossier `frontend/carpooling-frontend`.

---

## CI/CD & Qualité du code

Le projet est équipé d’un pipeline CI/CD complète avec :

- CircleCI : intégration continue (build, tests, analyse)
- SonarCloud : qualité du code et détection des bugs/vulnérabilités
- Codecov : couverture de tests avec JaCoCo
- Checkstyle : respect des conventions Java
- PMD : détection de mauvaises pratiques de code
- SpotBugs : analyse statique pour identifier des bugs potentiels
- JaCoCo : rapport de couverture de tests

---

## Badges

CircleCi : 
[![CircleCI](https://dl.circleci.com/status-badge/img/gh/Mouhamadou-Ahibou-DIALLO/carpooling/tree/master.svg?style=svg)](https://dl.circleci.com/status-badge/redirect/gh/Mouhamadou-Ahibou-DIALLO/carpooling/tree/master)

CodeCov :
[![codecov](https://codecov.io/gh/Mouhamadou-Ahibou-DIALLO/carpooling/graph/badge.svg?token=OY5CKMR8CB)](https://codecov.io/gh/Mouhamadou-Ahibou-DIALLO/carpooling)

SonarCloud :
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=checkout_organisation-diallo01&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=checkout_organisation-diallo01)
---

## Explication de chaque fonctionnalité

Se réferer au repertoire docs sur la racine du projet


