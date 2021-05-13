# Lazybones Template

Set of useful Lazybones templates for working on open source or Micronaut Function AWS projects.

## Installation

Lazybones is no longer part of SDKMAN. Use the following script to install it

```
mkdir -p ~/.lazybones/app
curl --show-error --location https://github.com/agorapulse/lazybones/releases/download/v0.8.3/lazybones-0.8.3.zip | tar -xf - -C ~/.lazybones/app
export PATH="$PATH:~/.lazybones/app/lazybones-0.8.3/bin"
```

## Available Templates 

### Kordamp Groovy

Simple template which generates a Groovy multiproject using [Kordamp][1]

The generated project will contain
 * Single subproject
 * Guide
 * GitHub Action Workflow for check, release and periodical Gradle watch dog
 
#### Usage

Create new project using the available template

```
lazybones create https://github.com/agorapulse/lazybones-templates/releases/download/1.1.13/kordamp-groovy-template-1.1.13.zip kordamp-test
```

[1]: http://kordamp.org/kordamp-gradle-plugins/

### Micronaut Function Groovy

Template which generate a Groovy [Micronaut][2] function. It allows you to select input and output event type
and also to easily include some of the useful Micronaut AWS libraries.

NOTE: This template is designed for event-based Micronaut functions. For [API Gateway Support](https://micronaut-projects.github.io/micronaut-aws/latest/guide/#apiProxy) with controllers use [Micronaut CLI](https://docs.micronaut.io/latest/guide/index.html#buildCLI)

The generated project will contain
 * Micronaut Function
 * Tests
 * Logging to Sentry
 
#### Usage

Create new project using the available template

```
lazybones create https://github.com/agorapulse/lazybones-templates/releases/download/1.1.13/micronaut-function-groovy-template-1.1.13.zip micronaut-template-test
```

[2]: http://micronaut.io


