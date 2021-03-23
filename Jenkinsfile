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
						
					}
					sh "sudo chmod -R 777 devops"
				}
			}
	
		 }
		stage('Maven Build') {
			steps {
				 echo 'Build/Compile the code'
				 dir('devops'){
					sh 'sudo ./mvnw clean package'
				 }
			}
	
		 }
		 stage ('create docker registry url') {
          steps {
            script {
              
                   def aws_account_number = "607187345607"
                   echo "${aws_account_number}"
                   def region = "eu-west-1"
                   def aws_url = aws_account_number.trim() + ".dkr.ecr." + region + ".amazonaws.com"
				   echo "---------------------------------------------------"
				   echo "${aws_url}"
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
