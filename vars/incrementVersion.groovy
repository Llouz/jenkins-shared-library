
def call() {
    def currentVersion = sh(
        script: "mvn help:evaluate -Dexpression=project.version -q -DforceStdout",
        returnStdout: true
    ).trim()

    echo "Current version: ${currentVersion}"

    def matcher = currentVersion =~ /^(\d+)\.(\d+)\.(\d+)(-SNAPSHOT)?$/

    if (!matcher.matches()) {
        error "Unsupported Maven version: ${currentVersion}"
    }

    def major = matcher[0][1].toInteger()
    def minor = matcher[0][2].toInteger()
    def patch = matcher[0][3].toInteger() + 1

    def newVersion = "${major}.${minor}.${patch}"

    echo "New version: ${newVersion}"

    sh """
        mvn versions:set \
          -DnewVersion=${newVersion} \
          -DgenerateBackupPoms=false
    """

    return newVersion
}
