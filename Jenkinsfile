pipeline {
    agent any

    environment {
        DOCKER_CMD = "docker"
        IMAGE_NAME = "planit-full"
        FRONT_PORT = "5500"
        BACK_PORT = "8081"
    }

    stages {
        stage('1️⃣ Checkout backend') {
            steps {
                echo "📥 Clonage du dépôt backend..."
                checkout scm
            }
        }

        stage('2️⃣ Checkout frontend') {
            steps {
                echo "📥 Clonage du dépôt frontend..."
                sh "git clone https://github.com/SB-y/descodeuses-todo-list-app.git descodeuses-app"
            }
        }

        stage('3️⃣ Build Docker image (Front + Back)') {
            steps {
                echo "🏗️ Construction de l’image Docker complète..."
                sh "${DOCKER_CMD} build -t ${IMAGE_NAME} -f cicd/Dockerfile ."
            }
        }

        stage('4️⃣ Run container') {
            steps {
                echo "🐳 Démarrage du conteneur..."
                sh """
                    ${DOCKER_CMD} rm -f ${IMAGE_NAME} || true
                    ${DOCKER_CMD} run -d --name ${IMAGE_NAME} \
                        -p ${BACK_PORT}:8081 -p ${FRONT_PORT}:5000 ${IMAGE_NAME}
                """
                echo "🌐 Frontend → http://localhost:${FRONT_PORT}"
                echo "⚙️ Backend → http://localhost:${BACK_PORT}"
            }
        }

        stage('5️⃣ Run Selenium tests') {
            steps {
                echo "🧪 Lancement des tests Selenium..."
                dir('selenium') {
                    sh "npm ci"
                    sh "node test.js"
                }
            }
        }

        stage('6️⃣ Nettoyage') {
            steps {
                echo "🧹 Arrêt du conteneur..."
                sh "${DOCKER_CMD} stop ${IMAGE_NAME} || true"
            }
        }
    }

    post {
        success {
            echo "🎉 Pipeline complète exécutée avec succès !"
        }
        failure {
            echo "❌ Le pipeline a échoué."
            sh "${DOCKER_CMD} logs ${IMAGE_NAME} || true"
        }
    }
}
