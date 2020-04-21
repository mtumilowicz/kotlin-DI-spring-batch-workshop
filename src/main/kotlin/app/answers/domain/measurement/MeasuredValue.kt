package app.answers.domain.measurement

import app.answers.domain.anomaly.valuetoohigh.Limit
import java.math.BigDecimal

class MeasuredValue(private val raw: BigDecimal) {

    constructor(limit: String) : this(BigDecimal(limit))

    operator fun compareTo(limit: Limit): Int = compareValues(raw, limit.raw)
}