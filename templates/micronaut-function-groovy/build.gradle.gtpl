plugins {
    id "com.github.johnrengelman.shadow" version "5.0.0"
    id "jp.classmethod.aws.lambda" version "0.39"
    id "net.ltgt.apt-eclipse" version "0.21"
    id "groovy"
    id "application"
}



version "0.1"
group "$group"

repositories {
    mavenCentral()
    maven { url "https://jcenter.bintray.com" }
    maven { url "https://dl.bintray.com/agorapulse/libs" }
    maven { url "https://repo.grails.org/grails/core" }
}

configurations {
    // for dependencies that are needed for development only
    developmentOnly 
}

dependencies {
    // custom dependencies
    <% for (dep in selectedLibs*.dependency.flatten().unique { it.coordinates } .sort { it.scope }) { %>
    $dep.scope "$dep.coordinates" <% } %>

    // default dependencies
    annotationProcessor platform("io.micronaut:micronaut-bom:" + micronautVersion)
    annotationProcessor "io.micronaut:micronaut-inject-java"
    annotationProcessor "io.micronaut:micronaut-validation"

    implementation platform("io.micronaut:micronaut-bom:" + micronautVersion)
    implementation "io.micronaut:micronaut-inject"
    implementation "io.micronaut:micronaut-validation"
    implementation "io.micronaut:micronaut-runtime"
    implementation "io.micronaut:micronaut-function-aws"
    implementation "io.micronaut:micronaut-runtime-groovy"

    developmentOnly platform("io.micronaut:micronaut-bom:" + micronautVersion)
    developmentOnly "io.micronaut:micronaut-http-server-netty"
    developmentOnly "io.micronaut:micronaut-function-web"

    compileOnly "io.micronaut:micronaut-inject-groovy"

    implementation 'com.amazonaws:aws-lambda-java-core:' + awsLambdaCoreVersion
    implementation 'com.amazonaws:aws-lambda-java-events:' + awsLambdaEventsVersion

    implementation "com.agorapulse:micronaut-log4aws:1.0.0"

    testAnnotationProcessor platform('io.micronaut:micronaut-bom:' + micronautVersion)
    testAnnotationProcessor "io.micronaut:micronaut-inject-java"

    testImplementation platform('io.micronaut:micronaut-bom:' + micronautVersion)
    testImplementation("org.spockframework:spock-core") {
        exclude group: "org.codehaus.groovy", module: "groovy-all"
    }
    testImplementation 'net.bytebuddy:byte-buddy:1.10.6'

    testImplementation "io.micronaut:micronaut-inject-groovy"
    testImplementation "io.micronaut.test:micronaut-test-spock"
    testImplementation "io.micronaut:micronaut-function-client"
}

test.classpath += configurations.developmentOnly

shadowJar {
    transform(com.github.jengelman.gradle.plugins.shadow.transformers.Log4j2PluginsCacheFileTransformer)
}

tasks.withType(JavaCompile){
    options.encoding = "UTF-8"
    options.compilerArgs.add('-parameters')
}

tasks.withType(GroovyCompile) {
    groovyOptions.forkOptions.jvmArgs.add('-Dgroovy.parameters=true')
}

tasks.withType(Test){
    environment 'TEST_RESOURCES_FOLDER', new File(project.projectDir, 'src/test/resources').canonicalPath
    systemProperty 'user.timezone', 'UTC'
    systemProperty 'user.language', 'en'
}


shadowJar {
    mergeServiceFiles()
}

run.classpath += configurations.developmentOnly
run.jvmArgs('-noverify', '-XX:TieredStopAtLevel=1', '-Dcom.sun.management.jmxremote', '-Dmicronaut.environments=dev')
mainClassName = "${pkg}.Application"
applicationDefaultJvmArgs = [""]

jar {
    manifest {
        attributes 'Main-Class': mainClassName
    }
}

<% if (region || profile) { %>
aws {
    <% if (region) { %>region = '$region'<% } %>
    <% if (profile) { %>profileName = '$profile'<% } %>
}
<% } %>

task deploy(type: jp.classmethod.aws.gradle.lambda.AWSLambdaMigrateFunctionTask, dependsOn: shadowJar) {
    functionName = "$functionName"
    handler = "$pkg.${functionName}Handler::apply"
    role = 'arn:aws:iam::' + aws.accountId + ':role/lambda_basic_execution'
    runtime = com.amazonaws.services.lambda.model.Runtime.Java8
    zipFile = shadowJar.archivePath
    memorySize = 512
    timeout = 120
}
