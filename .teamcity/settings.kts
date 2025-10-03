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
                buildArtifactRules = listOf(ArtifactRule.include("dotfiles", "dotfiles.zip")),
                outputArtifactRules = listOf(ArtifactRule.include("dotfiles.zip", "dotfiles"))
            ) {
                id("PrepareFilesForSonar")
                name = "\uD83D\uDCC4 Prepare dotfiles for SonarScan"

                steps {
                    script {
                        name = "Copy files into a temporary location"
                        scriptContent = """
                            |#!/bin/bash
                            |mkdir dotfiles
                            |cp resources/.* -t dotfiles/ || echo "Ignoring errors for copy"
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
                        workingDir = "dotfiles"
                    }
                }
            }

            makeVerify {
                buildArtifactRules = listOf(ArtifactRule.include("dotfiles.zip"))

                sonarScanSettings = {
                    sonarProjectSources = "resources/,dotfiles/,Makefile"
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
                            src = "dotfiles.zip!**",
                            dst = "dotfiles"
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
                        rules =  listOf(ArtifactRule.include("dotfiles.zip"))
                        cleanDestination = false
                    }
                }
            }
        }
    }

}
