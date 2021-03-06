/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.performance.java

import org.gradle.performance.AbstractCrossVersionPerformanceTest
import spock.lang.Unroll

class IdeIntegrationPerformanceTest extends AbstractCrossVersionPerformanceTest {
    @Unroll("Project '#testProject' eclipse")
    def "eclipse"() {
        given:
        runner.testId = "eclipse $testProject"
        runner.testProject = testProject
        runner.tasksToRun = ['eclipse']
        runner.targetVersions = ["3.4-rc-3"]
        runner.gradleOpts = ["-Xms${maxMemory}", "-Xmx${maxMemory}"]

        when:
        def result = runner.run()

        then:
        result.assertCurrentVersionHasNotRegressed()

        where:
        testProject       | maxMemory
        "small"           | '128m'
        "multi"           | '128m'
        "lotDependencies" | '256m'
    }

    @Unroll("Project '#testProject' idea")
    def "idea"() {
        given:
        runner.testId = "idea $testProject"
        runner.testProject = testProject
        runner.tasksToRun = ['idea']
        runner.gradleOpts = ["-Xms${maxMemory}", "-Xmx${maxMemory}"]
        runner.targetVersions = ["3.4-rc-3"]
        runner.warmUpRuns = iterations
        runner.runs = iterations

        when:
        def result = runner.run()

        then:
        result.assertCurrentVersionHasNotRegressed()

        where:
        testProject            | maxMemory | iterations
        "small"                | '128m'    | null
        "multi"                | '128m'    | null
        "lotDependencies"      | '256m'    | null
        "largeEnterpriseBuild" | '8G'      | 5
    }
}
