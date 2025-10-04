pipeline {
    agent any

    // 1. Define Jenkins Job Parameters (best practice, but you can rely on UI parameters)
    parameters {
        string(name: 'TEST_GROUPS', defaultValue: '', description: 'TestNG groups to include (e.g., group1,group2).')
        string(name: 'BROWSER_COUNT', defaultValue: '2', description: 'Number of parallel execution nodes/browsers.')
    }

    stages {
        stage('Checkout') {
            steps {
                // Replace with your actual SCM checkout
                checkout scm
            }
        }

        stage('Parallel Test Execution') {
            steps {
                script {
                    def branches = [:]
                    def numBrowsers = params.BROWSER_COUNT.toInteger()

                    // 2. Create dynamic parallel stages based on BROWSER_COUNT
                    for (int i = 1; i <= numBrowsers; i++) {
                        def branchName = "Test_Shard_${i}"

                        // Define the work for each parallel branch/shard
                        branches[branchName] = {
                            node('maven') { // Use a specific label for your build agents/nodes
                                stage("Run ${branchName}") {
                                    // 3. Construct the Maven command with parameters
                                    // -DbrowserId=${i} could be used to select a browser/config in your code
                                    // -Dtest.groups=${params.TEST_GROUPS} applies the group filter
                                    // -Dparallel.threads=1 ensures TestNG only uses one thread per shard, as sharding is handled by Jenkins
                                    sh "mvn clean test -DbrowserId=${i} -Dtest.groups=${params.TEST_GROUPS} -Dparallel.threads=1"
                                }

                                // 4. Archive results - VERY IMPORTANT: must use unique paths
                                // to avoid conflicts when all parallel stages finish.
                                archiveArtifacts artifacts: "target/surefire-reports/TEST-*.xml", fingerPrint: true

                                // Store results in a unique directory
                                junit testResults: "target/surefire-reports/TEST-*.xml", allowEmptyResults: true
                            }
                        }
                    }

                    // 5. Execute the dynamic stages in parallel
                    parallel branches
                }
            }
        }

        stage('Publish Consolidated Results') {
            // Note: If all parallel stages ran on the same workspace,
            // the final JUnit step would aggregate them.
            // In a distributed setup, results are typically gathered and published here.
            // Since we're archiving/publishing in each parallel step, this stage
            // is mainly for pipeline completeness or additional reporting.
            steps {
                echo "Consolidated test execution complete."
            }
        }
    }
}