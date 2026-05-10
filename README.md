# WeDone – Backend

API REST Spring Boot du projet **WeDone**, une application de gestion de tâches collaborative.

---

## 🚀 Présentation

Le backend fournit les fonctionnalités suivantes :

- gestion des utilisateurs (inscription, connexion)
- authentification sécurisée via JWT
- gestion des tâches (CRUD)
- gestion des projets
- gestion des contacts
- assignation de tâches
- système de commentaires par tâche
- gestion des rôles (USER / ADMIN)

L’API est consommée par un frontend Angular.

---

## 🧱 Architecture

Le backend est structuré en architecture en couches :

- **Controller** : gestion des requêtes HTTP (API REST)
- **Service** : logique métier
- **Repository** : accès aux données via Spring Data JPA
- **DTO** : objets de transfert de données entre frontend et backend

Cette organisation garantit une séparation claire des responsabilités.

---

## 🛠️ Stack technique

- Java 17
- Spring Boot
- Spring Security
- JWT
- Spring Data JPA (Hibernate)
- Maven

---

## 🗄️ Base de données

- **PostgreSQL** : données métier (utilisateurs, tâches, projets, contacts)
- **MongoDB Atlas** : journalisation des actions (logs)

---

## ⚙️ Installation

### Prérequis

- Java 17
- Maven
- PostgreSQL

### 1. Cloner le dépôt

```bash
git clone <url-du-repo-backend>
cd backend
```

### 2. Configurer les variables d’environnement

Créer les variables suivantes :

```text
DB_URL
DB_USERNAME
DB_PASSWORD
JWT_SECRET
MONGO_URI
```

---

### 3. Lancer l’application

```bash
mvn spring-boot:run
```

Backend accessible sur :

```text
http://localhost:8081
```

---

## 🔐 Authentification

Le backend utilise JWT :

- génération d’un token après connexion
- validation du token à chaque requête
- gestion stateless (sans session)

---

## 🛡️ Sécurité

- Spring Security
- protection des endpoints via rôles (USER / ADMIN)
- mots de passe hachés avec BCrypt
- contrôle des accès aux ressources

---

## 🧪 Tests

Tests backend réalisés avec :

- JUnit 5
- Mockito

---

## 🚢 Déploiement

Le backend est déployé sur **Render**.

- containerisation via Docker
- variables d’environnement configurées sur Render
- déploiement automatique à chaque mise à jour

---

## 🔗 Frontend associé

Ce projet fonctionne avec un frontend séparé :

```text
<url-du-repo-frontend>
```

---

## 👤 Auteur

Projet réalisé dans le cadre de la formation **CDA – DesCodeuses**.
