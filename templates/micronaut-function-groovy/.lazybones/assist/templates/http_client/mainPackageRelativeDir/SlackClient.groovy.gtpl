package $pkg;

import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.validation.Validated;

import javax.validation.constraints.NotBlank;

@Validated
@Client('\${slack.url}')
public interface SlackClient {

    /**
     * Post a message to Slack
     * @return response from Slack API
     */
    @Post(value = '/\${slack.app}/\${slack.key}/\${slack.secret}')
    String postMessage(@NotBlank String text);

}
