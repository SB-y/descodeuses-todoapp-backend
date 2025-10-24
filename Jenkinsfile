pipeline {
    agent any

    environment {
        NETLIFY_URL = 'https://descodeuses-todolist-app.netlify.app/'
        DOCKER_IMAGE = 'selenium-netlify-tests'
    }

    stages {
        stage('📥 Cloner le dépôt') {
            steps {
                echo "📦 Récupération du code source..."
                checkout scm
            }
        }

        stage('🐳 Construire l’image Docker de test') {
            steps {
                echo "🏗️ Construction de l’image Docker Selenium..."
                sh 'docker build -t ${DOCKER_IMAGE}:${BUILD_ID} -f cicd/Dockerfile .'
            }
        }

        stage('🧪 Lancer les tests Selenium sur Netlify') {
            steps {
                echo "🚀 Lancement des tests Selenium sur ${NETLIFY_URL}"
                sh '''
                    docker run --rm \
                        -e TEST_URL="${NETLIFY_URL}" \
                        ${DOCKER_IMAGE}:${BUILD_ID}
                '''
            }
        }
    }

    post {
        success {
            echo "🎉 Tous les tests Selenium ont réussi sur Netlify !"
        }
        failure {
            echo "❌ Les tests Selenium ont échoué. Consulte les logs au-dessus."
        }
    }
}
