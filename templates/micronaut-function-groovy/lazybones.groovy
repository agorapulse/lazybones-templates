import uk.co.cacoethes.util.NameType

final String MICRONAUT_LIBRARIES_VERSION = '1.2.9'

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

final Map<String, String> INPUT_EVENTS = [
        map: "java.util.Map",
        ag: "com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent",
        agv2: "com.amazonaws.services.lambda.runtime.events.APIGatewayV2ProxyRequestEvent",
        cf: "com.amazonaws.services.lambda.runtime.events.CloudFrontEvent",
        log: "com.amazonaws.services.lambda.runtime.events.CloudWatchLogsEvent",
        cc: "com.amazonaws.services.lambda.runtime.events.CodeCommitEvent",
        cognito: "com.amazonaws.services.lambda.runtime.events.CognitoEvent",
        config: "com.amazonaws.services.lambda.runtime.events.ConfigEvent",
        dynamodb: "com.amazonaws.services.lambda.runtime.events.DynamodbEvent",
        iot: "com.amazonaws.services.lambda.runtime.events.IoTButtonEvent",
        kinesis_analytics_firehose: "com.amazonaws.services.lambda.runtime.events.KinesisAnalyticsFirehoseInputPreprocessingEvent",
        kinesis_analytics_output: "com.amazonaws.services.lambda.runtime.events.KinesisAnalyticsOutputDeliveryEvent",
        kinesis_analytics_streams: "com.amazonaws.services.lambda.runtime.events.KinesisAnalyticsStreamsInputPreprocessingEvent",
        kinesis: "com.amazonaws.services.lambda.runtime.events.KinesisEvent",
        kinesis_firehose: "com.amazonaws.services.lambda.runtime.events.KinesisFirehoseEvent",
        lex: "com.amazonaws.services.lambda.runtime.events.LexEvent",
        s3: "com.amazonaws.services.lambda.runtime.events.S3Event",
        scheduled: "com.amazonaws.services.lambda.runtime.events.ScheduledEvent",
        sns: "com.amazonaws.services.lambda.runtime.events.SNSEvent",
        sqs: "com.amazonaws.services.lambda.runtime.events.SQSEvent",
]

final Map<String, String> OUTPUT_EVENTS = [
        string: "java.lang.String",
        map: "java.util.Map",
        ag: "com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent",
        agv2: "com.amazonaws.services.lambda.runtime.events.APIGatewayV2ProxyResponseEvent",
        kinesis_analytics_output: "com.amazonaws.services.lambda.runtime.events.KinesisAnalyticsOutputDeliveryResponse",
        kinesis_analytics_firehose: "com.amazonaws.services.lambda.runtime.events.KinesisAnalyticsInputPreprocessingResponse",
]


final Map<String, String> LIBRARIES = [
        'ag-ws': "com.agorapulse:micronaut-aws-sdk-ag-ws:$MICRONAUT_LIBRARIES_VERSION",
        'cw': "com.agorapulse:micronaut-aws-sdk-cloudwatch:$MICRONAUT_LIBRARIES_VERSION",
        'core': "com.agorapulse:micronaut-aws-sdk-core:$MICRONAUT_LIBRARIES_VERSION",
        'dynamodb': "com.agorapulse:micronaut-aws-sdk-dynamodb:$MICRONAUT_LIBRARIES_VERSION",
        'kinesis': "com.agorapulse:micronaut-aws-sdk-kinesis:$MICRONAUT_LIBRARIES_VERSION",
        'kinesis-worker': "com.agorapulse:micronaut-aws-sdk-kinesis-worker:$MICRONAUT_LIBRARIES_VERSION",
        's3': "com.agorapulse:micronaut-aws-sdk-s3:$MICRONAUT_LIBRARIES_VERSION",
        'ses': "com.agorapulse:micronaut-aws-sdk-ses:$MICRONAUT_LIBRARIES_VERSION",
        'sns': "com.agorapulse:micronaut-aws-sdk-sns:$MICRONAUT_LIBRARIES_VERSION",
        'sqs': "com.agorapulse:micronaut-aws-sdk-sqs:$MICRONAUT_LIBRARIES_VERSION",
        'sts': "com.agorapulse:micronaut-aws-sdk-sts:$MICRONAUT_LIBRARIES_VERSION",
]

String functionNameSuggestion = transformText(projectDir.name, from: NameType.HYPHENATED, to: NameType.CAMEL_CASE)
String functionName = ask("What is the name of the function 'AccountStatistics' [$functionNameSuggestion]: ", functionNameSuggestion, "name")

String groupSuggestion = 'com.' + projectDir.parentFile.name.replaceAll('[-_]', '.').toLowerCase()
String group = ask("What is the Maven coordinates group of the new project [$groupSuggestion]: ", groupSuggestion, "group")

String pgkSuggestion = (group + '.' + projectDir.name).replaceAll('[-_]', '.').toLowerCase()
String pkg = ask("What is the base package of the new project [$pgkSuggestion]: ", pgkSuggestion, "pkg")

String inputEventClass = readClass(INPUT_EVENTS, 'input')
String outputEventClass = readClass(OUTPUT_EVENTS, 'output')
List<String> selectedLibs = readLibs(LIBRARIES)

String functionNameHyphens = transformText(functionName, from: NameType.CAMEL_CASE, to: NameType.HYPHENATED)
String functionNameProperty = transformText(functionName, from: NameType.CAMEL_CASE, to: NameType.PROPERTY)
String inputEventClassSimple = extractSimpleName(inputEventClass)
String outputEventClassSimple = extractSimpleName(outputEventClass)

Map attrs = [
        functionName: functionName,
        functionNameHypens: functionNameHyphens,
        group: group,
        pkg: pkg,
        inputEventClass: inputEventClass,
        inputEventClassSimple: inputEventClassSimple,
        outputEventClass: outputEventClass,
        outputEventClassSimple: outputEventClassSimple,
        selectedLibs: selectedLibs,
        micronautLibrariesVersion: MICRONAUT_LIBRARIES_VERSION
]


processTemplates "settings.gradle", attrs
processTemplates "build.gradle", attrs
processTemplates "gradle.properties", attrs
processTemplates "micronaut-cli.yml", attrs

File mainPackageDir = new File(projectDir, "src/main/groovy/${pkg.replace('.', '/')}")
File mainResourcesDir = new File(projectDir, "src/main/resources/")
File testPackageDir = new File(projectDir, "src/test/groovy/${pkg.replace('.', '/')}")
File testResourcesDir = new File(projectDir, "src/test/resources/")
File testPackageResourcesDir = new File(testResourcesDir, pkg.replace('.', '/'))

mainPackageDir.mkdirs()
mainResourcesDir.mkdirs()
testPackageDir.mkdirs()
testResourcesDir.mkdirs()
testPackageResourcesDir.mkdirs()

new File(mainResourcesDir, 'sentry.properties').text = """
async=false
stacktrace.app.packages=$pkg
"""

new File(mainResourcesDir, 'application.yml').text = """
micronaut:
  function:
    name: $functionNameHyphens
"""

new File(mainPackageDir, 'Application.java').text = """
package $pkg;

import io.micronaut.runtime.Micronaut;

public class Application {

    public static void main(String[] args) {
        Micronaut.run(Application.class);
    }
    
}
"""

new File(mainPackageDir, "${functionName}Handler.groovy").text = """
package $pkg

import $inputEventClass
import $outputEventClass
import groovy.transform.CompileStatic
import groovy.transform.InheritConstructors
import io.micronaut.function.executor.FunctionInitializer
import io.micronaut.function.FunctionBean

import javax.inject.Inject
import java.util.function.Function

@CompileStatic
@FunctionBean(
        value = '$functionNameHyphens',
        method = 'apply'
)
@InheritConstructors
class ${functionName}Handler extends FunctionInitializer implements Function<$inputEventClassSimple, $outputEventClassSimple> {

    private final ${functionName}Service ${functionNameProperty}Service

    ${functionName}Handler(${functionName}Service ${functionNameProperty}Service) {
        this.${functionNameProperty}Service = ${functionNameProperty}Service
    }

    @Override
    $outputEventClassSimple apply($inputEventClassSimple event) {
         return ${functionNameProperty}Service.handle(event)
    }

    /**
     * This main method allows running the function as a CLI application using: echo '{}' | java -jar function.jar 
     * where the argument to echo is the JSON to be parsed.
     */
    static void main(String...args) throws IOException {
        ${functionName}Handler function = new ${functionName}Handler()
        function.run(args, { context -> function.apply(context.get(${inputEventClassSimple}.class)) })
    }    
}
"""

new File(mainPackageDir, "${functionName}Service.groovy").text = """
package $pkg

import $inputEventClass
import $outputEventClass

import com.agorapulse.micronaut.log4aws.LogError

import groovy.transform.CompileStatic

import javax.inject.Singleton

@Singleton
@CompileStatic
class ${functionName}Service {

    @LogError
    $outputEventClassSimple handle($inputEventClassSimple event) {
        throw new UnsupportedOperationException('TODO: implement')
    }

}
"""

new File(testPackageDir, "${functionName}HandlerSpec.groovy").text = """
package $pkg

import $inputEventClass
import $outputEventClass
import spock.lang.AutoCleanup
import spock.lang.Specification

class ${functionName}HandlerSpec extends Specification {

    ${functionName}Service service = Mock(${functionName}Service)

    @AutoCleanup ${functionName}Handler handler = new ${functionName}Handler(service)

    void 'handle event'() {
        given:
            $inputEventClassSimple event = ${newEventString(inputEventClassSimple)}
            $outputEventClassSimple expectedOutput = ${newEventString(outputEventClassSimple)}
        when:
            Map output = handler.apply(event)
        then:
            output.is expectedOutput

            1 * service.handle(event) >> expectedOutput
    }

}
"""


new File(testPackageDir, "${functionName}ServiceSpec.groovy").text = """
package $pkg

import $inputEventClass
import $outputEventClass
import io.micronaut.context.ApplicationContext
import spock.lang.AutoCleanup
import spock.lang.Specification

class ${functionName}ServiceSpec extends Specification {

    @AutoCleanup ApplicationContext context

    ${functionName}Service service

    void setup() {
        context = ApplicationContext.build('test').build()
        // TODO: register mock collaborators
        // context.registerSingleton(Foo, fooMock)
        context.start()

        service = context.getBean(${functionName}Service)
    }

    void 'handle event'() {
        given:
            $inputEventClassSimple event = ${newEventString(inputEventClassSimple)}
            $outputEventClassSimple expectedOutput = ${newEventString(outputEventClassSimple)}
        when:
            $outputEventClassSimple output = service.handle(event)
        then:
            thrown UnsupportedOperationException

            // TODO: verify output
            // output == expectedOutput
    }

}
"""

//processTemplates "README.md", attrs
//
//File firstSubproject = new File(projectDir, "subprojects/$projectId")
//firstSubproject.mkdirs()
//
//File subprojectGradleFile = new File(firstSubproject, "${projectId}.gradle")
//subprojectGradleFile.text = """
//// delete if this subproject should not be published to BinTray
//config {
//    bintray {
//        enabled = true
//    }
//}
//
//dependencies {
//    // add project's dependencies
//}
//"""
//


File gitignore = new File(projectDir, '.gitignore')
gitignore.text = GIT_IGNORE_TEXT


String readClass(Map<String,String> inputEvents, String type) {
    List<Map.Entry<String, String>> eventsList = inputEvents.entrySet().toList()

    eventsList.eachWithIndex{ e,  i ->
        println "${i.toString().padLeft(2)}: ${e.key.padRight(30)} - ${e.value}"
    }

    String inputEventClass = null

    while (!inputEventClass) {
        String input = ask("What is the $type event type (order number, shortcut or fully-qualified name): ", "", type)

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
            default:
                println "Illegal $type $input"
        }
    }

    println "The $type event will be $inputEventClass\n"

    return inputEventClass
}

List<String> readLibs(Map<String,String> libs) {
    List<Map.Entry<String, String>> libsList = libs.entrySet().toList()

    libsList.eachWithIndex{ e,  i ->
        println "${i.toString().padLeft(2)}: ${e.key.padRight(30)} - ${e.value}"
    }

    String selected = ask("Do you need any additional libraries (comma-separated list of numbers or ids): ", "", "libs")

    List<String> selectedLibs = []
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

    for (String selectedLib in selectedLibs) {
        println "Selected library $selectedLib"
    }

    println()

    return selectedLibs
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