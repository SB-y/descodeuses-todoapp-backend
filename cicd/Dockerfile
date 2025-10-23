# Utilise l'image officielle Node.js 18 comme base
# -> On en a besoin pour compiler l'application Angular (frontend)
FROM node:18

# Installe Java 17 et Maven dans le conteneur
# -> nécessaires pour compiler et exécuter l'application Spring Boot (backend)
RUN apt-get update && apt-get install -y --no-install-recommends \
    openjdk-17-jre-headless maven \
 && rm -rf /var/lib/apt/lists/*
# "openjdk-17-jre-headless" = JRE Java sans interface graphique
# "maven" = outil de build Java
# "rm -rf ..." = nettoyage du cache apt pour alléger l'image

# ---------- FRONT (Angular) ----------
# Définit le dossier de travail du conteneur pour le front
WORKDIR /frontend

# Copie uniquement les fichiers de dépendances pour profiter du cache Docker
COPY descodeuses-app/package*.json ./

# Installe les dépendances Node.js (Angular, etc.)
RUN npm ci

# Copie tout le code source Angular dans le conteneur
COPY descodeuses-app/ .

# Compile Angular en mode production
# Résultat : un dossier dist/descodeuses-app/browser prêt à être servi
RUN npx ng build --configuration production

# ---------- BACK (Spring Boot) ----------
# Change le dossier de travail pour le backend
WORKDIR /backend

# Copie le code source du backend (Spring Boot)
COPY planit/ .

# Compile le backend avec Maven en ignorant les tests
# Résultat : un .jar généré dans target/
RUN mvn -DskipTests package

# ---------- CONFIGURATION D’EXÉCUTION ----------

# Ports exposés dans le conteneur :
# 5000 = pour le frontend
# 8081 = pour le backend
EXPOSE 5000 8081

# 👇 Active le profil de test (H2) pour éviter les erreurs PostgreSQL
ENV SPRING_PROFILES_ACTIVE=test

# Commande de démarrage :
# - Lance le backend Spring Boot avec Java
# - Lance en parallèle un serveur statique Node pour Angular
CMD ["sh", "-c", "java -jar /backend/target/*.jar & npx serve -s /frontend/dist/descodeuses-app/browser -l 5000 --single"]
