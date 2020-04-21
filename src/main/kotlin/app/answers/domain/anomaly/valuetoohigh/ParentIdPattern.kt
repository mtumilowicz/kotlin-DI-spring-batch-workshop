package app.answers.domain.anomaly.valuetoohigh

import app.answers.domain.measurement.ParentId

class ParentIdPattern(val regex: Regex) {

    fun matches(parentId: ParentId): Boolean = regex.matches(parentId.raw)

    override fun toString(): String = "ParentIdPattern(${regex.pattern})"
}