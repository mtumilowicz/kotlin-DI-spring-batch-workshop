package app.infrastructure.configuration

import app.domain.anomaly.valuetoohigh.ValueTooHighAnomalyDefinitionRepository
import app.domain.anomaly.valuetoohigh.ValueTooHighAnomalyDetector
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AnomalyDetectorConfig {

    @Bean
    fun valueTooHighAnomalyDetector(repository: ValueTooHighAnomalyDefinitionRepository): ValueTooHighAnomalyDetector =
            ValueTooHighAnomalyDetector(repository)
}