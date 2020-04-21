package app.answers.domain.anomaly

import app.answers.domain.anomaly.valuetoohigh.Limit
import app.answers.domain.anomaly.valuetoohigh.ValueTooHighAnomalyDefinition
import app.answers.domain.measurement.DeviceId
import app.answers.domain.measurement.MeasuredValue
import app.answers.domain.measurement.Measurement
import app.answers.domain.measurement.ParentId
import kotlin.text.Regex
import spock.lang.Specification

class ValueTooHighAnomalyDefinitionTest extends Specification {

    def 'CompliesWith'() {
        given: 'prepare definition with pattern and limit'
        def definition = new ValueTooHighAnomalyDefinition(
                new Regex('D.*1'),
                new Limit(15.3)
        )

        and: 'prepare measurement'
        def measurement = new Measurement(
                new ParentId(_parentId),
                new DeviceId(_deviceId),
                new MeasuredValue(_measuredValue)
        )

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
