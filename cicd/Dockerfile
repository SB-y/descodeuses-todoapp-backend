# ---------- Étape 1 : Build du front et du back ----------
    FROM node:18 AS build

    # Installe Java et Maven pour le build backend
    RUN apt-get update && apt-get install -y openjdk-17-jre-headless maven
    
    # === FRONT (Angular) ===
    WORKDIR /frontend
    COPY ../../descodeuses-app/package*.json ./
    RUN npm install
    COPY ../../descodeuses-app/ .
    RUN npm run build -- --configuration production --project descodeuses-app
    
    # === BACK (Spring Boot) ===
    WORKDIR /backend
    COPY ../../planit/ .
    RUN mvn -Dspring.profiles.active=test -DskipTests package
    
    
    # ---------- Étape 2 : Exécution ----------
    FROM openjdk:17-jdk-slim
    
    # Installe Node + Serve pour le front
    RUN apt-get update && apt-get install -y nodejs npm && npm install -g serve@14.2.5
    
    WORKDIR /app
    
    # Copie le front compilé et le backend jar
    COPY --from=build /frontend/dist/descodeuses-app/browser ./frontend
    COPY --from=build /backend/target/*.jar ./backend.jar
    
    # Ports exposés
    EXPOSE 8081 5000
    
    # Active le profil test (H2)
    ENV SPRING_PROFILES_ACTIVE=test
    
    # Lance le back et le front
    CMD ["sh", "-c", "java -jar backend.jar & serve -s frontend -l 5000 --single"]
    