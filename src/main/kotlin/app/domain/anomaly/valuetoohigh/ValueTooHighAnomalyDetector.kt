package app.domain.anomaly.valuetoohigh

import app.domain.anomaly.AnomalyDetector
import app.domain.anomaly.AnomalyReport
import app.domain.measurement.Measurement

class ValueTooHighAnomalyDetector(private val repository: ValueTooHighAnomalyDefinitionRepository) : AnomalyDetector {

    override fun detectAll(measurement: Measurement): Sequence<AnomalyReport> {
        return repository.findAll()
                .filter { it.compliesWith(measurement) }
                .map { prepareReport(measurement, it) }
    }

    private fun prepareReport(measurement: Measurement, anomalyDefinition: ValueTooHighAnomalyDefinition): AnomalyReport {
        return AnomalyReport("ValueTooHigh,${measurement.parentId},${anomalyDefinition.limit}")
    }
}