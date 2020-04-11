package app.infrastructure.repo.measurement

import app.domain.measurement.Measurement
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.math.BigDecimal

@JsonIgnoreProperties(ignoreUnknown = true)
data class MeasurementDTO(val parentId: String,
                          val deviceId: String,
                          val measuredValue: String) {

    constructor() : this("", "", "")

    fun toDomain(): Measurement {
        return Measurement(parentId, deviceId, BigDecimal(measuredValue))
    }
}