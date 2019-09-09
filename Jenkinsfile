pipeline {
	agent {
		label 'migration'
	}
	options {
		buildDiscarder(logRotator(numToKeepStr:'10'))
	}
	stages {
		stage('Build') {
			steps {
				withMaven(maven:'apache-maven-latest', mavenLocalRepo: '$WORKSPACE/.m2') {
					wrap([$class: 'Xvnc', useXauthority: true]) {
						sh 'mvn  clean verify -Dmaven.test.error.ignore=true -Dmaven.test.failure.ignore=true -PpackAndSign'
					}
				}
			}
			post {
				always {
					junit '*/target/surefire-reports/TEST-*.xml'
					archiveArtifacts artifacts: '*/target/work/data/.metadata/.log'
				}
			}
		}
		stage('Deploy') {
			when {
				branch 'master'
				// TODO deploy all branch from Eclipse.org Git repo
			}
			steps {
				sshagent ( ['projects-storage.eclipse.org-bot-ssh']) {
					// TODO compute the target URL (snapshots) according to branch name (0.5-snapshots...)
					sh '''
						ssh genie.tm4e@projects-storage.eclipse.org rm -rf /home/data/httpd/download.eclipse.org/tm4e/snapshots
						ssh genie.tm4e@projects-storage.eclipse.org mkdir -p /home/data/httpd/download.eclipse.org/tm4e/snapshots
						scp -r repository/target/repository/* genie.tm4e@projects-storage.eclipse.org:/home/data/httpd/download.eclipse.org/tm4e/snapshots
						scp repository/target/repository-*-SNAPSHOT.zip genie.tm4e@projects-storage.eclipse.org:/home/data/httpd/download.eclipse.org/tm4e/snapshots/repository.zip
					'''
				}
			}
		}
	}
}
