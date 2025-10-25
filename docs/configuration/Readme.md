# Quality Checks et Rapports du Projet

Ce projet utilise plusieurs outils pour assurer la qualité du code et mesurer la couverture des tests. Tous les rapports sont générés dans le dossier `build/reports` après l'exécution des tâches Gradle correspondantes.

## Commande pour exécuter les vérifications

Pour lancer toutes les vérifications de qualité et les tests, utilisez :

```bash
./gradlew check
```

Cette commande exécute:

- **Checkstyle** : analyse le style de code Java selon les règles définies.

- **PMD** : détecte les problèmes de conception, de complexité et de bonnes pratiques.

- **SpotBugs** : identifie les bugs potentiels dans le code.

- **Tests unitaires / intégration** : exécute les tests avec JUnit.

- **JaCoCo** : mesure la couverture des tests.

---

Rapports générés

Après exécution, les rapports sont disponibles dans:

| Outil      | Chemin du rapport                                                            |
| ---------- | ---------------------------------------------------------------------------- |
| Checkstyle | `build/reports/checkstyle/main.html` et `build/reports/checkstyle/test.html` |
| PMD        | `build/reports/pmd/main.html` et `build/reports/pmd/test.html`               |
| SpotBugs   | `build/reports/spotbugs/main.html` et `build/reports/spotbugs/test.html`     |
| JaCoCo     | `build/reports/jacoco/test/html/index.html`                                  |
| Tests      | `build/reports/tests/test/index.html`                                        |

---

## Exemples

Lancer tous les checks
```bash
./gradlew check
```

Lancer seulement PMD
```bash
./gradlew pmdMain pmdTest
```

Lancer seulement SpotBugs
```bash
./gradlew spotbugsMain spotbugsTest
```

Lancer seulement les tests et générer JaCoCo
```bash
./gradlew test jacocoTestReport
```

---

## Notes

- Les rapports HTML sont interactifs et permettent de naviguer facilement entre les classes et les méthodes concernées.

- Les rapports XML sont utilisés par les outils CI/CD comme SonarCloud ou Jenkins.

- Toute anomalie détectée par Checkstyle, PMD ou SpotBugs doit être corrigée pour garantir que le Quality Gate passe.

---

## Générer la documentation du package api.carpooling

Pour générer la documentation de toutes les classes, y compris les tests.
Elle inclut tous les membres, même les attributs et méthodes privées. Utilisez cette commande :

```bash
./gradlew javadocWithTest
```

La documentation sera disponible dans : `build/docs/javadoc-with-tests` sur le fichier **index.html** dont tu peux le superviser sur un navigateur.