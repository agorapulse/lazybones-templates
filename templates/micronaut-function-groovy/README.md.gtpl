# $functionNameNatural

This is a Micronaut AWS Lambda Function generated using [Micronaut Function Template][mfgt]

<!-- TODO: add your own description -->

[mfgt]: https://github.com/agorapulse/lazybones-templates#micronaut-function-groovy

## Getting Around

<!-- Feel free to delete this section when you get familiar with the project -->

The following files are waiting for you implementation:

 * [${functionName}Service]($mainPackageRelativeDir/${functionName}Service.groovy) - The main service
 * [${functionName}Configuration]($mainPackageRelativeDir/${functionName}Configuration.groovy) - The configuration object
 * [${functionName}ServiceSpec]($testPackageRelativeDir/${functionName}ServiceSpec.groovy) - The main service's test
<% if (isNewEvent(inputEventClass)) { %> * [$inputEventClass]($mainPackageRelativeDir/${inputEventClass}.groovy) - Input event<% } %>
<% if (isNewEvent(outputEventClass)) { %> * [$outputEventClass]($mainPackageRelativeDir/${outputEventClass}.groovy) - Output event<% } %>

The following files are infrastructure ones and should not be changed:
 * [Application]($mainPackageRelativeDir/Application.java) - Local server launcher
 * [${functionName}Handler]($mainPackageRelativeDir/${functionName}Handler.groovy) - AWS Lambda handler
 * [${functionName}HandlerSpec]($testPackageRelativeDir/${functionName}HandlerSpec.groovy) - AWS Lambda handler sanity test

There are two configuration files:
 * [application.yml](src/main/resources/application.yml) - Main configuration file for production and local server
 * [application-dev.yml](src/main/resources/application-dev.yml) - Main configuration file for local server only

<% if (!standalone) { %>
This function is part of a another project. Please, revisit [the Gradle build file](build.gradle) and [the Gradle properties file](gradle.properties)
and remove all unnecessary configuration.
<% } %>

<% if (slug) { %>
You need to [create new GitHub repository](https://github.com/new) called `$slug` if you haven't done so.

You need to configure [GitHub Secrets](https://github.com/$slug/settings/secrets) if you want to enable automated deployments:
 * `STAGING_AWS_ACCESS_KEY_ID` - AWS key ID for the staging environment
 * `STAGING_AWS_SECRET_ACCESS_KEY` - AWS secret key for the staging environment
 * `PRODUCTION_AWS_ACCESS_KEY_ID` - AWS key ID for the production environment
 * `PRODUCTION_AWS_SECRET_ACCESS_KEY` - AWS secret key for the production environment

Then tou can push this project to GitHub:
```
git init
git add -A
git commit -m "Initial commit"
git remote add origin git@github.com:${slug}.git
git push -u origin master
```

<% } %>

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

## Manual Deployment

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

After deployment you should be able to open the following links:

 * [Function][deployed]
 * [Logs][logs]

[deployed]: https://${region}.console.aws.amazon.com/lambda/home?region=${region}#/functions/${functionName}?tab=configuration
[logs]: https://${region}.console.aws.amazon.com/cloudwatch/home?region=${region}#logStream:group=/aws/lambda/${functionName};streamFilter=typeLogStreamPrefix

<% if (slug) { %>
## Continuous Integration and Delivery

This function has enabled continuous integration and delivery using GitHub Actions:

 * [Check](https://github.com/$slug/actions?query=workflow%3ACheck) - After each commit to any branch or any PR request opened the `./gradlew check` command is run (see [gradle.yml](.github/workflows/gradle.yml)))
 * [Gradle RC Watchdog](https://github.com/$slug/actions?query=workflow%3AGradle+RC+Watchdog) - Once month `master` branch is tested against the latest RC version of Gradle``./gradlew check` command is run (see [gradle-versions-watchdog.yml](.github/workflows/gradle-versions-watchdog.yml))
 * [Release to Staging](https://github.com/$slug/actions?query=workflow%3ARelease+to+Staging) - Every commit into `master` branch runs `./gradlew deploy` using `STAGING_AWS_ACCESS_KEY_ID` and  `STAGING_AWS_SECRET_ACCESS_KEY` secrets (see [staging.yml.gtpl](.github/workflows/staging.yml))
 * [Release to Production](https://github.com/$slug/actions?query=workflow%3ARelease+to+Production) - Every release (tag)  runs `./gradlew deploy` using `PRODUCTION_AWS_ACCESS_KEY_ID` and  `PRODUCTION_AWS_SECRET_ACCESS_KEY` secrets  (see [release.yml](.github/workflows/release.yml))

<% } %>

<% if (selectedLibs) { %>
## Libraries

<%
        for (lib in selectedLibs) {
            out << '\n  * ' << lib.name

            if (lib.link) {
                out << " - [Documentation][$lib.name]"
            }

            if (lib.documentation) {
                out << "\n    * $lib.documentation"
            }

            if (lib.configuration) {
                out << "\n    * **CONFIGURATION REQUIRED:** - visit [application.yml](src/main/resources/application.yml) configuration file to correct the placeholder values!"
            }

        }
%>

Please, follow the links to read more about how to use the library in your project.

<% for (lib in selectedLibs) { %>
[$lib.name]: $lib.link
<% } %>

<% } %>
