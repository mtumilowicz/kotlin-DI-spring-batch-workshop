package app.answers.infrastructure.repo.anomaly

import app.answers.domain.anomaly.valuetoohigh.Limit
import app.answers.domain.anomaly.valuetoohigh.ValueTooHighAnomalyDefinition

data class ValueTooHighAnomalyDefinitionDTO(
        val parentPattern: String,
        val limit: String
) {

    fun toDomain(): ValueTooHighAnomalyDefinition = ValueTooHighAnomalyDefinition(parentPattern.toRegex(), Limit(limit))
}