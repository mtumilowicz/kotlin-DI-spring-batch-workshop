package app

import app.domain.measurement.MeasurementService
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.context.annotation.Bean

@SpringBootApplication(exclude = [DataSourceAutoConfiguration::class])
class App {

    @Bean
    fun appRunner(measurementService: MeasurementService): ApplicationRunner {
        return ApplicationRunner { args: ApplicationArguments? ->
            try {
                measurementService.printAllAnomalyReports()
            } catch (ex: Exception) {
                println("Error when printing all anomalies: ${ex.localizedMessage}")
                System.exit(-1)
            }
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(App::class.java, *args)
        }
    }
}