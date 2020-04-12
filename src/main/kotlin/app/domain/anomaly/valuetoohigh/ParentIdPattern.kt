package app.domain.anomaly.valuetoohigh

import app.domain.measurement.ParentId

class ParentIdPattern(val regex: Regex) {

    fun matches(parentId: ParentId): Boolean = regex.matches(parentId.raw)

    override fun toString(): String = "ParentIdPattern(${regex.pattern})"
}