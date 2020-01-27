# Lazybones Template

Set of useful Lazybones templates for working on open source or Micronaut Function AWS projects.

## Installation

If you don't have Lazybones installed yet, you can use SDKMAN:

```
sdk install lazybones
```

Then you need to add Agorapulse Lazybones repositories to the configuration:

```
lazybones config add bintrayRepositories agorapulse/lazybones-templates
```


### Troubleshooting

#### Exception in thread "main" java.io.FileNotFoundException: ~/.lazybones/managed-config.json (No such file or directory)

You need to manually create file `~/.lazybones/managed-config.json` with the content `{ }` and try again.

#### The user configuration file overrides this setting, so the new value won't take effect

You need to manually edit file `~/.lazybones/config.groovy`:

```
bintrayRepositores = [
    "pledbrook/lazybones-templates",
    "agorapulse/lazybones-templates",
]
```

Then you can create new project using the available templates

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
lazybones create kordamp-groovy kordamp-test
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
lazybones create micronaut-function-groovy micronaut-template-test
```

[2]: http://micronaut.io


