package app.domain.anomaly

import app.domain.anomaly.valuetoohigh.ValueTooHighAnomalyDefinition
import app.domain.measurement.Measurement
import spock.lang.Specification

class ValueTooHighAnomalyDefinitionTest extends Specification {

    def 'CompliesWith'() {
        given: 'prepare definition with pattern and limit'
        def definition = new ValueTooHighAnomalyDefinition('D.*1', 15.3)

        and: 'prepare measurement'
        def measurement = new Measurement(_parentId, _deviceId, _measuredValue)

        expect: 'find anomalies'
        definition.compliesWith(measurement) == anomaly

        where:
        _parentId | _deviceId   | _measuredValue || anomaly
        'D.21'    | 'deviceId1' | 15.0           || false
        'D.111'   | 'deviceId2' | 15.3           || false
        'D.331'   | 'deviceId3' | 20.0           || true
        'A.21'    | 'deviceId4' | 20.0           || false
    }
}
