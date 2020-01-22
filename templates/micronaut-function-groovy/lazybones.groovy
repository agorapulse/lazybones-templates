@Grapes(
    @Grab(group='org.yaml', module='snakeyaml', version='1.25')
)

import uk.co.cacoethes.util.NameType
import org.apache.commons.io.FileUtils
import org.yaml.snakeyaml.Yaml
import groovy.transform.Field

@Field Yaml yaml = new Yaml()

final String GIT_IGNORE_TEXT = """
# Gradle
build/
.gradle/

# IDEs

*.c9
*.iml
*.ipr
*.iws
*.vscode
.idea/
.asscache
MANIFEST.MF
out

# PAW
*.paw

# Redis
*.rdb

"""

String functionNameSuggestion = transformText(projectDir.name, from: NameType.HYPHENATED, to: NameType.CAMEL_CASE)
String functionName = ask("What is the name of the function 'AccountStatistics' [$functionNameSuggestion]: ", functionNameSuggestion, "name")

String groupSuggestion = 'com.' + projectDir.parentFile.name.replaceAll('[-_]', '.').toLowerCase()
String group = ask("What is the group (base package) of the new project [$groupSuggestion]: ", groupSuggestion, "group")

String pgkSuggestion = (group + '.' + transformText(functionName, from:  NameType.CAMEL_CASE, to: NameType.HYPHENATED)).replaceAll('[-_]', '.').toLowerCase()
String pkg = ask("What is the base package of the new project [$pgkSuggestion]: ", pgkSuggestion, "pkg")

String region = ask("In which region you want to create the function [eu-west-1]: ", 'eu-west-1', "region")
String profile = ask("Which AWS profile you want to use for deployment [beta]: ", 'beta', "profile")
String port = ask("Which port would yoo like to use for running the function locally [8080]: ", '8080', "port")

String inputEventClass = readClass([req: "${functionName}Request"] + loadAssist('input-events.yml'), 'input')
String outputEventClass = readClass([resp: "${functionName}Response"] + loadAssist('output-events.yml'), 'output')
List<String> selectedLibs = readLibs(loadAssist('libraries.yml'))
// clean up .lazybones directory
FileUtils.deleteDirectory(new File(templateDir, ".lazybones/assist"))

String functionNameHyphens = transformText(functionName, from: NameType.CAMEL_CASE, to: NameType.HYPHENATED)
String functionNameProperty = transformText(functionName, from: NameType.CAMEL_CASE, to: NameType.PROPERTY)
String functionNameNatural = transformText(functionName, from: NameType.CAMEL_CASE, to: NameType.NATURAL)
String inputEventClassSimple = extractSimpleName(inputEventClass)
String outputEventClassSimple = extractSimpleName(outputEventClass)

String packageAsDir = pkg.replace('.', '/')
String mainPackageRelativeDir = "src/main/groovy/$packageAsDir"
String mainResourcesRelativeDir = "src/main/resources/"
String testPackageRelativeDir = "src/test/groovy/$packageAsDir"
String testResourcesRelativeDir = "src/test/resources/"
String testPackageResourcesRelativeDir = "src/test/resources/$packageAsDir"

File mainPackageDir = new File(projectDir, mainPackageRelativeDir)
File mainResourcesDir = new File(projectDir, mainResourcesRelativeDir)
File testPackageDir = new File(projectDir, testPackageRelativeDir)
File testResourcesDir = new File(projectDir, testResourcesRelativeDir)
File testPackageResourcesDir = new File(projectDir, testPackageResourcesRelativeDir)

mainPackageDir.mkdirs()
mainResourcesDir.mkdirs()
testPackageDir.mkdirs()
testResourcesDir.mkdirs()
testPackageResourcesDir.mkdirs()

Map attrs = [
        // props
        functionName: functionName,
        functionNameHyphens: functionNameHyphens,
        functionNameProperty: functionNameProperty,
        functionNameNatural: functionNameNatural,
        group: group,
        pkg: pkg,
        port: port,
        inputEventClass: inputEventClass,
        inputEventClassSimple: inputEventClassSimple,
        outputEventClass: outputEventClass,
        outputEventClassSimple: outputEventClassSimple,
        selectedLibs: selectedLibs,
        region: region,
        profile: profile,
        packageAsDir: packageAsDir,
        mainPackageRelativeDir: mainPackageRelativeDir,
        mainResourcesRelativeDir: mainResourcesRelativeDir,
        testPackageRelativeDir: testPackageRelativeDir,
        testResourcesRelativeDir: testResourcesRelativeDir,
        testPackageResourcesRelativeDir: testPackageResourcesRelativeDir,
        mainPackageDir: mainPackageDir,
        mainResourcesDir: mainResourcesDir,
        testPackageDir: testPackageDir,
        testResourcesDir: testResourcesDir,
        testPackageResourcesDir: testPackageResourcesDir,

        // methods
        isNewEvent: this.&isNewEvent,
        requiresImport: this.&requiresImport,
        newEventString: this.&newEventString,
        toYaml: this.&toYaml,
]

safeProcessTemplates "settings.gradle", attrs
safeProcessTemplates "build.gradle", attrs
safeProcessTemplates "gradle.properties", attrs
safeProcessTemplates "micronaut-cli.yml", attrs
safeProcessTemplates "README.md", attrs
safeProcessTemplates "request.http", attrs

safeProcessTemplates "src/main/resources/sentry.properties", attrs
safeProcessTemplates "src/main/resources/application.yml", attrs
safeProcessTemplates "src/main/resources/application-dev.yml", attrs
safeProcessTemplates "src/main/groovy/*.java", attrs
safeProcessTemplates "src/main/groovy/*.groovy", attrs
safeProcessTemplates "src/test/groovy/*.groovy", attrs

FileUtils.moveFile(new File(templateDir, "request.http"), new File(projectDir, "${functionNameHyphens}.http"))

FileUtils.moveFile(new File(templateDir, "src/main/groovy/Application.java"), new File(mainPackageDir, 'Application.java'))
FileUtils.moveFile(new File(templateDir, "src/main/groovy/Handler.groovy"), new File(mainPackageDir, "${functionName}Handler.groovy"))
FileUtils.moveFile(new File(templateDir, "src/main/groovy/Service.groovy"), new File(mainPackageDir, "${functionName}Service.groovy"))
FileUtils.moveFile(new File(templateDir, "src/test/groovy/HandlerSpec.groovy"), new File(testPackageDir, "${functionName}HandlerSpec.groovy"))
FileUtils.moveFile(new File(templateDir, "src/test/groovy/ServiceSpec.groovy"), new File(testPackageDir, "${functionName}ServiceSpec.groovy"))

if (isNewEvent(inputEventClass)) {
    FileUtils.copyFile(new File(templateDir, "src/main/groovy/Request.groovy"), new File(mainPackageDir, "${inputEventClass}.groovy"))
}

if (isNewEvent(outputEventClass)) {
    FileUtils.copyFile(new File(templateDir, "src/main/groovy/Response.groovy"), new File(mainPackageDir, "${outputEventClass}.groovy"))
}

FileUtils.deleteQuietly(new File(templateDir, "src/main/groovy/Request.groovy"))
FileUtils.deleteQuietly(new File(templateDir, "src/main/groovy/Response.groovy"))


// must be handled manaully because it's hard-excluded
File gitignore = new File(projectDir, '.gitignore')
gitignore.text = GIT_IGNORE_TEXT

Map<String, Object> loadAssist(String filename) {
    return yaml.load(new File(templateDir, ".lazybones/assist/$filename").newInputStream())
}

String toYaml(Object o) {
    return yaml.dumpAsMap(o)
}

String readClass(Map<String,Object> inputEvents, String type) {
    println "\nWhat is the $type event type (order number, shortcut, fully-qualified name or name of the new event):"

    List<Map.Entry<String, String>> eventsList = inputEvents.entrySet().toList()

    eventsList.eachWithIndex{ e,  i ->
        println "${i.toString().padLeft(6)}: ${e.key.padRight(30)} - ${e.value}"
    }

    String inputEventClass = null

    while (!inputEventClass) {
        String input = ask(":", "", type)

        switch (input) {
            case inputEvents.keySet():
                inputEventClass = inputEvents[input]
                break
            case ~/\d+/:
                inputEventClass = eventsList[input.toInteger()].value
                break
            case ~/(\w+)(\.\w+)+/:
                inputEventClass = input
                break
            case this.&isNewEvent:
                inputEventClass = input
                break
            default:
                println "Illegal $type $input"
        }
    }

    println "The $type event will be $inputEventClass\n"

    return inputEventClass
}

List<String> readLibs(Map<String,Object> libs) {
    println "\nDo you need any additional libraries (comma-separated list of numbers or ids):"

    List<Map.Entry<String, String>> libsList = libs.entrySet().toList()

    libsList.eachWithIndex{ e,  i ->
        println "${i.toString().padLeft(6)}: ${e.key.padRight(30)} - ${e.value.name} [${e.value.dependency*.coordinates.join(',')}]"
    }

    String selected = ask(":", "", "libs")

    if (!selected) {
        println "No libraries selected!"
        return []
    }

    List<Map> selectedLibs = []
    for (String input in selected.split(/\s*,\s*/)) {
        switch (input) {
            case libs.keySet():
                selectedLibs << libs[input]
                break
            case ~/\d+/:
                selectedLibs << libsList[input.toInteger()].value
                break
            default:
                println "Illegal library selection: $input"
        }
    }

    for (Map selectedLib in selectedLibs) {
        println "Selected library $selectedLib.name"
    }

    println()

    return selectedLibs
}

void safeProcessTemplates(String pattern, Map attrs) {
    try {
        processTemplates(pattern, attrs)
    } catch (Exception e) {
        System.err.println "Exception processing $pattern: $e"
    }
}

static String extractSimpleName(String name) {
    name.substring(name.lastIndexOf('.') + 1)
}

static String newEventString(String simpleClassName) {
    switch(simpleClassName) {
        case Map.simpleName:
            return "[timestamp: ${System.currentTimeMillis()}]"
        case String.simpleName:
            return "\"${UUID.randomUUID()}\""
        default:
            return "new $simpleClassName()"
    }
}

static boolean isNewEvent(String eventName) {
    return eventName ==~ /[A-Z]\w+/
}

static boolean requiresImport(String eventName) {
    if (isNewEvent(eventName)) {
        return false
    }
    if (eventName.startsWith('java.')) {
        return false
    }
    return true
}