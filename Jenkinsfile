pipeline {
    agent any

    environment {
        DOCKER_CMD = "docker"
        IMAGE_NAME = "planit-test"         // nom de ton image Docker
        FRONT_PORT = "5500"                // port externe pour le front
        BACK_PORT = "8090"                 // port externe pour le back
    }

    stages {

        stage('1️⃣ Cloner le code CI/CD') {
            steps {
                echo "📥 Clonage du dépôt Git CI/CD..."
                git branch: 'main', url: 'https://github.com/SB-y/todo-cicd.git'
            }
        }

        stage('2️⃣ Construire l’image Docker') {
            steps {
                echo "🏗️ Construction de l’image Docker..."
                sh "${DOCKER_CMD} build -t ${IMAGE_NAME} -f cicd/Dockerfile ."
            }
        }

        stage('3️⃣ Lancer le conteneur') {
            steps {
                echo "🐳 Démarrage du conteneur Docker..."
                sh """
                    ${DOCKER_CMD} rm -f planit-test || true
                    ${DOCKER_CMD} run -d --name planit-test -p ${BACK_PORT}:8081 -p ${FRONT_PORT}:5000 ${IMAGE_NAME}
                """
                echo "🌐 Frontend → http://localhost:${FRONT_PORT}"
                echo "⚙️ Backend → http://localhost:${BACK_PORT}"
            }
        }

        stage('4️⃣ Lancer les tests Selenium') {
            steps {
                echo "🧪 Exécution des tests Selenium..."
                dir('selenium') {
                    sh "npm ci"
                    sh "node test.js"
                }
            }
        }

        stage('5️⃣ (Optionnel) Arrêter le conteneur') {
            steps {
                echo "🧹 Nettoyage du conteneur..."
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