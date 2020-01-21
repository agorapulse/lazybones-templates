# Lazybones Template

If you don't have Lazybones installed yet, you can use SDKMAN:

```
sdk install lazybones
lazybones config add bintrayRepositories agorapulse/lazybones-templates
```


Then you can create new project using the available templates
## Kordamp Groovy

Simple templates which generate a Groovy multiproject using [Kordamp][1]

The generated project will contain
 * Single subproject
 * Guide
 * GitHub Action Workflow for check, release and periodical Gradle watch dog
 
### Usage

Create new project using the available templates

```
lazybones create kordamp-groovy kordamp-test
```

[1]: http://kordamp.org/kordamp-gradle-plugins/

## Micronaut Function Groovy


