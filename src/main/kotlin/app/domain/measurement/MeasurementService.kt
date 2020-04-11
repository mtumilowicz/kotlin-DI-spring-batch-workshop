package app.domain.measurement

import app.domain.anomaly.AnomalyDetector
import app.domain.anomaly.AnomalyReport

class MeasurementService(
        private val detectors: List<AnomalyDetector>,
        private val measurementRepository: MeasurementRepository
) {

    fun printAllAnomalyReports() {
        measurementRepository.forAll { measurements ->
            measurements
                    .flatMap { measurement: Measurement -> detectAnomalies(measurement) }
                    .map { it.report }
                    .forEach(::println)
        }
    }

    private fun detectAnomalies(measurement: Measurement): Sequence<AnomalyReport> {
        return detectors.asSequence()
                .flatMap { detector -> detector.detectAll(measurement) }
    }
}