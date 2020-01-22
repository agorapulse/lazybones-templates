package $pkg

<%
    Set<String> imports = [
        'com.agorapulse.micronaut.log4aws.LogError',
        'groovy.transform.CompileStatic',
        'javax.inject.Singleton',
    ] as TreeSet
    if (requiresImport(inputEventClass)) { imports << inputEventClass }
    if (requiresImport(outputEventClass)) { imports << outputEventClass }
    if (isSelected('snitch')) { imports << 'com.agorapulse.micronaut.snitch.Snitch' }
    for (i in imports) {
        out << 'import ' << i << '\n'
    }
%>
@Singleton
@CompileStatic
class ${functionName}Service {


    <% if (isSelected('snitch')) { %>@Snitch <% } %>@LogError
    $outputEventClassSimple handle($inputEventClassSimple event) {
        // TODO: implement
        throw new UnsupportedOperationException('TODO: implement')
    }

}