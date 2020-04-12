package app.domain.anomaly.valuetoohigh

import app.domain.anomaly.AnomalyDefinition
import app.domain.measurement.Measurement

class ValueTooHighAnomalyDefinition(
        private val parentIdPattern: ParentIdPattern,
        val limit: Limit
) : AnomalyDefinition {

    constructor(parentIdPattern: Regex, limit: Limit) :
            this(ParentIdPattern(parentIdPattern), limit)

    override fun compliesWith(measurement: Measurement): Boolean =
            measurement.parentIdMatches(parentIdPattern) and measurement.exceeds(limit)
}