pipeline {
    agent any

    environment {
        DOCKER_CMD = "docker"
        IMAGE_NAME = "planit-test"
        FRONT_PORT = "5500"
        BACK_PORT = "8090"
    }

    stages {
        stage('1️⃣ Checkout code') {
            steps {
                echo "📥 Récupération du dépôt..."
                checkout scm
            }
        }

        stage('2️⃣ Construire l’image Docker') {
            steps {
                echo "🏗️ Construction de l’image Docker..."
                dir('planit') {
                    sh "${DOCKER_CMD} build -t ${IMAGE_NAME} -f cicd/Dockerfile ."
                }
            }
        }

        stage('3️⃣ Lancer le conteneur') {
            steps {
                echo "🐳 Démarrage du conteneur Docker..."
                sh """
                    ${DOCKER_CMD} rm -f planit-test || true
                    ${DOCKER_CMD} run -d --name planit-test -p ${BACK_PORT}:8081 -p ${FRONT_PORT}:5000 ${IMAGE_NAME}
                """
            }
        }

        stage('4️⃣ Lancer les tests Selenium') {
            steps {
                echo "🧪 Exécution des tests Selenium..."
                dir('back_planit/selenium') {
                    sh "npm ci"
                    sh "node test.js"
                }
            }
        }

        stage('5️⃣ Nettoyage') {
            steps {
                echo "🧹 Arrêt du conteneur..."
                sh "${DOCKER_CMD} stop planit-test || true"
            }
        }
    }

    post {
        success {
            echo "🎉 Pipeline exécuté avec succès !"
        }
        failure {
            echo "❌ Le pipeline a échoué."
            sh "${DOCKER_CMD} logs planit-test || true"
        }
    }
}