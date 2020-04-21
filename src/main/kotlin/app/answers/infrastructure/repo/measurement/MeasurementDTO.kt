package app.answers.infrastructure.repo.measurement

import app.answers.domain.measurement.DeviceId
import app.answers.domain.measurement.MeasuredValue
import app.answers.domain.measurement.Measurement
import app.answers.domain.measurement.ParentId
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class MeasurementDTO(val parentId: String,
                          val deviceId: String,
                          val measuredValue: String) {

    constructor() : this("", "", "")

    fun toDomain(): Measurement = Measurement(ParentId(parentId), DeviceId(deviceId), MeasuredValue(measuredValue))
}