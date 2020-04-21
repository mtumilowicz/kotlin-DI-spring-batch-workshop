package app.answers.domain.anomaly

import app.answers.domain.measurement.Measurement

interface AnomalyDetector {

    fun detectAllAnomaliesIn(measurement: Measurement): Sequence<AnomalyReport>
}