# Lazybones Kordamp Template

Simple templates which generate a Groovy multiproject using [Kordamp][1]

The generated project will contain
 * Single subproject
 * Guide
 * GitHub Action Workflow for check, release and periodical Gradle watch dog
 
## Usage

First you need to register add `agorapulse/lazybones-templates` into `~/.lazybones/config.groovy`

```
bintrayRepositores = [
    "pledbrook/lazybones-templates",
    "agorapulse/lazybones-templates",
]
```

Then you can create new project using the avalable templates

```
lazybones create kordamp-groovy kordamp-test
```

[1]: http://kordamp.org/kordamp-gradle-plugins/
