package app.domain.anomaly

import app.domain.anomaly.valuetoohigh.ValueTooHighAnomalyDetector
import app.domain.measurement.DeviceId
import app.domain.measurement.MeasuredValue
import app.domain.measurement.Measurement
import app.domain.measurement.ParentId
import app.infrastructure.repo.anomaly.ValueTooHighAnomalyDefinitionInMemoryRepository
import spock.lang.Specification
import spock.lang.Unroll

class ValueTooHighAnomalyDetectorTest extends Specification {

    @Unroll("DetectAll for: #_parentId")
    def 'DetectAll'() {
        given:
        def detector = new ValueTooHighAnomalyDetector(new ValueTooHighAnomalyDefinitionInMemoryRepository())

        and:
        def measurement = new Measurement(
                new ParentId(_parentId),
                new DeviceId(_deviceId),
                new MeasuredValue(_measuredValue)
        )

        expect:
        detector.detectAllAnomaliesIn(measurement)*.report == anomalies

        where:
        _parentId | _deviceId   | _measuredValue || anomalies
        'D.21'    | 'deviceId1' | 15.0           || ['ValueTooHigh,ParentId(D.21),Limit(10.1)']
        'D.111'   | 'deviceId2' | 15.3           || ['ValueTooHigh,ParentId(D.111),Limit(10.1)']
        'D.11111' | 'deviceId2' | 150.5          || ['ValueTooHigh,ParentId(D.11111),Limit(10.1)',
                                                     'ValueTooHigh,ParentId(D.11111),Limit(20.2)']
        'A.22'    | 'deviceId3' | 20.0           || []

    }
}
