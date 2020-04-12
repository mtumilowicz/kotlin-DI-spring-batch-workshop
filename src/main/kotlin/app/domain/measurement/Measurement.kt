package app.domain.measurement

import app.domain.anomaly.valuetoohigh.Limit
import app.domain.anomaly.valuetoohigh.ParentIdPattern

class Measurement(
        val parentId: ParentId,
        private val deviceId: DeviceId,
        private val measuredValue: MeasuredValue
) {

    fun parentIdMatches(pattern: ParentIdPattern): Boolean {
        return pattern.matches(parentId)
    }

    fun exceeds(limit: Limit): Boolean {
        return measuredValue > limit
    }

}