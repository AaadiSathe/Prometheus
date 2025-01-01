pipeline {
    agent any

    tools {
        maven 'MAVEN3'
        jdk 'OracleJDK17'
        ansible 'Ansible'
    }

    parameters {
        choice(name: 'ACTION', choices: ['DEPLOY', 'ROLLBACK'], description: 'Choose to deploy new version or rollback')
        choice(name: 'FORCE_DEPLOY_COLOR', choices: ['', 'blue', 'green'], description: 'Force deployment to a specific color (leave empty for auto switch)')
        string(name: 'ROLLBACK_VERSION', defaultValue: '', description: 'Build ID to rollback to (leave empty for new deployment)')
    }

    environment {
        SPRING_DATASOURCE_URL = "jdbc:mysql://3.110.133.63:3306/project"
        DEPLOYMENT_COLOR_FILE = "/var/tmp/current_color.txt"
        DEPLOYMENT_HISTORY_FILE = "/var/tmp/deployment_history.json"
        CURRENT_VERSION_FILE = "/var/tmp/current_version.txt"
        APP_NAME = "spring-app"
        DOCKER_IMAGE_NAME = "master-spring-boot"
    }

    stages {
        stage('INITIALIZATION') {
            steps {
                script {
                    // Validate rollback parameters
                    if (params.ACTION == 'ROLLBACK' && !params.ROLLBACK_VERSION?.trim()) {
                        error("Rollback version must be specified when ACTION is ROLLBACK")
                    }
                    
                    // Create necessary files with proper permissions
                    sh """
                        sudo touch ${DEPLOYMENT_COLOR_FILE} ${DEPLOYMENT_HISTORY_FILE} ${CURRENT_VERSION_FILE} || true
                        sudo chmod 666 ${DEPLOYMENT_COLOR_FILE} ${DEPLOYMENT_HISTORY_FILE} ${CURRENT_VERSION_FILE} || true
                        
                        if [ ! -f ${DEPLOYMENT_HISTORY_FILE} ] || [ ! -s ${DEPLOYMENT_HISTORY_FILE} ]; then
                            echo '[]' | sudo tee ${DEPLOYMENT_HISTORY_FILE}
                        fi
                        if [ ! -f ${CURRENT_VERSION_FILE} ] || [ ! -s ${CURRENT_VERSION_FILE} ]; then
                            echo 'INITIAL' | sudo tee ${CURRENT_VERSION_FILE}
                        fi
                        if [ ! -f ${DEPLOYMENT_COLOR_FILE} ] || [ ! -s ${DEPLOYMENT_COLOR_FILE} ]; then
                            echo 'blue' | sudo tee ${DEPLOYMENT_COLOR_FILE}
                        fi
                    """
                }
            }
        }

        stage('FETCHING CODE') {
            when {
                expression { params.ACTION == 'DEPLOY' }
            }
            steps {
                script {
                    try {
                        withCredentials([string(credentialsId: 'github-token-id', variable: 'GITHUB_TOKEN')]) {
                            git branch: 'master', url: "https://${GITHUB_TOKEN}@github.com/KartikNikhadeBrighLink/PractiveSpringBoot.git"
                        }
                    } catch (Exception e) {
                        error("Failed to fetch code: ${e.message}")
                    }
                }
            }
        }

        stage('BUILDING') {
            when {
                expression { params.ACTION == 'DEPLOY' }
            }
            steps {
                script {
                    try {
                        sh 'mvn clean install -DskipTests'
                        sh 'cp target/SmartContactManger-0.0.1-SNAPSHOT.war .'
                    } catch (Exception e) {
                        error("Build failed: ${e.message}")
                    }
                }
            }
        }

        stage('TESTING') {
            when {
                expression { params.ACTION == 'DEPLOY' }
            }
            steps {
                script {
                    try {
                        withCredentials([usernamePassword(credentialsId: 'mysql-credentials-id', usernameVariable: 'MYSQL_USER', passwordVariable: 'MYSQL_PASSWORD')]) {
                            sh 'mvn test'
                        }
                    } catch (Exception e) {
                        error("Tests failed: ${e.message}")
                    }
                }
            }
        }

        stage('DETERMINE DEPLOYMENT COLOR') {
            steps {
                script {
                    try {
                        if (params.FORCE_DEPLOY_COLOR) {
                            env.DEPLOY_COLOR = params.FORCE_DEPLOY_COLOR
                        } else {
                            def currentColor = sh(script: "cat ${DEPLOYMENT_COLOR_FILE}", returnStdout: true).trim()
                            env.DEPLOY_COLOR = (currentColor == 'blue') ? 'green' : 'blue'
                        }
                        echo "Deploying to ${env.DEPLOY_COLOR} environment"
                    } catch (Exception e) {
                        error("Failed to determine deployment color: ${e.message}")
                    }
                }
            }
        }

        stage('BUILD OR RETRIEVE DOCKER IMAGE') {
            steps {
                script {
                    try {
                        if (params.ACTION == 'DEPLOY') {
                            // Build new image
                            env.IMAGE_TAG = "${DOCKER_IMAGE_NAME}:${BUILD_ID}"
                            sh "docker build -t ${env.IMAGE_TAG} ."
                            
                            // Save current build info for future rollbacks
                            sh """
                                echo "${BUILD_ID}" > /var/tmp/last_successful_build
                            """
                            
                            // Save deployment info
                            def deploymentInfo = """
                                {
                                    "buildId": "${BUILD_ID}",
                                    "timestamp": "${new Date().format("yyyy-MM-dd HH:mm:ss")}",
                                    "imageTag": "${env.IMAGE_TAG}",
                                    "color": "${env.DEPLOY_COLOR}"
                                }
                            """
                            sh """
                                echo '${deploymentInfo}' | sudo tee -a ${DEPLOYMENT_HISTORY_FILE}
                            """
                        } else {
                            // List available images for reference
                            echo "Available versions for rollback:"
                            sh "docker images ${DOCKER_IMAGE_NAME}:* --format '{{.Tag}}'"
                            
                            // Verify rollback image exists
                            env.IMAGE_TAG = "${DOCKER_IMAGE_NAME}:${params.ROLLBACK_VERSION}"
                            def imageExists = sh(script: "docker image inspect ${env.IMAGE_TAG} >/dev/null 3>&1", returnStatus: true) == 0
                            
                            if (!imageExists) {
                                error("""
                                    Rollback image ${env.IMAGE_TAG} not found!
                                    Available versions are:
                                    ${sh(script: "docker images ${DOCKER_IMAGE_NAME}:* --format '{{.Tag}}'", returnStdout: true)}
                                    Please choose one of the available versions.
                                """)
                            }
                        }
                        echo "Using image: ${env.IMAGE_TAG}"
                    } catch (Exception e) {
                        error("Docker image operation failed: ${e.message}")
                    }
                }
            }
        }

        stage('DEPLOY NEW CONTAINER') {
            steps {
                script {
                    try {
                        def containerName = "${APP_NAME}-${env.DEPLOY_COLOR}"
                        def port = (env.DEPLOY_COLOR == 'blue') ? '8081' : '8082'
                        
                        // Remove existing container if it exists
                        sh "docker rm -f ${containerName} || true"
                        
                        withCredentials([usernamePassword(credentialsId: 'mysql-credentials-id', usernameVariable: 'MYSQL_USER', passwordVariable: 'MYSQL_PASSWORD')]) {
                            sh """
                                docker run -d \
                                    --name ${containerName} \
                                    -p ${port}:8081 \
                                    -e SPRING_DATASOURCE_URL=${SPRING_DATASOURCE_URL} \
                                    -e SPRING_DATASOURCE_USERNAME=${MYSQL_USER} \
                                    -e SPRING_DATASOURCE_PASSWORD=${MYSQL_PASSWORD} \
                                    ${env.IMAGE_TAG}
                            """
                        }
                        
                        // Wait for container to be healthy
                        sh "sleep 20"
                    } catch (Exception e) {
                        error("Container deployment failed: ${e.message}")
                    }
                }
            }
        }

        stage('CHECK API HEALTH') {
            steps {
                script {
                    try {
                        def port = (env.DEPLOY_COLOR == 'blue') ? '8081' : '8082'
                        def maxRetries = 3
                        def retryCount = 0
                        def healthy = false
                        
                        while (!healthy && retryCount < maxRetries) {
                            def response = sh(
                                script: "curl -s -o /dev/null -w '%{http_code}' http://3.110.133.63:${port}/health",
                                returnStdout: true
                            ).trim()
                            
                            if (response == '200') {
                                healthy = true
                                echo "API is healthy"
                            } else {
                                retryCount++
                                if (retryCount < maxRetries) {
                                    echo "Attempt ${retryCount}/${maxRetries}: API not healthy (status: ${response}). Retrying in 10 seconds..."
                                    sleep 10
                                }
                            }
                        }
                        
                        if (!healthy) {
                            error("API health check failed after ${maxRetries} attempts")
                        }
                    } catch (Exception e) {
                        error("Health check failed: ${e.message}")
                    }
                }
            }
        }

        stage('SWITCH TRAFFIC TO NEW CONTAINER') {
            steps {
                script {
                    try {
                        // Update Nginx configuration using Ansible
                        sh """
                            ansible-playbook -i inventory.ini switch_nginx.yml \
                                --extra-vars "deployment_color=${env.DEPLOY_COLOR}"
                        """
                        echo "Traffic switched to ${env.DEPLOY_COLOR} container"
                    } catch (Exception e) {
                        error("Traffic switch failed: ${e.message}")
                    }
                }
            }
        }

        stage('CLEANUP OLD RESOURCES') {
            steps {
                script {
                    try {
                        def oldColor = (env.DEPLOY_COLOR == 'blue') ? 'green' : 'blue'
                        def oldContainer = "${APP_NAME}-${oldColor}"
                        
                        // Stop and remove old container
                        sh """
                            docker stop ${oldContainer} || true
                            docker rm ${oldContainer} || true
                        """
                        
                        if (params.ACTION == 'DEPLOY') {
                            // Get current build number as integer
                            def currentBuild = BUILD_ID.toInteger()
                            
                            // Get all available image tags
                            def allTags = sh(
                                script: "docker images ${DOCKER_IMAGE_NAME}:* --format '{{.Tag}}'",
                                returnStdout: true
                            ).trim().split('\n')
                            
                            // Convert tags to integers and sort
                            def tagNumbers = allTags.collect { it.toInteger() }.sort()
                            
                            // Keep only current and previous build images
                            tagNumbers.each { tag ->
                                if (tag < (currentBuild - 2)) {
                                    sh """
                                        docker rmi ${DOCKER_IMAGE_NAME}:${tag} || true
                                        echo "Removed old image: ${DOCKER_IMAGE_NAME}:${tag}"
                                    """
                                }
                            }
                            
                            echo "Cleanup completed. Keeping current (${currentBuild}) and previous (${currentBuild - 2}) builds for rollback safety."
                        }
                    } catch (Exception e) {
                        echo "Warning: Cleanup encountered an issue: ${e.message}"
                        // Don't fail the pipeline for cleanup issues
                    }
                }
            }
        }

        stage('UPDATE DEPLOYMENT STATUS') {
            steps {
                script {
                    try {
                        // Update color file
                        sh """
                            echo '${env.DEPLOY_COLOR}' | sudo tee ${DEPLOYMENT_COLOR_FILE}
                        """
                        
                        // Update version file
                        def status = params.ACTION == 'ROLLBACK' ? 
                            "ROLLBACK to ${params.ROLLBACK_VERSION}" : 
                            "DEPLOYED ${BUILD_ID}"
                        sh """
                            echo '${status}' | sudo tee ${CURRENT_VERSION_FILE}
                        """
                        
                        echo "Deployment status updated: ${status} on ${env.DEPLOY_COLOR}"
                    } catch (Exception e) {
                        error("Failed to update deployment status: ${e.message}")
                    }
                }
            }
        }
    }

    post {
        success {
            script {
                def actionType = params.ACTION == 'ROLLBACK' ? 'rolled back to' : 'deployed'
                def version = params.ACTION == 'ROLLBACK' ? params.ROLLBACK_VERSION : BUILD_ID
                echo "Successfully ${actionType} version ${version} on ${env.DEPLOY_COLOR} stack"
            }
        }
        failure {
            script {
                echo "Pipeline failed. Cleaning up resources..."
                try {
                    // Cleanup on failure
                    if (env.DEPLOY_COLOR) {
                        sh """
                            docker rm -f ${APP_NAME}-${env.DEPLOY_COLOR} || true
                        """
                    }
                } catch (Exception e) {
                    echo "Cleanup after failure encountered an issue: ${e.message}"
                }
            }
        }
        always {
            // Clean up unused Docker resources
            sh """
                docker image prune -f
                docker container prune -f
            """
        }
    }
}
