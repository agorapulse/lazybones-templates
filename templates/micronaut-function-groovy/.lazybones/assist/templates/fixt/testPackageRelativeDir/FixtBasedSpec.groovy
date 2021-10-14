package $pkg

import com.agorapulse.testing.fixt.Fixt
import spock.lang.Specification

class FixtBasedSpec extends Specification {

    Fixt fixt = Fixt.create(FixtBasedSpec)

    void 'load test resources file'() {
        expect:
            fixt.readText('hello.txt') == 'Hello World'
    }

}
