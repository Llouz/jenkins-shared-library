
def call() {

    def currentVersion = sh(
        script: 'mvn help:evaluate -Dexpression=project.version -q -DforceStdout',
        returnStdout: true
    ).trim()

    echo "Current version: ${currentVersion}"

    // Remove SNAPSHOT suffix
    def cleanVersion = currentVersion.replace('-SNAPSHOT', '')

    // Split version into three parts
    def parts = cleanVersion.split('\\.')

    if (parts.length != 3 ||
        !parts.every { it ==~ /[0-9]+/ }) {
        error "Unsupported Maven version: ${currentVersion}"
    }

    def major = parts[0].toInteger()
    def minor = parts[1].toInteger()
    def patch = parts[2].toInteger() + 1

    def newVersion = "${major}.${minor}.${patch}-SNAPSHOT"

    echo "New version: ${newVersion}"

    sh """
        mvn versions:set \
            -DnewVersion=${newVersion} \
            -DgenerateBackupPoms=false
    """

    return newVersion
}
