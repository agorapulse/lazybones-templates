package $pkg

import com.agorapulse.dru.Dru
import com.agorapulse.dru.dynamodb.persistence.DynamoDB
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper
import io.micronaut.context.ApplicationContext
import spock.lang.AutoCleanup
import spock.lang.Specification

class DynamoDBEntitySpec extends Specification {

    @AutoCleanup Dru dru = Dru.plan {
        from 'entities.json', {
            map 'entities', {
                to DynamoDBEntity
            }
        }
    }

    @AutoCleanup ApplicationContext context

    DynamoDBEntityDBService service

    void setup() {
        dru.load()

        context = ApplicationContext.build().build()
        context.registerSingleton(DynamoDBMapper, DynamoDB.createMapper(dru))
        context.start()

        service = context.getBean(DynamoDBEntityDBService)
    }

    void 'CRUD works'() {
        expect:
            service.get('1', '1')
            service.count('1') == 2

        when:
            service.save(new DynamoDBEntity(hashKey: '1', rangeKey: '3'))
        then:
            service.count('1') == 3

        when:
            service.delete('1', '2')
        then:
            service.count('1') == 2
    }

}
