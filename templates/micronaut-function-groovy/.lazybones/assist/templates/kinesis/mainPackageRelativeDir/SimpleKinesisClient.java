package $pkg;

import com.agorapulse.micronaut.aws.kinesis.annotation.KinesisClient;
import com.amazonaws.services.kinesis.model.PutRecordResult;

@KinesisClient
public interface SimpleKinesisClient {

    /**
     * Puts a record into Kinesis stream.
     *
     * @param record record to be pushed into the stream, you can change this to object which will be serialized automatically
     * @return result of the operation
     * @link https://agorapulse.github.io/micronaut-libraries/#_publishing_with_kinesisclient
     */
    PutRecordResult putRecordString(String record);
}
