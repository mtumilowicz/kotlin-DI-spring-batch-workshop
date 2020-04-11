package app.infrastructure.repo.anomaly

import app.domain.anomaly.valuetoohigh.ValueTooHighAnomalyDefinition
import java.math.BigDecimal

data class ValueTooHighAnomalyDefinitionDTO(
        val parentPattern: String,
        val limit: String
) {

    fun toDomain(): ValueTooHighAnomalyDefinition {
        return ValueTooHighAnomalyDefinition(parentPattern, BigDecimal(limit))
    }
}