package app.domain.anomaly.valuetoohigh

import app.domain.measurement.Measurement
import java.math.BigDecimal
import java.util.function.Predicate
import java.util.regex.Pattern

class ValueTooHighAnomalyDefinition private constructor(private val parentPattern: Predicate<String>,
                                                        val limit: BigDecimal) {

    constructor(parentPattern: String, limit: BigDecimal) : this(Pattern.compile(parentPattern).asMatchPredicate(), limit)

    fun compliesWith(measurement: Measurement): Boolean {
        return measurement.parentIdMatches(parentPattern) && measurement.exceedsLimit(limit)
    }
}