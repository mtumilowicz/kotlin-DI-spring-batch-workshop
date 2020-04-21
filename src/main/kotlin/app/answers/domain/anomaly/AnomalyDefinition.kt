package app.answers.domain.anomaly

import app.answers.domain.measurement.Measurement

interface AnomalyDefinition {

    fun compliesWith(measurement: Measurement): Boolean
}