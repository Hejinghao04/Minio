pipeline {
    agent any

    environment {
        PROJECT_DIR = '/opt/minio-project'
    }

    stages {
        stage('Pull Code') {
            steps {
                dir("${PROJECT_DIR}") {
                    git branch: 'main', url: 'https://github.com/Hejinghao04/Minio.git'
                }
            }
        }

        stage('Build Backend') {
            steps {
                dir("${PROJECT_DIR}") {
                    sh './mvnw clean package -DskipTests'
                }
            }
        }

        stage('Build Frontend') {
            steps {
                dir("${PROJECT_DIR}/minio-ui") {
                    sh 'npm install && npm run build'
                }
            }
        }

        stage('Deploy') {
            steps {
                dir("${PROJECT_DIR}") {
                    sh 'docker compose up -d --build backend'
                    sh 'docker compose restart frontend'
                }
            }
        }
    }
}
