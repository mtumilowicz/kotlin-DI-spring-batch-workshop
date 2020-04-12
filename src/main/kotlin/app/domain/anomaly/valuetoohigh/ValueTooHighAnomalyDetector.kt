package app.domain.anomaly.valuetoohigh

import app.domain.anomaly.AnomalyDetector
import app.domain.anomaly.AnomalyReport
import app.domain.measurement.Measurement

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