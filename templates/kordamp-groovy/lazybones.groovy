import uk.co.cacoethes.util.NameType

String projectId = ask("What is the ID of the project such as 'micronaut-aws-sdk' [$projectDir.name]:", projectDir.name, "id")

String githubOrg = ask("What is your GitHub organization or username [$projectDir.parentFile.name]:", projectDir.parentFile.name, "org")

String slug = "$githubOrg/$projectId"

String groupSuggestion = 'com.' + githubOrg.replaceAll('[-_]', '.')
String group = ask("What is the Maven coordinates group of the new project [$groupSuggestion]:", groupSuggestion, "group")

String pgkSuggestion = (group + '.' + projectId).replaceAll('[-_]', '.')
String pkg = ask("What is the base package of the new project [$pgkSuggestion]:", pgkSuggestion, "pkg")

String nameSuggestion = transformText(projectId, from: NameType.HYPHENATED, to: NameType.NATURAL)
String name = ask("What is the human readable name of the new project [$nameSuggestion]:", nameSuggestion, "name")

String descriptionSuggestion = "$nameSuggestion Library"
String description = ask("What is the description of the project [$descriptionSuggestion]:", descriptionSuggestion, "desc")

String vendorSuggestion = transformText(githubOrg, from: NameType.HYPHENATED, to: NameType.NATURAL)
String vendor = ask("What is the human readable vendor name of your organization [$vendorSuggestion]:", vendorSuggestion, "vendor")

String devName = ''

try {
    devName = 'git config --get user.name'.execute().text.trim()
} catch (Exception e) {
    // no git present
}

if (devName) {
    devName = ask("Who are you? [$devName]:", devName, "dev.name")
}

while (!devName) {
    devName = ask("Who are you?", '', "dev.name")
}

String devIdSuggestion = System.getenv('BINTRAY_USER') ?: System.getenv('LOGNAME') ?:  System.getenv('USER')
String devId = ask("What is your preferred user id (nickname) [$devIdSuggestion]:", devIdSuggestion, "dev.id")

String bintrayOrg = ask("What is your BinTray organization or username [$githubOrg]:", githubOrg, "bintray.org")
String bintrayRepo = ask("What is your BinTray repository name [maven]:", 'maven', "bintray.repo")

Map attrs = [
    org: githubOrg,
    projectId: projectId,
    name: name,
    vendor: vendor,
    desc: description,
    devId: devId,
    devName: devName,
    bintrayOrg: bintrayOrg,
    bintrayRepo: bintrayRepo,
    slug: slug,
    group: group
]

processTemplates "settings.gradle", attrs
processTemplates "build.gradle", attrs
processTemplates "gradle.properties", attrs
processTemplates "README.md", attrs

File firstSubproject = new File(projectDir, "subprojects/$projectId")
firstSubproject.mkdirs()

File subprojectGradleFile = new File(firstSubproject, "${projectId}.gradle")
subprojectGradleFile.createNewFile()
subprojectGradleFile.text << """
// delete if this subproject should not be published to BinTray
config {
    bintray {
        enabled = true
    }
}

dependencies {
    // add project's dependencies
}
"""

new File(firstSubproject, "src/main/groovy/${pkg.replace('.', '/')}").mkdirs()
new File(firstSubproject, "src/main/resources/").mkdirs()
new File(firstSubproject, "src/test/groovy/${pkg.replace('.', '/')}").mkdirs()
new File(firstSubproject, "src/test/resources/").mkdirs()

File gitignore = new File(projectDir, '.gitignore')
gitignore.createNewFile()
gitignore.text << [
    "# Gradle",
    "build/",
    ".gradle/",
    "# IDEs",
    "*.c9",
    "*.iml",
    "*.ipr",
    "*.iws",
    "*.vscode",
    ".idea/",
    ".asscache",
    "MANIFEST.MF",
    "out",
    "# PAW",
    "*.paw",
    "# Redis",
    "*.rdb",
].join('\n')


