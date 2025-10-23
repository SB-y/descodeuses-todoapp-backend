# ---------- STAGE 1 : Build Angular + Spring Boot ----------
    FROM node:18 AS build

    RUN apt-get update && apt-get install -y --no-install-recommends \
        openjdk-17-jre-headless maven \
     && rm -rf /var/lib/apt/lists/*
    
    # --- Build du FRONT (Angular) ---
    WORKDIR /frontend
    COPY ../../descodeuses-app/package*.json ./
    RUN npm ci
    COPY ../../descodeuses-app/ .
    RUN npm run build -- --configuration production --project descodeuses-app
    
    # --- Build du BACK (Spring Boot) ---
    WORKDIR /backend
    COPY ../../planit/ .
    RUN mvn -DskipTests package
    
    
    # ---------- STAGE 2 : Exécution ----------
    FROM node:18
    
    RUN apt-get update && apt-get install -y --no-install-recommends openjdk-17-jre-headless \
     && rm -rf /var/lib/apt/lists/*
    
    WORKDIR /app
    COPY --from=build /frontend/dist/descodeuses-app/browser ./frontend
    COPY --from=build /backend/target/*.jar ./backend.jar
    
    EXPOSE 5000 8081
    ENV SPRING_PROFILES_ACTIVE=test
    
    CMD ["sh", "-c", "java -jar backend.jar & npx serve -s frontend -l 5000 --single"]
    