package app.domain.measurement

import java.math.BigDecimal
import java.util.function.Predicate

class Measurement(
        val parentId: String,
        private val deviceId: String,
        private val measuredValue: BigDecimal
) {

    fun parentIdMatches(pattern: Predicate<String>): Boolean {
        return pattern.test(parentId)
    }

    fun exceedsLimit(limit: BigDecimal): Boolean {
        return measuredValue > limit
    }

}