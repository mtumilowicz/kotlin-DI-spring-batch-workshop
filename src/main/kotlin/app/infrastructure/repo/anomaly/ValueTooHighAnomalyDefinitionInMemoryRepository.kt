package app.infrastructure.repo.anomaly

import app.domain.anomaly.valuetoohigh.Limit
import app.domain.anomaly.valuetoohigh.ValueTooHighAnomalyDefinition
import app.domain.anomaly.valuetoohigh.ValueTooHighAnomalyDefinitionRepository

class ValueTooHighAnomalyDefinitionInMemoryRepository : ValueTooHighAnomalyDefinitionRepository {

    private val data = arrayOf(
            ValueTooHighAnomalyDefinition(Regex("D.*1"), Limit("10.1")),
            ValueTooHighAnomalyDefinition(Regex("D.1*1"), Limit("20.2")),
            ValueTooHighAnomalyDefinition(Regex("A.22"), Limit("100.0"))
    )

    override fun findAll(): Sequence<ValueTooHighAnomalyDefinition> = data.asSequence()
}