pipeline {
    agent any

    environment {
        DOCKER_CMD = "docker"
        IMAGE_NAME = "planit-full"
    }

    options {
        timestamps()
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
                sh """
                    echo '🧹 Nettoyage ancien dossier frontend (si présent)...'
                    rm -rf descodeuses-app
                    git clone https://github.com/SB-y/descodeuses-todo-list-app.git descodeuses-app
                """
            }
        }

        stage('3️⃣ Build Docker image (Front + Back)') {
            steps {
                echo "🏗️ Construction de l’image Docker complète..."
                sh """
                    ${DOCKER_CMD} build --pull --progress=plain -t ${IMAGE_NAME} -f cicd/Dockerfile .
                """
            }
        }

        stage('4️⃣ Run container (ports dynamiques)') {
            steps {
                echo "🐳 Démarrage du conteneur ${IMAGE_NAME} avec ports dynamiques..."
                script {
                    // Supprimer les conteneurs précédents du même nom
                    sh """
                        echo '🧹 Suppression des anciens conteneurs ${IMAGE_NAME}...'
                        ${DOCKER_CMD} ps -aq --filter "name=${IMAGE_NAME}" | xargs -r ${DOCKER_CMD} rm -f || true
                    """

                    // Démarrer le conteneur avec ports dynamiques pour front et back
                    sh """
                        echo '🚀 Lancement du conteneur...'
                        ${DOCKER_CMD} run -d --name ${IMAGE_NAME} -p 0:8081 -p 0:5000 ${IMAGE_NAME}
                    """

                    // Récupération des ports réellement alloués
                    def BACK_PORT_ACTUAL = sh(
                        script: "${DOCKER_CMD} port ${IMAGE_NAME} 8081/tcp | awk -F: '{print \$2}'",
                        returnStdout: true
                    ).trim()

                    def FRONT_PORT_ACTUAL = sh(
                        script: "${DOCKER_CMD} port ${IMAGE_NAME} 5000/tcp | awk -F: '{print \$2}'",
                        returnStdout: true
                    ).trim()

                    echo "🌐 Frontend disponible sur : http://localhost:${FRONT_PORT_ACTUAL}"
                    echo "⚙️ Backend disponible sur : http://localhost:${BACK_PORT_ACTUAL}"

                    // Sauvegarde dans le contexte Jenkins (utile pour tests Selenium)
                    env.FRONT_PORT_ACTUAL = FRONT_PORT_ACTUAL
                    env.BACK_PORT_ACTUAL = BACK_PORT_ACTUAL
                }
            }
        }

        stage('5️⃣ Run Selenium tests') {
            steps {
                echo "🧪 Lancement des tests Selenium..."
                dir('cicd/selenium') {
                    sh """
                        echo "🔍 Tests sur : http://localhost:${FRONT_PORT_ACTUAL}"
                        npm ci
                        node test.js --url=http://localhost:${FRONT_PORT_ACTUAL}
                    """
                }
            }
        }

        stage('6️⃣ Nettoyage') {
            steps {
                echo "🧹 Nettoyage des ressources Docker..."
                sh """
                    ${DOCKER_CMD} stop ${IMAGE_NAME} || true
                    ${DOCKER_CMD} rm ${IMAGE_NAME} || true
                    ${DOCKER_CMD} image prune -f
                """
            }
        }
    }

    post {
        success {
            echo "🎉 Pipeline terminée avec succès !"
        }
        failure {
            echo "❌ Le pipeline a échoué. Voici les logs du conteneur :"
            sh "${DOCKER_CMD} logs ${IMAGE_NAME} || true"
        }
    }
}
