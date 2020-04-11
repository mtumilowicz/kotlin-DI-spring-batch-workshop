package app.domain.measurement

import spock.lang.Specification

import java.util.regex.Pattern

class MeasurementTest extends Specification {
    def 'ParentIdMatches'() {
        given:
        def measurement = new Measurement('parentId1', 'deviceId1', 110.1)

        expect:
        measurement.parentIdMatches(_pattern) == result

        where:
        _pattern                                         || result
        Pattern.compile("[a-zA-Z]").asMatchPredicate()   || false
        Pattern.compile("[a-zA-z]+1").asMatchPredicate() || true
    }

    def 'ExceedsLimit'() {
        given:
        def measurement = new Measurement('parentId1', 'deviceId1', 110.1)

        expect:
        measurement.exceedsLimit(_limit) == exceeds

        where:
        _limit || exceeds
        10.0   || true
        110.1  || false
        200.0  || false
    }
}
