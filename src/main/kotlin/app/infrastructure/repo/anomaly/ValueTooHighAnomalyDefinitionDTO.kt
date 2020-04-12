package app.infrastructure.repo.anomaly

import app.domain.anomaly.valuetoohigh.Limit
import app.domain.anomaly.valuetoohigh.ValueTooHighAnomalyDefinition

data class ValueTooHighAnomalyDefinitionDTO(
        val parentPattern: String,
        val limit: String
) {

    fun toDomain(): ValueTooHighAnomalyDefinition {
        return ValueTooHighAnomalyDefinition(parentPattern.toRegex(), Limit(limit))
    }
}