package app.domain.anomaly

import app.domain.measurement.Measurement

interface AnomalyDetector {

    fun detectAll(measurement: Measurement): Sequence<AnomalyReport>
}