package app.domain.measurement

interface MeasurementRepository {
    fun forAll(allConsumer: (Sequence<Measurement>) -> Unit)
}