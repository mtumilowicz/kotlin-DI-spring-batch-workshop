package app.domain.anomaly

import app.domain.anomaly.valuetoohigh.ValueTooHighAnomalyDetector
import app.domain.measurement.Measurement
import app.infrastructure.repo.anomaly.ValueTooHighAnomalyDefinitionInMemoryRepository
import spock.lang.Specification
import spock.lang.Unroll

class ValueTooHighAnomalyDetectorTest extends Specification {

    @Unroll("DetectAll for: #_parentId")
    def 'DetectAll'() {
        given:
        def detector = new ValueTooHighAnomalyDetector(new ValueTooHighAnomalyDefinitionInMemoryRepository())

        and:
        def measurement = new Measurement(_parentId, _deviceId, _measuredValue)

        expect:
        detector.detectAll(measurement)*.report == anomalies

        where:
        _parentId | _deviceId   | _measuredValue || anomalies
        'D.21'    | 'deviceId1' | 15.0           || ['ValueTooHigh,D.21,10.1']
        'D.111'   | 'deviceId2' | 15.3           || ['ValueTooHigh,D.111,10.1']
        'D.11111' | 'deviceId2' | 150.5          || ['ValueTooHigh,D.11111,10.1', 'ValueTooHigh,D.11111,20.2']
        'A.22'    | 'deviceId3' | 20.0           || []

    }
}
