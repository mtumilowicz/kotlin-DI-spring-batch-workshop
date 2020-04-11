package app.domain.anomaly.valuetoohigh

import app.domain.anomaly.AnomalyDefinition
import app.domain.measurement.Measurement
import java.math.BigDecimal
import java.util.function.Predicate
import java.util.regex.Pattern

class ValueTooHighAnomalyDefinition(
        private val parentPattern: Predicate<String>,
        val limit: BigDecimal
) : AnomalyDefinition {

    constructor(parentPattern: String, limit: BigDecimal) : this(Pattern.compile(parentPattern).asMatchPredicate(), limit)

    override fun compliesWith(measurement: Measurement): Boolean {
        return measurement.parentIdMatches(parentPattern) and measurement.exceedsLimit(limit)
    }
}