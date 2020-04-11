package app.domain.anomaly

import app.domain.measurement.Measurement

interface AnomalyDefinition {

    fun compliesWith(measurement: Measurement): Boolean
}