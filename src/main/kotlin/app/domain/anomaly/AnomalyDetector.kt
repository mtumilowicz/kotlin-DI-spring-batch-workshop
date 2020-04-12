package app.domain.anomaly

import app.domain.measurement.Measurement

interface AnomalyDetector {

    fun detectAllAnomaliesIn(measurement: Measurement): Sequence<AnomalyReport>
}