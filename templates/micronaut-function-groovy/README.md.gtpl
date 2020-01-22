# $functionNameNatural

This is a Micronaut AWS Lambda Function generated using [Micronaut Function Template][mfgt]

<!-- TODO: add your own description -->

[mfgt]: https://github.com/agorapulse/lazybones-templates#micronaut-function-groovy

## Getting Around

<!-- Feel free to delete this section when you get familiar with the project -->

Following files are waiting for you implementation

 * [${functionName}Service]($mainPackageRelativeDir/${functionName}Service.groovy)
 * [${functionName}ServiceSpec]($testPackageRelativeDir/${functionName}ServiceSpec.groovy)

## Local Execution

The function can be run using the embedded HTTP server at `http://localhost:$port`:

```
./gradlew run
```

Then you can execute the function using [IntelliJ HTTP Request File](${functionNameHyphens}.http) or cURL:

```
curl --header "Content-Type: application/json" --request POST --data '{ }' http://localhost:$port/$functionNameHyphens
```

The port can be changed by setting the `micronaut.server.port` property in the local [configuration file](src/main/resources/application-dev.yml).

## Deployment

You need to setup you AWS credentials before deploying this function. There are two ways how to achieve this:

Either ensure you have set up your credentials in `~/.aws/credentials` file:
```
[${ profile ?: 'default' }]
aws_access_key_id = ${ profile ?: 'adminuser' } access key ID
aws_secret_access_key = ${ profile ?: 'adminuser' } secret access key
<%if (region) { %>region = $region<% } %>
```

Or by using environmental variables by running following commands in your terminal:

```
export AWS_ACCESS_KEY_ID=${ profile ?: 'adminuser' } access key ID
export AWS_SECRET_ACCESS_KEY=${ profile ?: 'adminuser' } secret access key
<%if (region) { %>export AWS_DEFAULT_REGION=${region}<% } %>
```

Then you can deploy the function by running the following command:

```
./gradlew deploy
```

<% if (selectedLibs) { %>
## Libraries

You have selected the following libraries:
<% for (lib in selectedLibs) { %>
 * [$lib.name][$lib.name]<% if (lib.configuration) { %> - [CONFIGURATION REQUIRED](src/main/resources/application.yml)!<% } %>
<% } %>

Please, follow the links to read more about how to use the library in your project.

<% for (lib in selectedLibs) { %>
[$lib.name]: $lib.link
<% } %>

<% } %>
