package app.domain.measurement

import app.domain.anomaly.valuetoohigh.Limit
import java.math.BigDecimal

class MeasuredValue(private val raw: BigDecimal) {

    constructor(limit: String) : this(BigDecimal(limit))


    operator fun compareTo(limit: Limit): Int =
            raw.compareTo(limit.raw)
}