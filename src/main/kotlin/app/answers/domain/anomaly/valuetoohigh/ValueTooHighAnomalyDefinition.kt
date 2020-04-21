package app.answers.domain.anomaly.valuetoohigh

import app.answers.domain.anomaly.AnomalyDefinition
import app.answers.domain.measurement.Measurement

class ValueTooHighAnomalyDefinition(
        private val parentIdPattern: ParentIdPattern,
        val limit: Limit
) : AnomalyDefinition {

    constructor(parentIdPattern: Regex, limit: Limit) :
            this(ParentIdPattern(parentIdPattern), limit)

    override fun compliesWith(measurement: Measurement): Boolean =
            measurement.parentIdMatches(parentIdPattern) and measurement.exceeds(limit)
}