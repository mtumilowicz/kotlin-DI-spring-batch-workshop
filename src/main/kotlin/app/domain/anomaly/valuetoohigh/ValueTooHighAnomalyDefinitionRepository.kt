package app.domain.anomaly.valuetoohigh

interface ValueTooHighAnomalyDefinitionRepository {
    fun findAll(): Sequence<ValueTooHighAnomalyDefinition>
}