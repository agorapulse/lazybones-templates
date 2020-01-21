# Lazybones Template

If you don't have Lazybones installed yet, you can use SDKMAN:

```
sdk install lazybones
```

Then you need to add Agorapulse Lazybones repositories to the configuration:

```
lazybones config add bintrayRepositories agorapulse/lazybones-templates
```

Then you can create new project using the available templates
## Kordamp Groovy

Simple template which generates a Groovy multiproject using [Kordamp][1]

The generated project will contain
 * Single subproject
 * Guide
 * GitHub Action Workflow for check, release and periodical Gradle watch dog
 
### Usage

Create new project using the available template

```
lazybones create kordamp-groovy kordamp-test
```

[1]: http://kordamp.org/kordamp-gradle-plugins/

## Micronaut Function Groovy

Template which generate a Groovy [Micronaut][2] function. It allows you to select input and output event type
and also to easily include some of the useful Micronaut AWS libraries.

The generated project will contain
 * Micronaut Function
 * Tests
 * Logging to Sentry
 
### Usage

Create new project using the available template

```
lazybones create micronaut-function-groovy kordamp-test
```

[2]: http://micronaut.io


