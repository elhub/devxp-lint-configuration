import jetbrains.buildServer.configs.kotlin.ArtifactRule
import jetbrains.buildServer.configs.kotlin.buildSteps.script
import no.elhub.devxp.build.configuration.pipeline.constants.AgentScope
import no.elhub.devxp.build.configuration.pipeline.constants.Group.DEVXP
import no.elhub.devxp.build.configuration.pipeline.dsl.elhubProject
import no.elhub.devxp.build.configuration.pipeline.jobs.customJob
import no.elhub.devxp.build.configuration.pipeline.jobs.makeVerify

elhubProject(DEVXP, "devxp-lint-configuration") {
    pipeline {
        sequential {
            val prepareFilesForSonarJob = customJob(
                AgentScope.LinuxAgentContext,
                buildArtifactRules = listOf(ArtifactRule.include("resources-sonar-temp", "resources-sonar-temp.zip")),
                outputArtifactRules = listOf(ArtifactRule.include("resources-sonar-temp.zip", "resources-sonar-temp"))
            ) {
                id("PrepareFilesForSonar")
                name = "\uD83D\uDCC4 Prepare dotfiles for SonarScan"

                steps {
                    script {
                        name = "Copy files into a temporary location"
                        scriptContent = """
                            |#!/bin/bash
                            |cp -R resources resources-sonar-temp
                        """.trimMargin()
                        workingDir = "."
                    }
                    script {
                        name = "Change dot files into non-dot files"
                        scriptContent = """
                            |#!/bin/bash
                            |for filename in $(ls -1a | grep "^\.[a-zA-Z0-9]"); do
                            |  if [ ! -d "${'$'}filename" ]; then
                            |      mv ${'$'}filename $(echo ${'$'}filename | sed 's/^.//')
                            |  fi
                            |done
                        """.trimMargin()
                        workingDir = "resources-sonar-temp"
                    }
                }
            }

            makeVerify {
                buildArtifactRules = listOf(ArtifactRule.include("resources-sonar-temp.zip"))

                sonarScanSettings = {
                    sonarProjectSources = "resources-sonar-temp/"
                    sonarProjectTests = "tests/"
                    additionalParams =
                        mutableListOf(
                            "-Dsonar.scm.exclusions.disabled=true",
                            "-Dsonar.lang.patterns.json=**/*.json,**/ecrc,**/jsonlintrc",
                            "-Dsonar.lang.patterns.yaml=**/*.yaml,**/*.yml,**/ansible-lint",
                            "-Dsonar.inclusions=*.json",
                            "-X"
                        )
                    outputArtifactRules = listOf(
                        ArtifactRule(
                            include = true,
                            src = "resources-sonar-temp.zip!**",
                            dst = "resources-sonar-temp"
                        )
                    )
                }
                enablePublishMetrics = false
                publishMetricsSettings = {
                    skipCodeCoverage = true
                }
                disableLint = true
            }.apply {
                this.buildType.dependencies {
                    artifacts(buildTypeId = prepareFilesForSonarJob.id) {
                        buildRule = lastFinished("%teamcity.build.branch%")
                        rules =  listOf(ArtifactRule.include("resources-sonar-temp.zip"))
                        cleanDestination = false
                    }
                }
            }
        }
    }

}
