package app.answers.infrastructure.configuration

import app.answers.domain.anomaly.valuetoohigh.ValueTooHighAnomalyDefinitionRepository
import app.answers.domain.anomaly.valuetoohigh.ValueTooHighAnomalyDetector
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AnomalyDetectorConfig {

    @Bean
    fun valueTooHighAnomalyDetector(repository: ValueTooHighAnomalyDefinitionRepository): ValueTooHighAnomalyDetector =
            ValueTooHighAnomalyDetector(repository)
}