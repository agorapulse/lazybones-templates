package $pkg;

import com.agorapulse.micronaut.aws.sqs.annotation.QueueClient;

@QueueClient
public interface SimpleQueueClient {

    /**
     * Sends a message to the default queue.
     *
     * @param record record to be pushed into the queue, you can change this to object which will be serialized automatically
     * @return message id
     * @link https://agorapulse.github.io/micronaut-libraries/#_publishing_with_queueclient
     */
    String sendMessage(String record);

}
