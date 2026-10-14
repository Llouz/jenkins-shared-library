#!/usr/bin/env groovy
package com.example

class Docker implements Serializable {

    def script

    Docker(script) {
        this.script = script
    }

    def buildDockerImage(String imageName) {
        script.echo "building the docker image ${imageName}..."
        script.sh "docker build -t ${imageName} ."
    }

    def dockerLogin() {
        script.withCredentials([script.usernamePassword(
                credentialsId: 'docker-hub-repo',
                usernameVariable: 'USER',
                passwordVariable: 'PASS')]) {
            script.sh 'echo $PASS | docker login -u $USER --password-stdin'
        }
    }

    def dockerPush(String imageName) {
        script.retry(3) {
            script.sh "docker push ${imageName}"
        }
    }
}
