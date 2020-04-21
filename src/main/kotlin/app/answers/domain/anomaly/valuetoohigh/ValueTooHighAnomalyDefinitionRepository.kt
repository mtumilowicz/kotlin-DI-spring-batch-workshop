package app.answers.domain.anomaly.valuetoohigh

interface ValueTooHighAnomalyDefinitionRepository {

    fun findAll(): Sequence<ValueTooHighAnomalyDefinition>
}