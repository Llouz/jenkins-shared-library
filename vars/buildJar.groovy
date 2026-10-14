#!/usr/bin/env groovy

def call() {
    echo "building the jar..."
    sh 'mvn package -DskipTests'
}
