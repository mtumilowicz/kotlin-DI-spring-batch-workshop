package app.answers.infrastructure.repo.anomaly

import app.answers.domain.anomaly.valuetoohigh.ValueTooHighAnomalyDefinition
import app.answers.domain.anomaly.valuetoohigh.ValueTooHighAnomalyDefinitionRepository
import com.google.gson.Gson
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.file.Files
import java.nio.file.Paths

@Component
class ValueTooHighAnomalyDefinitionFileRepository(
        @Value("\${anomaly.definition.valueTooHigh.file.path}")
        val path: String
) : ValueTooHighAnomalyDefinitionRepository {

    private val gson: Gson = Gson()

    override fun findAll(): Sequence<ValueTooHighAnomalyDefinition> {
        return try {
            Files.newBufferedReader(Paths.get(path)).use {
                gson.fromJson(it, Array<ValueTooHighAnomalyDefinitionDTO>::class.java)
                        .map { dto -> dto.toDomain() }
                        .asSequence()
            }
        } catch (e: Exception) {
            println("Problem with IO with the file: $path, reason: ${e.localizedMessage}")
            emptySequence()
        }
    }
}