# Étape 1 : utiliser une image Node compatible Mac (et Linux)
FROM node:20

USER root
# Installer Chromium + son driver
RUN apt-get update && apt-get install -y chromium chromium-driver curl gnupg \
    && apt-get clean && rm -rf /var/lib/apt/lists/*

# Définir le répertoire de travail
WORKDIR /tests

# Copier ton script de test
COPY cicd/selenium/test.js .

# Installer selenium-webdriver
RUN npm install selenium-webdriver

# Variables d'environnement pour Chrome headless
ENV CHROME_BIN=/usr/bin/chromium
ENV CHROME_PATH=/usr/lib/chromium

# Exposer (optionnel, juste pour cohérence)
EXPOSE 8080 4444

# Lancer les tests Selenium
CMD ["node", "test.js"]
