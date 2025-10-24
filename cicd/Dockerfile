# ---------- Étape 1 : Build du frontend ----------
    FROM node:20 AS build-frontend
    WORKDIR /frontend
    
    COPY ../../descodeuses-app/package*.json ./
    RUN npm ci
    COPY ../../descodeuses-app/ .
    RUN npm run build -- --configuration production --project descodeuses-app
    
    # ---------- Étape 2 : Build du backend ----------
    FROM maven:3.9-eclipse-temurin-17 AS build-backend
    WORKDIR /backend
    
    COPY ../../planit/pom.xml .
    RUN mvn dependency:go-offline -B
    
    COPY ../../planit/ .
    RUN mvn -Dspring.profiles.active=test -DskipTests package
    
    # ---------- Étape 3 : Runtime ----------
    # ⚠️ On prend node + Java pour exécuter à la fois front et back
    FROM node:20
    
    # Installe Java pour Spring Boot
    RUN apt-get update && apt-get install -y openjdk-17-jre-headless \
     && npm install -g serve@14.2.5 \
     && rm -rf /var/lib/apt/lists/*
    
    WORKDIR /app
    
    # Copie du frontend et du backend
    COPY --from=build-frontend /frontend/dist/descodeuses-app/browser ./frontend
    COPY --from=build-backend /backend/target/*.jar ./backend.jar
    
    EXPOSE 5000 8081
    ENV SPRING_PROFILES_ACTIVE=test
    
    # Lance le backend et le frontend
    CMD ["sh", "-c", "java -jar backend.jar & serve -s frontend -l 5000 --single"]
    