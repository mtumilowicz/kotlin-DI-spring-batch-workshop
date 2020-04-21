package app.answers.domain.measurement

import app.answers.domain.anomaly.valuetoohigh.Limit
import app.answers.domain.anomaly.valuetoohigh.ParentIdPattern

class Measurement(
        val parentId: ParentId,
        private val deviceId: DeviceId,
        private val measuredValue: MeasuredValue
) {

    fun parentIdMatches(pattern: ParentIdPattern): Boolean = pattern.matches(parentId)

    fun exceeds(limit: Limit): Boolean = measuredValue > limit
}