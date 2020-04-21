package app.answers.domain.anomaly.valuetoohigh

import java.math.BigDecimal

data class Limit(val raw: BigDecimal) {

    constructor(limit: String) : this(BigDecimal(limit))

    override fun toString(): String = "Limit($raw)"

}