package app.infrastructure.repo.measurement

import app.domain.measurement.Measurement
import app.domain.measurement.MeasurementRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.batch.item.ExecutionContext
import org.springframework.batch.item.json.JacksonJsonObjectReader
import org.springframework.batch.item.json.JsonItemReader
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.FileSystemResource
import org.springframework.stereotype.Component

@Component
class MeasurementBatchRepository(
        @Value("\${measurements.path}")
        val path: String
) : MeasurementRepository {

    override fun forAll(allConsumer: (Sequence<Measurement>) -> Unit) {
        val reader: JsonItemReader<MeasurementDTO> = measurementJsonReader()
        reader.open(ExecutionContext())
        try {
            allConsumer(findAll(reader))
        } finally {
            reader.close()
        }
    }

    private fun findAll(reader: JsonItemReader<MeasurementDTO>): Sequence<Measurement> =
            generateSequence { tryToRead(reader) }
                    .map(MeasurementDTO::toDomain)

    private fun tryToRead(reader: JsonItemReader<MeasurementDTO>): MeasurementDTO? =
            try {
                reader.read()
            } catch (e: Exception) {
                println("Cannot read a file: $path, reason: ${e.localizedMessage}")
                null
            }

    private fun measurementJsonReader(): JsonItemReader<MeasurementDTO> {
        val objectMapper = ObjectMapper()
        val jsonObjectReader = JacksonJsonObjectReader(MeasurementDTO::class.java)
        jsonObjectReader.setMapper(objectMapper)
        return JsonItemReaderBuilder<MeasurementDTO>()
                .jsonObjectReader(jsonObjectReader)
                .resource(FileSystemResource(path))
                .name("measurementJsonReader")
                .build()
    }

}