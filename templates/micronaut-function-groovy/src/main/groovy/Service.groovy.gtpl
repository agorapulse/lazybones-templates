package $pkg

<%
    List<String> toBeInjected = []
    List<String> toBeAssigned = []
    List<String> toBeImported = []

    for (service in ([[name: 'configuration', type: "${functionName}Configuration"]] + selectedLibs.findAll { it.provides } *.provides).flatten().grep()) {
        toBeInjected << "${extractSimpleName(service.type)} $service.name"
        toBeAssigned << service.name
        if (service.type.contains('.')) {
            toBeImported << service.type
        }
    }

    Set<String> imports = [
        'com.agorapulse.micronaut.log4aws.LogError',
        'groovy.transform.CompileStatic',
        'groovy.util.logging.Slf4j',
        'javax.inject.Singleton',
    ] as TreeSet

    imports.addAll toBeImported

    if (requiresImport(inputEventClass)) {
        imports << inputEventClass
    }

    if (requiresImport(outputEventClass)) {
        imports << outputEventClass
    }

    if (isSelected('snitch')) {
        imports << 'com.agorapulse.micronaut.snitch.Snitch'
    }

    for (i in imports) {
        out << 'import ' << i << '\n'
    }
%>
@Slf4j
@Singleton
@CompileStatic
class ${functionName}Service {

<%
    for (service in toBeInjected) {
        out << "    private final $service\n"
    }
%>
    <% if (toBeInjected.size() > 5) { %>@SuppressWarnings('ParameterCount')\n    <% } %>${functionName}Service(
        ${toBeInjected.join(',\n        ')}
    ) {<%
        for (field in toBeAssigned) {
            out << '\n        ' << "this.$field = $field"
        }%>
    }

    <% if (isSelected('snitch')) { %>@Snitch <% } %>@LogError
    $outputEventClassSimple handle($inputEventClassSimple event) {
        // TODO: implement
        log.debug "got event: \$event"
        throw new UnsupportedOperationException('TODO: implement')
    }

}
