#Ce Dockerfile construit une image qui contient uniquement l'application back-end Spring Boot.
#Le .jar est généré dans le conteneur puis lancé à chaque démarrage.
#Le serveur Spring tourne ensuite sur le port 8080 du conteneur.
#Front et back : 2 apps distinctes
#Netlify héberge le front-end statique (HTML, CSS, JS buildés par Angular).
#Render héberge l'API back-end (Java Spring Boot), serveur java qui expose API sur port 8080



# On part d'une image de base : Java 17 (Eclipse Temurin = distribution OpenJDK officielle)
FROM eclipse-temurin:17-jdk

# On définit le dossier de travail à l'intérieur du conteneur
# Tout ce qui suit (COPY, RUN, etc.) se fera dans /app
WORKDIR /app

# On copie tous les fichiers du projet local dans le conteneur
# → Code source Java, pom.xml, dossier mvnw, etc.
COPY . .

# On rend le script Maven wrapper exécutable
RUN chmod +x mvnw

# On lance la compilation du projet et on crée le .jar
# -DskipTests : on saute les tests pour accélérer la build
RUN ./mvnw clean package -DskipTests

# On expose le port 8080
# (le port sur lequel Spring Boot écoutera à l’intérieur du conteneur)
EXPOSE 8080

# On définit la commande de démarrage du conteneur
# → Elle lance l'application Spring Boot
CMD ["java", "-jar", "target/mon-app.jar", "--spring.profiles.active=docker"]
