pipeline {
    agent any

    tools {
        maven 'MAVEN_HOME' // Ensure 'MAVEN_HOME' matches your Jenkins tool configuration
    }

    parameters {
        // Change from string to choice for better UX, listing available browsers
        choice(
            name: 'BROWSERS',
            choices: 'chrome\nfirefox\nsafari\nchrome,firefox', // Options are separated by a newline
            description: 'Select the browser(s) for parallel run.'
        )
        string(name: 'TEST_GROUPS', defaultValue: 'smoke', description: 'TestNG groups to include (e.g., smoke,regression).')
    }

    stages {
        stage('Initialize & Checkout') {
            steps {
                echo "Starting parallel test initialization."
                checkout scm
            }
        }

        stage('Parallel Cross-Browser Execution') {
            steps {
                script {
                    def branches = [:]

                    // Split the comma-separated or newline-separated parameter into a list
                    def browsersToRun = params.BROWSERS.tokenize(',').collect { it.trim() }

                    // If using a 'choice' parameter, it might come in as a single string
                    // of selected options, so use .tokenize() for robustness.

                    // Create dynamic parallel stages for each browser
                    for (browserName in browsersToRun) {
                        def branchName = "Test_on_${browserName.capitalize()}"

                        // Define the work for each parallel branch/shard
                        branches[branchName] = {
                            node('built-in') { // Run on a suitable agent
                                stage("Run ${branchName}") {
                                    // CRITICAL: Pass the browser name to your Java code via -Dbrowser
                                    // Removed -Dparallel.threads=1 to let TestNG parallelism (thread-count=4) work.
                                    sh "mvn clean test -Dbrowser=${browserName} -Dtest.groups=${params.TEST_GROUPS}"
                                }

                                // 4. Archive and report results for this specific shard/browser
                                archiveArtifacts artifacts: "target/surefire-reports/TEST-*.xml", fingerPrint: true
                                junit testResults: "target/surefire-reports/TEST-*.xml", allowEmptyResults: true
                            }
                        }
                    }

                    // 5. Execute all dynamic stages in parallel
                    parallel branches
                }
            }
        }

        stage('Publish Consolidated Results') {
            steps {
                echo "Consolidated test execution complete across all browsers."
            }
        }
    }
}