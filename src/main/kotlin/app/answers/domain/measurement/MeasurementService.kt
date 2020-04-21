package app.answers.domain.measurement

import app.answers.domain.anomaly.AnomalyDetector
import app.answers.domain.anomaly.AnomalyReport

class MeasurementService(
        private val detectors: List<AnomalyDetector>,
        private val measurementRepository: MeasurementRepository
) {

    fun printAllAnomalyReports() =
            measurementRepository.forAll { measurements ->
                measurements
                        .flatMap { detectAnomalies(it) }
                        .map { it.report }
                        .forEach(::println)
            }

    private fun detectAnomalies(measurement: Measurement): Sequence<AnomalyReport> =
            detectors.asSequence()
                    .flatMap { detector -> detector.detectAllAnomaliesIn(measurement) }
}