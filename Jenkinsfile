pipeline {
	agent migration
	options {
		buildDiscarder(logRotator(numToKeepStr:'10'))
	}
	stages {
		stage('Build') {
			steps {
				wrap([$class: 'Xvnc', useXauthority: true]) {
					sh 'mvn -Dmaven.repo.local=$WORKSPACE/.m2 clean verify -Dmaven.test.error.ignore=true -Dmaven.test.failure.ignore=true -PpackAndSign'
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
				// TODO compute the target URL (snapshots) according to branch name (0.5-snapshots...)
				sh 'rm -rf /home/data/httpd/download.eclipse.org/tm4e/snapshots'
				sh 'mkdir -p /home/data/httpd/download.eclipse.org/tm4e/snapshots'
				sh 'cp -r repository/target/repository/* /home/data/httpd/download.eclipse.org/tm4e/snapshots'
				sh 'cp repository/target/repository-*-SNAPSHOT.zip /home/data/httpd/download.eclipse.org/tm4e/snapshots/repository.zip'
			}
		}
	}
}
