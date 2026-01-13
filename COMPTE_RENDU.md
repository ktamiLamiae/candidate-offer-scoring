# Image :

<img src="file.jpg">

# Rapport de Projet : IAMC-O (Identity and Matching Candidate - Opportunity)

Ce document présente une vue d'ensemble technique du projet **IAMC-O**, une application de scoring et de matching entre candidats et offres d'emploi basée sur les compétences.

## 1. Objectif du Projet
L'objectif principal est de fournir un système automatisé capable d'évaluer la pertinence d'un profil de candidat par rapport à une offre d'emploi spécifique en calculant un score de compatibilité.

## 2. Stack Technique
Le projet est construit sur des technologies Java modernes et robustes :
- **Langage** : Java 17
- **Gestion de dépendances** : Maven
- **Persistance** : Hibernate (Mapping XML)
- **Base de données** : PostgreSQL (Exécuté via Docker)
- **Conteneurisation** : Docker & Docker Compose

## 3. Architecture du Système
Le projet suit une architecture multicouche pour une meilleure séparation des préoccupations :

- **Modèle (POJO)** : Représentation des entités métier.
- **DAO (Data Access Object)** : Interface de communication avec la base de données.
- **Service** : Contient la logique métier, notamment l'algorithme de matching.
- **Util** : Classes utilitaires comme `HibernateUtil` pour la gestion de la `SessionFactory`.

## 4. Modèle de Données
Les principales entités du système sont :

- **User** : Classe de base pour l'authentification (Email, Password, Nom, Prénom).
- **Candidat** : Étend `User`, contient un `Profil`.
- **Recruteur** : Étend `User`, publie des `Offres`.
- **Profil** : Contient les détails du candidat et sa liste de `ProfilSkill`.
- **Offre** : Description du poste et liste de `OffreSkill` (compétences requises).
- **Skill** : Référentiel des compétences (Nom, Catégorie).
- **Candidature** : Lie un candidat à une offre avec un résultat de matching.
- **MatchResult** : Stocke le score calculé et les explications détaillées.

## 5. Algorithme de Matching
Le cœur du système réside dans le `MatchingService`. Le calcul du score pour chaque compétence requise dans une offre suit cette logique :

1. **Présence de la compétence** : Si le candidat possède la compétence requise, il obtient **50%** des points alloués à cette compétence.
2. **Niveau de maîtrise** : Si le niveau du candidat est supérieur ou égal au niveau requis, il obtient les **50%** restants.
3. **Score Global** : La somme des points obtenus est divisée par le nombre total de compétences requises pour obtenir un pourcentage de 0 à 100%.

## 6. Interfaces Graphiques (IHM)
L'interface utilisateur a été développée avec **JavaFX 17** pour offrir une expérience fluide et moderne :

- **Layout Sidebar Moderne** : Remplacement de l'entête classique par une barre de navigation latérale (Sidebar) pour une navigation plus intuitive.
- **JavaFX & FXML** : Les interfaces sont définies en FXML, séparant ainsi le design de la logique métier (contrôleurs).
- **Design System Custom** : Utilisation d'un fichier `style.css` pour unifier l'esthétique (couleurs sombres, dégradés, cartes modernes).
- **Écrans Principaux** :
    - **Login** : Authentification et redirection selon le rôle (Candidat ou Recruteur).
    - **Tableau de Bord Recruteur** : Analyse de matching en temps réel et gestion des offres.
    - **Tableau de Bord Candidat** : Consultation des meilleures offres et édition du profil technique.
    - **Édition de Profil** : Gestion dynamique des compétences et des années d'expérience.

## 7. Configuration de la Persistance
La persistance est gérée via Hibernate avec des fichiers de maadmin@iamco.compping XML situés dans `src/main/resources/com/iamco/model/`. 
La configuration principale (`hibernate.cfg.xml`) définit la connexion à la base de données PostgreSQL :
- **URL** : `jdbc:postgresql://localhost:5433/iamcodb`
- **Dialect** : `org.hibernate.dialect.PostgreSQLDialect`

## 8. Déploiement
Le projet utilise Docker pour simplifier la mise en place de l'environnement :
- `docker-compose.yml` : Définit le service `db` (PostgreSQL).
- `init.sql` : Script d'initialisation créant les tables et les contraintes d'intégrité.
