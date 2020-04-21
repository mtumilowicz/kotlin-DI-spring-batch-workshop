package app.answers.domain.anomaly.valuetoohigh

import app.answers.domain.anomaly.AnomalyDetector
import app.answers.domain.anomaly.AnomalyReport
import app.answers.domain.measurement.Measurement

class ValueTooHighAnomalyDetector(
        private val definitionRepository: ValueTooHighAnomalyDefinitionRepository
) : AnomalyDetector {

    override fun detectAllAnomaliesIn(measurement: Measurement): Sequence<AnomalyReport> =
            definitionRepository.findAll()
                    .filter { definition -> definition.compliesWith(measurement) }
                    .map { definition -> prepareReport(measurement, definition) }


    private fun prepareReport(measurement: Measurement, anomalyDefinition: ValueTooHighAnomalyDefinition): AnomalyReport =
            AnomalyReport("ValueTooHigh,${measurement.parentId},${anomalyDefinition.limit}")

}