import no.elhub.devxp.build.configuration.pipeline.constants.Group.DEVXP
import no.elhub.devxp.build.configuration.pipeline.dsl.elhubProject
import no.elhub.devxp.build.configuration.pipeline.jobs.makeVerify

elhubProject(DEVXP, "devxp-lint-configuration") {
    pipeline {
        sequential {
            makeVerify {
                sonarScanSettings = {
                    sonarProjectSources = "resources/,Makefile"
                    additionalParams =
                        mutableListOf(
                            "-Dsonar.scm.exclusions.disabled=true"
                        )
                }
                enablePublishMetrics = true
                publishMetricsSettings = {
                    skipCodeCoverage = true
                }
            }
        }
    }
}
