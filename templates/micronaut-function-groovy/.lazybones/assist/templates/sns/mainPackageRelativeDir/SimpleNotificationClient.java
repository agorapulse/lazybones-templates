package $pkg;

import com.agorapulse.micronaut.aws.sns.annotation.NotificationClient;

@NotificationClient
public interface SimpleNotificationClient {

    /**
     * Sends a message to the notification topic.
     *
     * @param message message to be pushed into the topic, you can change this to object which will be serialized automatically
     * @return message id
     * @link https://agorapulse.github.io/micronaut-libraries/#_publishing_with_notificationclient
     */
    String publishMessage(String message);

}
