package app.answers.domain.measurement

import app.answers.domain.anomaly.valuetoohigh.Limit
import app.answers.domain.anomaly.valuetoohigh.ParentIdPattern
import kotlin.text.Regex
import spock.lang.Specification

class MeasurementTest extends Specification {
    def 'ParentIdMatches'() {
        given:
        def measurement = new Measurement(
                new ParentId('parentId1'),
                new DeviceId('deviceId1'),
                new MeasuredValue(110.1)
        )

        expect:
        measurement.parentIdMatches(new ParentIdPattern(new Regex(_pattern))) == result

        where:
        _pattern     || result
        "[a-zA-Z]"   || false
        "[a-zA-z]+1" || true
    }

    def 'ExceedsLimit'() {
        given:
        def measurement = new Measurement(
                new ParentId('parentId1'),
                new DeviceId('deviceId1'),
                new MeasuredValue(110.1)
        )

        expect:
        measurement.exceeds(new Limit(_limit)) == exceeds

        where:
        _limit || exceeds
        10.0   || true
        110.1  || false
        200.0  || false
    }
}
