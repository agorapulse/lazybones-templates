@Grapes(
    @Grab(group='org.yaml', module='snakeyaml', version='1.25')
)

import uk.co.cacoethes.util.NameType
import org.apache.commons.io.FileUtils
import org.yaml.snakeyaml.Yaml
import groovy.transform.Field
import groovy.text.SimpleTemplateEngine

@Field Yaml yaml = new Yaml()
@Field SimpleTemplateEngine ste = new SimpleTemplateEngine()

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

println()
println('=' * 60)
println('Micronaut Function Groovy Template by Agorapulse'.center(60))
println('=' * 60)
println()

String functionNameSuggestion = transformText(projectDir.name, from: NameType.HYPHENATED, to: NameType.CAMEL_CASE)
String functionName = ask("What is the name of the function 'AccountStatistics' [$functionNameSuggestion]: ", functionNameSuggestion, "name")

String standaloneSuggestion = isProbablyStandalone(projectDir) ? 'yes' : 'no'
boolean standalone = toBooleanAnswer(ask("Is this function standalone (not a subproject) [$standaloneSuggestion]: ", standaloneSuggestion, "standalone"))

String orgSuggestion = projectDir.parentFile.name.toLowerCase()
String slugSuggestion = "$orgSuggestion/$projectDir.name"

String slug = standalone ? ask("Do you plan to share the project on GitHub? What will be the slug [$slugSuggestion]: ", slugSuggestion, "slug") : ''

String groupSuggestion = 'com.' + orgSuggestion.replaceAll('[-_]', '.')
String group = ask("What is the group (base package) of the new project [$groupSuggestion]: ", groupSuggestion, "group")

String pgkSuggestion = (group + '.' + transformText(functionName, from:  NameType.CAMEL_CASE, to: NameType.HYPHENATED)).replaceAll('[-_]', '.').toLowerCase()
String pkg = ask("What is the base package of the new project [$pgkSuggestion]: ", pgkSuggestion, "pkg")

String region = ask("In which region you want to create the function [eu-west-1]: ", 'eu-west-1', "region")
String profile = ask("Which AWS profile you want to use for deployment [beta]: ", 'beta', "profile")
String port = ask("Which port would you like to use for running the function locally [8080]: ", '8080', "port")

String inputEventClass = readClass([req: "${functionName}Request"] + loadAssist('input-events.yml'), 'input')
String outputEventClass = readClass([resp: "${functionName}Response"] + loadAssist('output-events.yml'), 'output')
List<Map> selectedLibs = readLibs(loadAssist('libraries.yml'))

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
        slug: slug,
        standalone: standalone,
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
        isSelected: this.&isSelected.rcurry(selectedLibs),
]

selectedLibs.each {
    if (it.documentation) {
        it.documentation = renderText(attrs, it.documentation)
    }
    if (it.files) {
        it.files.each {
            it.path = renderText(attrs, it.path)
            it.description = renderText(attrs, it.description)
        }
    }
}

safeProcessTemplates "settings.gradle", attrs
safeProcessTemplates "build.gradle", attrs
safeProcessTemplates "gradle.properties", attrs
safeProcessTemplates "micronaut-cli.yml", attrs
safeProcessTemplates "README.md", attrs
safeProcessTemplates "request.http", attrs

safeProcessTemplates ".github/workflows/*.yml", attrs

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
FileUtils.moveFile(new File(templateDir, "src/main/groovy/Configuration.groovy"), new File(mainPackageDir, "${functionName}Configuration.groovy"))
FileUtils.moveFile(new File(templateDir, "src/test/groovy/HandlerSpec.groovy"), new File(testPackageDir, "${functionName}HandlerSpec.groovy"))
FileUtils.moveFile(new File(templateDir, "src/test/groovy/ServiceSpec.groovy"), new File(testPackageDir, "${functionName}ServiceSpec.groovy"))

final List<String> availableRelativeDirs = [
        'mainPackageRelativeDir',
        'mainResourcesRelativeDir',
        'testPackageRelativeDir',
        'testResourcesRelativeDir',
        'testPackageResourcesRelativeDir',
]

for (lib in selectedLibs) {
    File libTemplatesDir = new File(templateDir, ".lazybones/assist/templates/$lib.id")
    if (libTemplatesDir.exists()) {
        safeProcessTemplates ".lazybones/assist/templates/$lib.id/**/*.*", attrs
        for (relativeDir in availableRelativeDirs) {
            File libraryRelativeDir = new File(templateDir, ".lazybones/assist/templates/$lib.id/$relativeDir")
            if (libraryRelativeDir.exists()) {
                FileUtils.copyDirectory(libraryRelativeDir, new File(projectDir, attrs[relativeDir]))
                FileUtils.deleteDirectory(libraryRelativeDir)
            }
        }
    }
}

if (isNewEvent(inputEventClass)) {
    FileUtils.moveFile(new File(templateDir, "src/main/groovy/Request.groovy"), new File(mainPackageDir, "${inputEventClass}.groovy"))
} else {
    FileUtils.deleteQuietly(new File(templateDir, "src/main/groovy/Request.groovy"))
}

if (isNewEvent(outputEventClass)) {
    FileUtils.moveFile(new File(templateDir, "src/main/groovy/Response.groovy"), new File(mainPackageDir, "${outputEventClass}.groovy"))
} else {
    FileUtils.deleteQuietly(new File(templateDir, "src/main/groovy/Response.groovy"))
}

// must be handled manaully because it's hard-excluded
writeGitIgnoreFile(GIT_IGNORE_TEXT)

if (!slug) {
    FileUtils.deleteDirectory(new File(templateDir, ".github"))
}

if (!standalone) {
    FileUtils.deleteDirectory(new File(templateDir, "gradle"))
    FileUtils.deleteQuietly(new File(templateDir, "gradlew"))
    FileUtils.deleteQuietly(new File(templateDir, "gradlew.bat"))
    FileUtils.deleteQuietly(new File(templateDir, "settings.gradle"))
}

// clean up .lazybones directory
FileUtils.deleteDirectory(new File(templateDir, ".lazybones/assist"))

private void writeGitIgnoreFile(String GIT_IGNORE_TEXT) {
    File gitignore = new File(projectDir, '.gitignore')
    gitignore.text = GIT_IGNORE_TEXT
}

private Map<String, Object> loadAssist(String filename) {
    return yaml.load(new File(templateDir, ".lazybones/assist/$filename").newInputStream())
}

private String toYaml(Object o) {
    return yaml.dumpAsMap(o)
}

private String readClass(Map<String,Object> inputEvents, String type) {
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

private List<Map> readLibs(Map<String,Object> libs) {
    println "\nDo you need any additional libraries (comma-separated list of numbers or ids):"

    List<Map.Entry<String, Map>> libsList = libs.entrySet().toList()

    libs.each { e ->
        e.value.put('id', e.key)
    }

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
            case 'all':
                selectedLibs = libs.values().toList()
                break
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

    while (selectedLibs*.requires.flatten().grep() && !selectedLibs*.requires.flatten().grep().every { it in selectedLibs*.id.flatten()}) {
        for (req in (selectedLibs*.requires.flatten().grep() -  selectedLibs*.id.flatten())) {
            selectedLibs << libs[req]
        }
    }

    for (Map selectedLib in selectedLibs) {
        println "Selected library $selectedLib.name"
    }

    println()

    return selectedLibs
}

private void safeProcessTemplates(String pattern, Map attrs) {
    try {
        processTemplates(pattern, attrs)
    } catch (Exception e) {
        System.err.println "Exception processing $pattern:"
        e.printStackTrace()
        throw e
    }
}

private String renderText(Map<String, Object> attrs, String templateString) {
    ste.createTemplate(templateString).make(attrs).toString().trim()
}

private static String extractSimpleName(String name) {
    name.substring(name.lastIndexOf('.') + 1)
}

private static String newEventString(String simpleClassName) {
    switch(simpleClassName) {
        case Map.simpleName:
            return "[timestamp: ${System.currentTimeMillis()}]"
        case String.simpleName:
            return "'${UUID.randomUUID()}'"
        default:
            return "new $simpleClassName()"
    }
}

private static boolean isNewEvent(String eventName) {
    return eventName ==~ /[A-Z]\w+/
}

private static boolean requiresImport(String eventName) {
    if (isNewEvent(eventName)) {
        return false
    }
    if (eventName.startsWith('java.')) {
        return false
    }
    return true
}

private static boolean isProbablyStandalone(File projectDir) {
    File parent = projectDir.parentFile
    for (int i = 0; i < 3; i++) {
        if (new File(parent, 'build.gradle').exists() || new File(parent, 'settings.gradle').exists()) {
            return false
        }
        try {
            parent = parent.parentFile
        } catch (Exception e) {
            return true
        }
    }
    return true
}

private static boolean toBooleanAnswer(String answer) {
    switch (answer.toLowerCase()) {
        case ['y', 'yes', 't', 'true']: return true
        default: return false
    }
}

private static boolean isSelected(String lib, List<Map> selectedLibs) {
    return selectedLibs.any { it.id == lib }
}