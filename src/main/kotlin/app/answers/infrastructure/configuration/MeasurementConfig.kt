package app.answers.infrastructure.configuration

import app.answers.domain.anomaly.AnomalyDetector
import app.answers.domain.measurement.MeasurementRepository
import app.answers.domain.measurement.MeasurementService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MeasurementConfig {

    @Bean
    fun measurementService(detectors: List<AnomalyDetector>, measurementRepository: MeasurementRepository): MeasurementService =
            MeasurementService(detectors, measurementRepository)
}