import no.elhub.devxp.build.configuration.pipeline.ElhubProject.Companion.elhubProject
import no.elhub.devxp.build.configuration.pipeline.constants.Group.DEVXP
import no.elhub.devxp.build.configuration.pipeline.jobs.makeVerify

elhubProject(DEVXP, "devxp-lint-configuration") {
    pipeline {
        sequential {
            makeVerify {
                sonarScanSettings = {
                    sonarProjectSources = "resources/,Makefile"
                }
                enablePublishMetrics = true
                publishMetricsSettings = {
                    skipCodeCoverage = true
                }
            }
        }
    }
}
