package $pkg

import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.AmazonSQSClient
import io.micronaut.context.ApplicationContext
import org.testcontainers.containers.localstack.LocalStackContainer
import org.testcontainers.spock.Testcontainers
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

@Testcontainers
class AwsSpec extends Specification {

    @Shared LocalStackContainer localstack = new LocalStackContainer()
            .withServices(LocalStackContainer.Service.SQS)

    @AutoCleanup ApplicationContext context

    AmazonSQS sqs

    void setup() {
        sqs = AmazonSQSClient.builder()
                .withEndpointConfiguration(localstack.getEndpointConfiguration(LocalStackContainer.Service.SQS))
                .withCredentials(localstack.defaultCredentialsProvider)
                .build()

        context = ApplicationContext.build().build()
        context.registerSingleton(AmazonSQS, sqs)
        context.start()
    }

    void 'test queue created'() {
        expect:
            !sqs.listQueues().queueUrls
        when:
            String mainQueueURL = sqs.createQueue('Main').queueUrl
        then:
            sqs.listQueues().queueUrls.contains(mainQueueURL)
    }

}
