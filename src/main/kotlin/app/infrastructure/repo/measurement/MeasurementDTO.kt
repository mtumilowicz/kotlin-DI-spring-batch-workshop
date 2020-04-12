package app.infrastructure.repo.measurement

import app.domain.measurement.DeviceId
import app.domain.measurement.MeasuredValue
import app.domain.measurement.Measurement
import app.domain.measurement.ParentId
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class MeasurementDTO(val parentId: String,
                          val deviceId: String,
                          val measuredValue: String) {

    constructor() : this("", "", "")

    fun toDomain(): Measurement = Measurement(ParentId(parentId), DeviceId(deviceId), MeasuredValue(measuredValue))
}