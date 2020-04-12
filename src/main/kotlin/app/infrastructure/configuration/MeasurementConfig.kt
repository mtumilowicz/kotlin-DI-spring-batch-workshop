package app.infrastructure.configuration

import app.domain.anomaly.AnomalyDetector
import app.domain.measurement.MeasurementRepository
import app.domain.measurement.MeasurementService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MeasurementConfig {

    @Bean
    fun measurementService(detectors: List<AnomalyDetector>, measurementRepository: MeasurementRepository): MeasurementService =
            MeasurementService(detectors, measurementRepository)
}