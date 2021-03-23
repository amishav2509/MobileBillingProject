pipeline {
    agent any
    stages {
		stage ('clean workspace') {
          steps {
            deleteDir()
          }
        }
		stage('Git Clone') {
 
			steps {
				script{
					sh "mkdir devops"
					dir('devops'){
						echo 'Cloning from Github'
						git branch: 'main', credentialsId: 'amishav2509', url: 'https://github.com/amishav2509/MobileBillingProject.git'
						sh "chmod -R 777 devops"
					}
				}
			}
	
		 }
		stage('Maven Build') {
			steps {
				 echo 'Compile the code'
				 dir('devops'){
					sh './mvnw clean compile'
				 }
			}
	
		 }
		stage('Test On Master') {
			 steps {
				echo "Task1 on Master"
			}
        }
    }
}
