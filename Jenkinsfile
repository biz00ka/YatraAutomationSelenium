pipeline {
    agent any

    tools {
            // Use the name you gave the Maven installation in Global Tool Configuration
            maven 'MAVEN_HOME'
        }

    parameters {
        // Updated to use a comma-separated list of browsers
        string(name: 'BROWSERS', defaultValue: 'chrome,firefox', description: 'Comma-separated list of browsers for parallel run (e.g., chrome,firefox).')
        string(name: 'TEST_GROUPS', defaultValue: 'smoke', description: 'TestNG groups to include (e.g., smoke,regression).')
    }

    stages {
            // [REMOVE] or simplify this stage if it was only for checkout
            stage('Initialize') {
                steps {
                    echo "Starting parallel test initialization."
                }
            }

        stage('Parallel Cross-Browser Execution') {
            steps {
                script {
                    def branches = [:]

                    // Split the input parameter into a list of browsers to execute
                    def browsersToRun = params.BROWSERS.tokenize(',')

                    // Create dynamic parallel stages for each browser
                    for (browserName in browsersToRun) {
                        def branchName = "Test_on_${browserName.trim().capitalize()}"

                        // Define the work for each parallel branch/shard
                        branches[branchName] = {
                            // Run on any available Jenkins agent with the 'maven' label
                            node('built-in') {
                                stage("Setup ${branchName}") {

                                    checkout scm
                                }
                                stage("Run ${branchName}") {
                                    // 3. Pass the specific browser name and TestNG group to Maven
                                    // -Dbrowser is read by your DriverFactory.java
                                    // -Dtest.groups is read by the pom.xml Surefire plugin
                                    sh "mvn clean test -Dbrowser=${browserName.trim()} -Dtest.groups=${params.TEST_GROUPS}"
                                }

                                // 4. Archive and report results for this specific shard/browser
                                // Note: Results from each parallel stage must be published separately
                                // and Jenkins will aggregate them.
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