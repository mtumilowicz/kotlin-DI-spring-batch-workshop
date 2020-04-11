package app.infrastructure.repo.anomaly

import app.domain.anomaly.valuetoohigh.ValueTooHighAnomalyDefinition
import app.domain.anomaly.valuetoohigh.ValueTooHighAnomalyDefinitionRepository
import java.math.BigDecimal

class ValueTooHighAnomalyDefinitionInMemoryRepository : ValueTooHighAnomalyDefinitionRepository {

    private val data = arrayOf(
            ValueTooHighAnomalyDefinition("D.*1", BigDecimal("10.1")),
            ValueTooHighAnomalyDefinition("D.1*1", BigDecimal("20.2")),
            ValueTooHighAnomalyDefinition("A.22", BigDecimal("100.0"))
    )

    override fun findAll(): Sequence<ValueTooHighAnomalyDefinition> {
        return data.asSequence()
    }
}