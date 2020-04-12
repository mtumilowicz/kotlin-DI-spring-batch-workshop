package app.functional

import app.domain.measurement.MeasurementService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class MeasurementServiceTest extends Specification {

    @Autowired
    MeasurementService measurementService

    def 'printAll'() {
        given: 'intercept the System.out'
        def buffer = new ByteArrayOutputStream()
        System.out = new PrintStream(buffer)

        when: 'run the service with files from resources'
        measurementService.printAllAnomalyReports()

        then: 'verify out'
        def out = buffer.toString().split() as List
        out == ['ValueTooHigh,ParentId(DC1),Limit(100)', 'ValueTooHigh,ParentId(DC2),Limit(100)']
    }
}
